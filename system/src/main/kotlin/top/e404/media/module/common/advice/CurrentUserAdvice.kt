package top.e404.media.module.common.advice

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.annotation.Order
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import top.e404.media.module.common.entity.database.RoleDo
import top.e404.media.module.common.entity.database.UserDo
import top.e404.media.module.common.entity.database.UserTokenDo
import top.e404.media.module.common.service.database.RoleService
import top.e404.media.module.common.service.database.UserService
import top.e404.media.module.common.service.database.UserTokenService
import top.e404.media.module.common.util.log
import top.e404.media.module.common.util.query
import top.e404.media.module.common.util.toJsonString
import java.util.concurrent.ConcurrentHashMap


/**
 * 用于在请求中获取该用户的信息
 */
@Aspect
@Order(1)
@Component
class CurrentUserAdvice {
    private val log = log()

    @set:Autowired
    lateinit var userService: UserService

    @set:Autowired
    lateinit var roleService: RoleService

    @set:Autowired
    lateinit var tokenService: UserTokenService

    /**
     * 注入当前用户数据
     *
     * @see CurrentUser
     */
    @Around("execution(* top.e404.media..controller..*Controller.*(..))")
    private fun injectUser(joinPoint: ProceedingJoinPoint): Any? {
        val requestAttributes = RequestContextHolder.getRequestAttributes() as ServletRequestAttributes?
        val request = requestAttributes!!.request
        val token = request.getHeader(HttpHeaders.AUTHORIZATION) ?: return joinPoint.proceed()
        log.debug("token: {}", token)

        // 缓存
        currentUserCache[token]?.let { cache ->
            if (!cache.isExpire) {
                log.debug("注入currentUser: {}", cache.toJsonString())
                currentUsers.set(cache)
                try {
                    return joinPoint.proceed()
                } finally {
                    currentUsers.remove()
                }
            }
        }

        val tokenDo = tokenService.getOne(query {
            eq(UserTokenDo::token, token)
        }) ?: return joinPoint.proceed()
        val userDo = userService.getById(tokenDo.userId!!)
        val roles = roleService.getRoleByUserId(tokenDo.userId!!).toSet()
        val perms = roles.flatMap { it.perms!! }.toSet()
        val current = CurrentUser(userDo, tokenDo, roles, perms)
        // 缓存
        currentUserCache[token] = current
        log.debug("缓存currentUser: {}", current.toJsonString())

        currentUsers.set(current)
        try {
            return joinPoint.proceed()
        } finally {
            currentUsers.remove()
        }
    }
}

internal val currentUserCache = ConcurrentHashMap<String, CurrentUser>()

/**
 * 删除所有当前用户缓存, 当`token`/`权限`/`角色`/`用户`更新时应手动调用
 */
fun refreshCurrentUserCache() = currentUserCache.clear()

internal val currentUsers = ThreadLocal<CurrentUser>()

/**
 * 当前请求对应的用户信息
 */
val currentUser: CurrentUser? get() = currentUsers.get()

/**
 * 注入的当前用户数据
 *
 * @property user 当前用户
 * @property token 当前token
 * @property roles 当前用户角色
 * @property perms 当前用户拥有的权限
 */
data class CurrentUser(
    val user: UserDo,
    val token: UserTokenDo,
    val roles: Set<RoleDo>,
    val perms: Set<String>
) {
    /**
     * 若token已过期则返回true
     */
    val isExpire get() = token.expireTime!! < System.currentTimeMillis()
}
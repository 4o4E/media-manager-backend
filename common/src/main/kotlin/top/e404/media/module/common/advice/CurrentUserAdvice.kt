package top.e404.media.module.common.advice

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.annotation.Order
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import top.e404.media.module.common.entity.auth.RoleDo
import top.e404.media.module.common.entity.auth.RolePermDo
import top.e404.media.module.common.entity.auth.UserDo
import top.e404.media.module.common.entity.auth.UserTokenDo
import top.e404.media.module.common.service.database.RolePermService
import top.e404.media.module.common.service.database.RoleService
import top.e404.media.module.common.service.database.UserService
import top.e404.media.module.common.service.database.UserTokenService


/**
 * 用于在请求中获取该用户的信息
 */
@Aspect
@Component
class CurrentUserAdvice {
    @set:Autowired
    lateinit var userService: UserService

    @set:Autowired
    lateinit var roleService: RoleService

    @set:Autowired
    lateinit var rolePermService: RolePermService

    @set:Autowired
    lateinit var tokenService: UserTokenService

    /**
     * 注入当前用户数据
     *
     * @see CurrentUser
     */
    @Around("execution(* top.e404.media.module.*.controller.*Controller.*(..))")
    @Order(0) // 最先执行
    private fun injectUser(joinPoint: ProceedingJoinPoint): Any? {
        val requestAttributes = RequestContextHolder.getRequestAttributes() as ServletRequestAttributes?
        val request = requestAttributes!!.request
        val token = request.getHeader(HttpHeaders.AUTHORIZATION)
        val tokenDo = token?.let { t ->
            tokenService.getOne(
                LambdaQueryWrapper<UserTokenDo>()
                    .eq(UserTokenDo::token, t)
            )
        } ?: return joinPoint.proceed()
        val userDo = userService.getById(tokenDo.id!!)
        val roles = roleService.getRoleById(tokenDo.id!!).toSet()
        val perms = rolePermService.list(
            LambdaQueryWrapper<RolePermDo>()
                .select(RolePermDo::role, RolePermDo::perm)
                .`in`(RolePermDo::role, roles.map(RoleDo::id))
        ).asSequence().map { it.perm!! }.toSet()
        currentUsers.set(CurrentUser(userDo, tokenDo, roles, perms))
        try {
            return joinPoint.proceed()
        } finally {
            currentUsers.remove()
        }
    }
}

internal val currentUsers = ThreadLocal<CurrentUser>()

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
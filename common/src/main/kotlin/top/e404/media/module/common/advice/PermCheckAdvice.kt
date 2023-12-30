package top.e404.media.module.common.advice

import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import top.e404.media.module.common.annontation.RequirePerm
import top.e404.media.module.common.exception.AuthorizationExpireException
import top.e404.media.module.common.exception.PermissionDeniedException
import top.e404.media.module.common.exception.UnauthorizedException
import top.e404.media.module.common.util.log

/**
 * 用于在请求controller层检查用户权限
 */
@Aspect
@Component
class PermCheckAdvice {
    private val log = log()

    /**
     * 检查当前用户是否有标记的权限
     */
    @Before("@annotation(ann)")
    @Order(1)
    private fun checkPerm(joinPoint: JoinPoint, ann: RequirePerm) {
        if (ann.perms.isEmpty()) return
        val current = currentUser ?: throw UnauthorizedException
        if (current.isExpire) throw AuthorizationExpireException

        // 缺失权限
        val lack = ann.perms.filter { it !in current.perms }
        if (lack.isNotEmpty()) {
            log.warn("用户{}访问接口{}时缺失权限{}", current.user.name, joinPoint.signature.name, lack)
            throw PermissionDeniedException
        }
    }
}


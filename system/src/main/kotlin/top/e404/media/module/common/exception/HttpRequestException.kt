package top.e404.media.module.common.exception

import org.springframework.http.ResponseEntity
import top.e404.media.module.common.entity.fail

/**
 * 基础异常
 */
open class HttpRequestException(
    override val message: String
) : Throwable(message) {
    open fun toResponseEntity() = ResponseEntity.ok(fail(message))
}

/**
 * 登录过期
 */
class AuthorizationExpireException : HttpRequestException("登录已过期")

/**
 * 未找到对应数据
 */
class NotFoundException(message: String = "未找到对应数据") : HttpRequestException(message)

/**
 * 无权访问
 */
class PermissionDeniedException : HttpRequestException("无权访问")

/**
 * 密码过于简单
 */
class SimplePasswordException : HttpRequestException("密码过于简单")

/**
 * 未登录
 */
class UnauthorizedException : HttpRequestException("未登录")

/**
 * 用户名或密码错误
 */
class WrongPasswordException : HttpRequestException("用户名或密码错误")
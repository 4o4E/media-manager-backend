package top.e404.media.module.common.exception

import org.springframework.http.ResponseEntity
import top.e404.media.module.common.entity.BaseResp

class NoChangeException : Exception()

/**
 * 基础异常
 */
open class HttpRequestException(
    val code: Int,
    override val message: String
) : Exception(message) {
    open fun toResponseEntity() = ResponseEntity.ok(BaseResp.fail(code, message))
}

interface FailReason {
    val code: Int
    val message: String

    fun thr(): Nothing = throw HttpRequestException(code, message)
}

@DslMarker
annotation class HttpResponseMarker

@HttpResponseMarker
fun fail(reason: FailReason, vararg params: Any?): Nothing {
    throw HttpRequestException(reason.code, reason.message.format(*params))
}

@HttpResponseMarker
fun notFound(name: String): Nothing = fail(CommonFail.NOT_FOUND, name)

enum class AuthFail(override val code: Int, override val message: String) : FailReason {
    UNAUTHORIZED(1001, "未登录"),
    PERMISSION_DENIED(1002, "无权访问"),
    AUTH_EXPIRE(1003, "登录已过期"),
    WRONG_PASSWORD(1004, "用户名或密码错误"),
    SIMPLE_PASSWORD(1005, "密码过于简单"),
    INVALID_TOKEN(1006, "无效的token"),
}

open class CustomFail(override val message: String) : FailReason {
    override val code: Int = 0
}

enum class CommonFail(override val code: Int, override val message: String) : FailReason {
    NOT_FOUND(2001, "未找到对应%s"),
    NO_CHANGE(2002, "没有修改"),
    BAD_OPERATOR(2003, "不允许的操作: %s"),
    BAD_REQUEST(2004, "请求数据格式错误: %s"),
}
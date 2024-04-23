package top.e404.media.module.common.exception

import org.springframework.http.HttpStatus

/**
 * 未登录
 */
object UnauthorizedException : CustomMessageException(
    "未登录",
    HttpStatus.UNAUTHORIZED
) {
    private fun readResolve(): Any = UnauthorizedException
    override val toResponseEntity by lazy { super.toResponseEntity }
}
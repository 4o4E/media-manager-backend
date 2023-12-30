package top.e404.media.module.common.exception

import org.springframework.http.HttpStatus

object UnauthorizedException : CustomMessageException(
    "Unauthorized",
    HttpStatus.UNAUTHORIZED
) {
    private fun readResolve(): Any = UnauthorizedException
    override val toResponseEntity by lazy { super.toResponseEntity }
}
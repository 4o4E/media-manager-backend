package top.e404.media.module.common.exception

import org.springframework.http.HttpStatus

object AuthorizationExpireException : CustomMessageException(
    "Authorization expire",
    HttpStatus.UNAUTHORIZED
) {
    private fun readResolve(): Any = AuthorizationExpireException
    override val toResponseEntity by lazy { super.toResponseEntity }
}
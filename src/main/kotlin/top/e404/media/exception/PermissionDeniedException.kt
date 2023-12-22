package top.e404.media.exception

import org.springframework.http.HttpStatus

object PermissionDeniedException : CustomMessageException(
    "Permission denied",
    HttpStatus.FORBIDDEN
) {
    private fun readResolve(): Any = PermissionDeniedException
    override val toResponseEntity by lazy { super.toResponseEntity }
}
package top.e404.media.module.common.exception

import org.springframework.http.HttpStatus

/**
 * 无权访问
 */
object PermissionDeniedException : CustomMessageException(
    "无权访问",
    HttpStatus.FORBIDDEN
) {
    private fun readResolve(): Any = PermissionDeniedException
}
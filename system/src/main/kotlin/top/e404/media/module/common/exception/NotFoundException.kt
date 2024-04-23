package top.e404.media.module.common.exception

import org.springframework.http.HttpStatus

/**
 * 未找到对应数据
 */
object NotFoundException : CustomMessageException(
    "未找到对应数据",
    HttpStatus.NOT_FOUND
) {
    private fun readResolve(): Any = PermissionDeniedException
    override val toResponseEntity by lazy { super.toResponseEntity }
}
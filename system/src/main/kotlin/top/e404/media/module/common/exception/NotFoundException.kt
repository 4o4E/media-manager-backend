package top.e404.media.module.common.exception

import org.springframework.http.HttpStatus

/**
 * 未找到对应数据
 */
class NotFoundException : CustomMessageException(
    "未找到对应数据",
    HttpStatus.NOT_FOUND
) {
    override val toResponseEntity by lazy { super.toResponseEntity }
}
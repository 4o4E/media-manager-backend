package top.e404.media.module.common.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

/**
 * 抛出此异常会响应400并以[message]作为body
 *
 * @property message 响应的消息
 */
open class CustomMessageException(
    override val message: String,
    private val status: HttpStatus = HttpStatus.BAD_REQUEST
) : Throwable(message) {
    open val toResponseEntity get() = ResponseEntity.status(status).body(message)
}
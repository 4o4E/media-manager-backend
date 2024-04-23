package top.e404.media.module.common.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

/**
 * 基础异常
 */
open class CustomMessageException(
    override val message: String,
    private val status: HttpStatus = HttpStatus.BAD_REQUEST
) : Throwable(message) {
    open val toResponseEntity get() = ResponseEntity.status(status).body(message)
}
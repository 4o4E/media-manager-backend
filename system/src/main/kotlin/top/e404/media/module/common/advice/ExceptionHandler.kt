package top.e404.media.module.common.advice

import org.slf4j.LoggerFactory
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestControllerAdvice
import top.e404.media.module.common.entity.BaseResp
import top.e404.media.module.common.exception.CommonFail
import top.e404.media.module.common.exception.HttpRequestException
import top.e404.media.module.common.exception.NoChangeException

/**
 * 全局异常处理器 用于处理所有未处理的异常
 */
@RestControllerAdvice
class ExceptionHandler {
    private val log = LoggerFactory.getLogger(this::class.java)

    private fun <T> badRequest(body: T): ResponseEntity<T> {
        log.debug("responseBody: {}", body)
        return ResponseEntity.badRequest().body(body)
    }

    /**
     * 最终的异常处理器，用于其他处理器未处理的异常，错误信息应该被打印
     *
     * 正常情况下不应有异常走此处理器, 若出现在日志中则需要完善处理逻辑或添加忽略
     *
     * @param e 异常
     * @return 返回给前端的响应
     */
    @ExceptionHandler(Exception::class)
    @ResponseBody
    protected fun exceptionHandler(e: Throwable): ResponseEntity<*> {
        if (e.cause is HttpRequestException) return exceptionHandler(e.cause as HttpRequestException)

        log.error("未处理的异常", e)
        return badRequest(e.message)
    }

    @ExceptionHandler(DataIntegrityViolationException::class)
    @ResponseBody
    protected fun exceptionHandler(
        e: DataIntegrityViolationException
    ) = badRequest("该数据已被引用, 无法删除")

    @ExceptionHandler(NoChangeException::class)
    @ResponseBody
    protected fun exceptionHandler(
        e: NoChangeException
    ) = ResponseEntity<Void>(HttpStatus.NOT_MODIFIED)

    @ExceptionHandler(HttpRequestException::class)
    @ResponseBody
    protected fun exceptionHandler(
        e: HttpRequestException
    ) = e.toResponseEntity().also {
        log.debug("responseBody: {}", it.body)
        log.warn(e.message, e)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseBody
    protected fun exceptionHandler(e: MethodArgumentNotValidException): ResponseEntity<String> {
        log.warn("参数异常:\n" + e.bindingResult.allErrors.joinToString("\n") { it.defaultMessage!! })
        return badRequest("参数异常")
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    @ResponseBody
    protected fun exceptionHandler(e: HttpMessageNotReadableException): ResponseEntity<BaseResp<*>> {
        log.warn("参数异常", e)
        var t: Throwable = e
        while (t.cause != null) t = t.cause!!
        return badRequest(BaseResp.fail(CommonFail.BAD_REQUEST.code, t.message!!))
    }

    @ExceptionHandler(NumberFormatException::class)
    @ResponseBody
    protected fun exceptionHandler(
        e: NumberFormatException
    ) = badRequest(BaseResp.fail(CommonFail.BAD_REQUEST.code, e.message!!))
}
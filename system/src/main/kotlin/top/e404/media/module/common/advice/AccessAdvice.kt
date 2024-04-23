package top.e404.media.module.common.advice

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.core.annotation.Order
import org.springframework.core.io.Resource
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import top.e404.media.module.common.util.TRACE_ID_KEY
import top.e404.media.module.common.util.log
import top.e404.media.module.common.util.toJsonString
import top.e404.media.module.common.util.traceId
import java.time.Duration
import java.time.Instant
import java.util.*

/**
 * 用于在请求中获取该用户的信息
 */
@Aspect
@Order(0) // 最先执行
@Component
class AccessAdvice {
    private val log = log()

    /**
     * 对所有controller方法进行计时
     */
    @Around("execution(* top.e404.media..controller..*Controller.*(..)) && @annotation(LogAccess)")
    private fun logAccess(joinPoint: ProceedingJoinPoint): Any? {
        traceId = UUID.randomUUID().toString().replace("-", "").also {
            log.debug("inject traceId: {}", it)
        }

        val attributes = RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes
        attributes.response!!.addHeader(TRACE_ID_KEY, traceId)
        val request = attributes.request
        val signature = joinPoint.signature.toLongString()
        log.debug("signature: {}", signature)
        log.debug("requestURL: {} {}", request.method, request.requestURL)
        log.debug("remoteAddr: {}", request.remoteAddr)
        log.debug("requestArgs: {}", request.parameterMap.toJsonString())
        log.debug(
            "requestBody: {}",
            if (request.contentType == MediaType.APPLICATION_JSON.toString()) {
                request.inputStream.bufferedReader().use { it.readText() }
            } else {
                "unsupported content type: ${request.contentType}"
            }
        )
        val start = Instant.now()
        try {
            val resp = joinPoint.proceed()
            if (resp !is Resource) {
                log.debug("responseBody: {}", resp.toJsonString())
            }
            return resp
        } finally {
            val duration = Duration.between(start, Instant.now()).toMillis()
            log.debug("耗时: {}ms", duration)
        }
    }
}
package top.e404.media.module.common.advice

import com.google.gson.Gson
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.annotation.Order
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import top.e404.media.module.common.util.log
import top.e404.media.module.common.util.traceId
import java.time.Duration
import java.time.Instant
import java.util.*

/**
 * 用于在请求中获取该用户的信息
 */
@Aspect
@Order(0)
@Component
class AccessAdvice {
    private val log = log()

    @set:Autowired
    lateinit var gson: Gson

    /**
     * 对所有controller方法进行计时
     */
    @Around("execution(* top.e404.media..controller..*Controller.*(..)) && @annotation(LogAccess)")
    private fun logAccess(joinPoint: ProceedingJoinPoint): Any? {
        traceId = UUID.randomUUID().toString().replace("-", "").also {
            log.debug("inject traceId: {}", it)
        }

        val attributes = RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes
        val request = attributes.request
        val signature = joinPoint.signature.toLongString()
        log.debug("signature: {}", signature)
        log.debug("requestURL: {}", request.requestURL)
        log.debug("remoteAddr: {}", request.remoteAddr)
        log.debug("requestArgs: {}", gson.toJson(request.parameterMap))
        log.debug(
            "requestBody: {}",
            if (request.contentType == MediaType.APPLICATION_JSON.toString()) {
                gson.toJson(request.inputStream.bufferedReader().use { it.readText() })
            } else {
                "unsupported content type: ${request.contentType}"
            }
        )
        val start = Instant.now()
        try {
            val resp = joinPoint.proceed()
            log.debug("responseBody: {}", gson.toJson(resp))
            return resp
        } finally {
            val duration = Duration.between(start, Instant.now()).toMillis()
            log.debug("耗时: {}ms", duration)
        }
    }
}
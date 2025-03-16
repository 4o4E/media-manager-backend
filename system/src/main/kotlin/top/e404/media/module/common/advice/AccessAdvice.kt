package top.e404.media.module.common.advice

import kotlinx.serialization.Serializable
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.core.annotation.Order
import org.springframework.core.io.Resource
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
import java.io.Serializable as JavaSerializable

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
        val signature = joinPoint.signature as MethodSignature
        log.debug("method: {}.{}", signature.method.declaringClass.name, signature.method.name)
        log.debug("url: {} {}", request.method, request.requestURL)
        log.debug("addr: {}", request.remoteAddr)
        if (log.isDebugEnabled) {
            log.debug(buildString {
                append("args: [")
                for ((index, parameter) in signature.method.parameters.withIndex()) {
                    if (index > 0) append(", ")
                    append(parameter.name)
                        .append(": ")
                        .append(joinPoint.args[index]?.let { if (it is Serializable || it is JavaSerializable) it.toJsonString() else it.javaClass.name }
                            ?: "null")
                }
                append("]")
            })
        }
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
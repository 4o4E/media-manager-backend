package top.e404.media.module.common.advice

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializerOrNull
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.slf4j.LoggerFactory
import org.springframework.core.annotation.Order
import org.springframework.core.io.Resource
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import top.e404.media.module.common.config.JsonConfig.Companion.json
import top.e404.media.module.common.util.TRACE_ID_KEY
import top.e404.media.module.common.util.clearTraceId
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
    private val log = LoggerFactory.getLogger("access")

    /**
     * 对所有controller方法进行计时
     */
    @OptIn(InternalSerializationApi::class)
    @Around("execution(* top.e404.media..controller..*Controller.*(..)) && @annotation(ann)")
    private fun logAccess(joinPoint: ProceedingJoinPoint, ann: LogAccess): Any? {
        traceId = UUID.randomUUID().toString().replace("-", "")

        val attributes = RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes
        attributes.response!!.addHeader(TRACE_ID_KEY, traceId)
        val request = attributes.request
        val signature = joinPoint.signature as MethodSignature
        log.debug("{}, {} {}", request.remoteAddr, request.method, request.requestURL)
        if (log.isDebugEnabled && ann.detail) {
            log.debug("method: {}.{}", signature.method.declaringClass.simpleName, signature.method.name)
            log.debug(buildString {
                append("args: [")
                for ((index, parameter) in signature.method.parameters.withIndex()) {
                    if (index > 0) append(", ")
                    append(parameter.name)
                        .append(": ")
                        .append(joinPoint.args[index]?.let {
                            (it as? JavaSerializable)?.toJsonString()
                                ?: it as? String
                                ?: (it as? Number)?.toString()
                                ?: it::class.serializerOrNull()?.let { serializer ->
                                    @Suppress("UNCHECKED_CAST")
                                    json.encodeToString(serializer as KSerializer<Any>, it)
                                }
                                ?: it.javaClass.name
                                ?: "null"
                        })
                }
                append("]")
            })
        }
        val start = Instant.now()
        try {
            val resp = joinPoint.proceed()
            if (resp !is Resource && ann.detail) {
                log.debug("responseBody: {}", resp.toJsonString())
            }
            return resp
        } finally {
            val duration = Duration.between(start, Instant.now()).toMillis()
            if (ann.detail) {
                log.debug("耗时: {}ms", duration)
            }
            clearTraceId()
        }
    }
}
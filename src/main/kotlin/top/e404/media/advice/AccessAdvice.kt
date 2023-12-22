package top.e404.media.advice

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import top.e404.media.util.log
import java.time.Duration
import java.time.Instant

/**
 * 用于在请求中获取该用户的信息
 */
@Aspect
@Component
class AccessAdvice {
    private val log = log()

    /**
     * 对所有controller方法进行计时
     */
    @Around("execution(* top.e404.media.controller.*Controller.*(..))")
    @Order(2)
    private fun logAccess(joinPoint: ProceedingJoinPoint): Any? {
        val start = Instant.now()
        try {
            return joinPoint.proceed()
        } finally {
            try {
                val duration = Duration.between(start, Instant.now()).toMillis()

                val attributes = RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes
                val request = attributes.request
                val signature = joinPoint.signature.toLongString()
                // 在log4j中配置对应的输出, 输出到指定日志目录
                log.debug("{} {} 耗时{}ms {}", request.remoteAddr, request.requestURL, duration, signature)
            } catch (e: Throwable) {
                log.warn("记录访问日志时出现异常", e)
            }
        }
    }
}
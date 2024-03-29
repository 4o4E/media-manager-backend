package top.e404.media.module.common.util

import org.slf4j.MDC

private const val traceIdKey = "traceId"
var traceId: String?
    get() = MDC.get(traceIdKey)
    set(value) {
        MDC.put(traceIdKey, value)
    }
package top.e404.media.module.common.util

import org.slf4j.MDC

const val TRACE_ID_KEY = "traceId"
var traceId: String?
    get() = MDC.get(TRACE_ID_KEY)
    set(value) {
        MDC.put(TRACE_ID_KEY, value)
    }
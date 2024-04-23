package top.e404.media.module.common.util

import com.fasterxml.jackson.databind.ObjectMapper

private val objectMapper = ObjectMapper()

fun Any?.toJsonString(): String = objectMapper.writeValueAsString(this)
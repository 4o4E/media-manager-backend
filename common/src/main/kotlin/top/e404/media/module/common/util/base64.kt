@file:Suppress("UNUSED")

package top.e404.media.module.common.util

import java.util.*

private val decoder = Base64.getDecoder()
private val encoder = Base64.getEncoder()

fun ByteArray.encodeToBase64(): String = encoder.encodeToString(this)
fun String.decodeAsBase64(): ByteArray = decoder.decode(this)
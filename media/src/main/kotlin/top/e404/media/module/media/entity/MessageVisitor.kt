@file:Suppress("UNUSED")

package top.e404.media.module.media.entity

import java.security.MessageDigest

/**
 * 遍历消息链对每个元素进行签名
 */
class MessageVisitor {
    private val digest: MessageDigest = MessageDigest.getInstance("sha-256")
    fun visit(bytes: ByteArray) = digest.update(bytes)

    fun visit(string: String) {
        digest.update(string.encodeToByteArray())
    }

    fun visit(num: Int) {
        digest.update(((num shr 8) and 0xff).toByte())
        digest.update((num and 0xff).toByte())
    }

    fun visit(num: Long) {
        digest.update(((num shr 24) and 0xff).toByte())
        digest.update(((num shr 16) and 0xff).toByte())
        digest.update(((num shr 8) and 0xff).toByte())
        digest.update((num and 0xff).toByte())
    }

    val result: String
        get() {
            @OptIn(ExperimentalStdlibApi::class)
            return digest.digest().toHexString()
        }
}
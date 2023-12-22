package top.e404.media.service

import org.springframework.stereotype.Service
import java.util.*

interface Base64Service {
    fun decode(base64: String): String
    fun encode(text: String): String
}

@Service
class Base64ServiceImpl : Base64Service {
    private val decoder = Base64.getDecoder()
    private val encoder = Base64.getEncoder()

    override fun decode(base64: String) = String(decoder.decode(base64))
    override fun encode(text: String): String = encoder.encodeToString(text.encodeToByteArray())
}
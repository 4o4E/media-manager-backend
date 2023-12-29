package top.e404.media.service.util

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import top.e404.media.util.decodeAsBase64
import java.security.Key
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher

interface RsaService {
    val publicKeyString: String

    /**
     * 用公钥加密
     */
    fun encryptByPublic(text: String): ByteArray

    /**
     * 用公钥解密
     */
    fun decryptByPublic(text: String): ByteArray

    /**
     * 用私钥加密
     */
    fun encryptByPrivate(text: String): ByteArray

    /**
     * 用私钥解密
     */
    fun decryptByPrivate(text: String): ByteArray
}

@Service
class RsaServiceImpl : RsaService {
    private val keyFactory: KeyFactory = KeyFactory.getInstance("RSA")

    @Value("\${application.key.public}")
    override lateinit var publicKeyString: String

    @Value("\${application.key.private}")
    lateinit var privateKeyString: String

    private val publicKey: PublicKey by lazy {
        keyFactory.generatePublic(X509EncodedKeySpec(publicKeyString.decodeAsBase64()))
    }

    private val privateKey: PrivateKey by lazy {
        keyFactory.generatePrivate(X509EncodedKeySpec(privateKeyString.decodeAsBase64()))
    }

    private fun initCipher(mode: Int, key: Key): Cipher = Cipher
        .getInstance("RSA")
        .apply { init(mode, key) }

    override fun encryptByPublic(text: String): ByteArray {
        return initCipher(Cipher.ENCRYPT_MODE, publicKey).doFinal(text.toByteArray())
    }

    override fun decryptByPublic(text: String): ByteArray {
        return initCipher(Cipher.DECRYPT_MODE, publicKey).doFinal(text.toByteArray())
    }

    override fun encryptByPrivate(text: String): ByteArray {
        return initCipher(Cipher.ENCRYPT_MODE, privateKey).doFinal(text.toByteArray())
    }

    override fun decryptByPrivate(text: String): ByteArray {
        return initCipher(Cipher.DECRYPT_MODE, privateKey).doFinal(text.toByteArray())
    }
}
package top.e404.media.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.core.io.FileSystemResource
import org.springframework.stereotype.Service
import java.io.File
import java.security.MessageDigest

interface FileService {
    fun getFileResourceBySha(sha: String): FileSystemResource?
    fun checkUpload(sha: String): Boolean
    fun upload(bytes: ByteArray): String
    fun exists(sha: String): Boolean
}

@Service
class FileServiceImpl : FileService {
    @Autowired
    @Qualifier("filesDir")
    lateinit var filesDir: File

    override fun getFileResourceBySha(sha: String): FileSystemResource? {
        val file = filesDir.resolve("${sha.substring(0, 2)}/${sha.substring(2, 4)}/${sha.substring(4)}")
        return if (file.exists()) FileSystemResource(file)
        else null
    }

    override fun checkUpload(sha: String): Boolean {
        return filesDir.resolve("${sha.substring(0, 2)}/${sha.substring(2, 4)}/${sha.substring(4)}").exists()
    }

    override fun upload(bytes: ByteArray): String {
        @OptIn(ExperimentalStdlibApi::class)
        val sha = MessageDigest.getInstance("sha-256").digest(bytes).toHexString()
        val file = filesDir.resolve("${sha.substring(0, 2)}/${sha.substring(2, 4)}/${sha.substring(4)}")
        if (file.exists()) return sha
        file.parentFile.mkdirs()
        file.writeBytes(bytes)
        return sha
    }

    override fun exists(sha: String) =
        filesDir.resolve("${sha.substring(0, 2)}/${sha.substring(2, 4)}/${sha.substring(4)}").exists()
}

package top.e404.media.module.media.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.core.io.FileSystemResource
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.security.MessageDigest

interface FileService {
    /**
     * 通过sha获取文件
     */
    fun getFileResourceBySha(sha: String): FileSystemResource?

    /**
     * 上传文件
     */
    fun upload(file: MultipartFile): String

    /**
     * 检查文件是否存在
     */
    fun exists(id: String): Boolean

    /**
     * 检查多个文件是否存在
     */
    fun allExists(ids: Iterable<String>): Boolean
}

@Service
class FileServiceImpl : FileService {
    @set:Autowired
    @Qualifier("filesDir")
    lateinit var filesDir: File

    override fun getFileResourceBySha(sha: String): FileSystemResource? {
        val file = filesDir.resolve("${sha.substring(0, 2)}/${sha.substring(2, 4)}/${sha.substring(4)}")
        return if (file.exists()) FileSystemResource(file)
        else null
    }

    override fun upload(file: MultipartFile): String {
        val bytes = file.bytes

        @OptIn(ExperimentalStdlibApi::class)
        val sha = MessageDigest.getInstance("sha-256").digest(bytes).toHexString()
        val localFile = filesDir.resolve("${sha.substring(0, 2)}/${sha.substring(2, 4)}/${sha.substring(4)}")
        if (localFile.exists()) return sha
        localFile.parentFile.mkdirs()
        localFile.writeBytes(bytes)
        return sha
    }

    override fun exists(id: String) =
        filesDir.resolve("${id.substring(0, 2)}/${id.substring(2, 4)}/${id.substring(4)}").exists()

    override fun allExists(ids: Iterable<String>) = ids.all(::exists)
}

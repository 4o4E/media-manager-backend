package top.e404.media.module.media.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.File

@Configuration
class FilesConfig {

    /**
     * 文件存储目录名
     */
    @Value("\${application.files.dir}")
    lateinit var dir: String

    /**
     * 文件存储目录
     */
    @get:Bean("filesDir")
    val filesDir by lazy { File(dir) }
}
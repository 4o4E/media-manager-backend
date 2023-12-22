package top.e404.media.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.File

@Configuration
class FilesConfig {
    @Value("\${application.files.dir}")
    lateinit var dir: String

    @get:Bean("filesDir")
    val filesDir by lazy { File(dir) }
}
package top.e404.media.module.media.config

import com.github.jershell.kbson.KBson
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class KBsonConfig {
    @get:Bean
    val kbson by lazy { KBson(configuration = com.github.jershell.kbson.Configuration(classDiscriminator = "type")) }

}
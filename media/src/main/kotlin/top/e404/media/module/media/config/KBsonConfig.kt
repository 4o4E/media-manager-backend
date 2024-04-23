package top.e404.media.module.media.config

import com.github.jershell.kbson.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class KBsonConfig {

    /**
     * KBson实例 用于bson序列化反序列化
     */
    @get:Bean
    val kbson by lazy { KBson(configuration = Configuration(classDiscriminator = "type")) }

}
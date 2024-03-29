package top.e404.media.module.common.config

import com.google.gson.Gson
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

@Component
class GsonProvider {
    @get:Bean
    val gson by lazy { Gson() }
}
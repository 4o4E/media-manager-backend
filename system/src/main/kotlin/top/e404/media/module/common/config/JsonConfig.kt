package top.e404.media.module.common.config

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.introspect.Annotated
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import kotlinx.serialization.json.Json
import org.springframework.context.annotation.Bean
import org.springframework.http.converter.json.KotlinSerializationJsonHttpMessageConverter
import org.springframework.stereotype.Component


@Component
class JsonConfig {
    companion object {
        val json = Json {
            ignoreUnknownKeys = true
            explicitNulls = false
        }
    }

    @get:Bean
    val json get() = Companion.json

    @get:Bean
    val ktxJson get() = KotlinSerializationJsonHttpMessageConverter(json)


    @Bean
    fun objectMapper(): ObjectMapper {
        val objectMapper = ObjectMapper()
        objectMapper.setAnnotationIntrospector(EmptyArrayIntrospector)
        // 让 null 的集合字段序列化为空数组 []
        val provider = DefaultSerializerProvider.Impl().apply {
            setNullValueSerializer(object : JsonSerializer<Any?>() {
                override fun serialize(value: Any?, gen: JsonGenerator, serializers: SerializerProvider) {
                    if (value == null) {
                        gen.writeStartArray()
                        gen.writeEndArray()
                    } else {
                        gen.writeObject(value)
                    }
                }
            })
        }

        objectMapper.setSerializerProvider(provider)
        return objectMapper
    }

    object EmptyArrayIntrospector : JacksonAnnotationIntrospector() {
        override fun readResolve(): Any = EmptyArrayIntrospector
        override fun findNullSerializer(a: Annotated): Any {
            if (List::class.java.isAssignableFrom(a.rawType)) {
                return ArrayNullSerializer
            }
            return super.findNullSerializer(a)
        }
    }

    object ArrayNullSerializer : StdSerializer<Any>(Any::class.java) {
        private fun readResolve(): Any = ArrayNullSerializer
        override fun serialize(value: Any?, gen: JsonGenerator, provider: SerializerProvider) {
            gen.writeStartArray()
            gen.writeEndArray()
        }
    }

}
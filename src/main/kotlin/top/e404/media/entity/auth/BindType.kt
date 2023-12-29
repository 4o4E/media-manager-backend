package top.e404.media.entity.auth

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import top.e404.media.util.primitive

/**
 * 用户绑定类型
 *
 * @property code 对应的绑定在数据库中的字段
 */
@Serializable(BindType.BindTypeSerializer::class)
enum class BindType(val code: Int) {
    EMAIL(1),
    PHONE(2);

    companion object {
        fun byCode(code: Int) = entries.first { it.code == code }
    }

    internal object BindTypeSerializer : KSerializer<BindType> {
        override val descriptor = primitive(PrimitiveKind.INT)
        override fun deserialize(decoder: Decoder) = BindType.byCode(decoder.decodeInt())
        override fun serialize(encoder: Encoder, value: BindType) = encoder.encodeInt(value.code)
    }
}
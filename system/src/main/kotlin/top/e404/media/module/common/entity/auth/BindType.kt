package top.e404.media.module.common.entity.auth

import com.baomidou.mybatisplus.annotation.EnumValue
import io.swagger.v3.oas.annotations.media.Schema
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import top.e404.media.module.common.util.primitive

/**
 * 用户绑定类型
 *
 * @property code 对应的绑定在数据库中的字段
 */

@Schema(description = "用户绑定类型")
@Serializable(BindType.BindTypeSerializer::class)
enum class BindType(@field:EnumValue val code: Int) {
    @Schema(description = "邮件")
    EMAIL(1),

    @Schema(description = "短信")
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
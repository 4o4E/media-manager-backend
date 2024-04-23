package top.e404.media.module.media.entity.comment

import io.swagger.v3.oas.annotations.media.Schema
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import top.e404.media.module.common.util.primitive

@Schema(description = "评论的数据类型")
@Serializable(CommentType.CommentTypeSerializer::class)
enum class CommentType(val code: Int) {
    @Schema(description = "Markdown")
    MARKDOWN(1),

    @Schema(description = "HTML")
    HTML(2);

    companion object {
        fun byCode(code: Int) = entries.first { it.code == code }
    }

    internal object CommentTypeSerializer : KSerializer<CommentType> {
        override val descriptor = primitive(PrimitiveKind.INT)
        override fun deserialize(decoder: Decoder) = byCode(decoder.decodeInt())
        override fun serialize(encoder: Encoder, value: CommentType) = encoder.encodeInt(value.code)
    }
}
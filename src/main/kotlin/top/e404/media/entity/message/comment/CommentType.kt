package top.e404.media.entity.message.comment

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import top.e404.media.util.primitive

@Serializable(CommentType.CommentTypeSerializer::class)
enum class CommentType(val code: Int) {
    MARKDOWN(1),
    HTML(2);

    companion object {
        fun byCode(code: Int) = entries.first { it.code == code }
    }

    internal object CommentTypeSerializer : KSerializer<CommentType> {
        override val descriptor = primitive(PrimitiveKind.INT)
        override fun deserialize(decoder: Decoder) = CommentType.byCode(decoder.decodeInt())
        override fun serialize(encoder: Encoder, value: CommentType) = encoder.encodeInt(value.code)
    }
}
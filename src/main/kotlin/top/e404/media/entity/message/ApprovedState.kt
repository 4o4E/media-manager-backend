package top.e404.media.entity.message

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import top.e404.media.util.primitive

@Serializable(ApprovedStateSerializer::class)
enum class ApprovedState(val code: Int) {
    /**
     * 等待审核
     */
    WAIT(1),

    /**
     * 通过审核
     */
    PASS(2),

    /**
     * 未通过审核
     */
    REJECT(3);
}

object ApprovedStateSerializer : KSerializer<ApprovedState> {
    override val descriptor = primitive(PrimitiveKind.INT)
    override fun deserialize(decoder: Decoder) =
        decoder.decodeInt().let { code -> ApprovedState.entries.first { it.code == code } }

    override fun serialize(encoder: Encoder, value: ApprovedState) = encoder.encodeInt(value.code)
}
package top.e404.media.module.media.entity

import io.swagger.v3.oas.annotations.media.Schema
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import top.e404.media.module.common.util.primitive

@Schema(description = "审核状态")
@Serializable(ApprovedState.ApprovedStateSerializer::class)
enum class ApprovedState(val code: Int) {
    @Schema(description = "等待审核")
    WAIT(1),

    @Schema(description = "通过审核")
    PASS(2),

    @Schema(description = "未通过审核")
    REJECT(3);

    companion object {
        fun byCode(code: Int) = entries.first { it.code == code }
    }

    internal object ApprovedStateSerializer : KSerializer<ApprovedState> {
        override val descriptor = primitive(PrimitiveKind.INT)
        override fun deserialize(decoder: Decoder) = byCode(decoder.decodeInt())
        override fun serialize(encoder: Encoder, value: ApprovedState) = encoder.encodeInt(value.code)
    }
}
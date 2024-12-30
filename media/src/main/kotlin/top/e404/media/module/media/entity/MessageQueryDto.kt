package top.e404.media.module.media.entity

import io.swagger.v3.oas.annotations.media.Schema
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.hibernate.validator.constraints.Range
import top.e404.media.module.common.util.primitive

@Schema(description = "消息查询")
@Serializable
data class MessageQueryDto(
    @Schema(description = "匹配模式")
    @Serializable(QueryMode.QueryModeSerializer::class)
    val queryMode: QueryMode = QueryMode.ALL,
    @Schema(description = "目标tag, 多个tag表示精准匹配", required = true)
    val tags: MutableSet<Long>,
    @Schema(description = "数量, 受账号等级限制")
    @Range(min = 1, max = 100)
    val count: Long = 1,
    @Schema(description = "指定消息类型")
    val type: MediaType? = null
)

@Serializable(QueryMode.QueryModeSerializer::class)
enum class QueryMode(val code: Int) {
    @Schema(description = "多个tab中任意一个匹配就算匹配")
    ANY(0),

    @Schema(description = "多个tab中全部匹配才算匹配")
    ALL(1);

    companion object {
        fun byCode(code: Int) = entries.first { it.code == code }
    }

    internal object QueryModeSerializer : KSerializer<QueryMode> {
        override val descriptor = primitive(PrimitiveKind.INT)
        override fun deserialize(decoder: Decoder) = byCode(decoder.decodeInt())
        override fun serialize(encoder: Encoder, value: QueryMode) = encoder.encodeInt(value.code)
    }
}


package top.e404.media.module.media.entity

import io.swagger.v3.oas.annotations.media.Schema
import kotlinx.serialization.Serializable
import top.e404.media.module.media.entity.data.Message

@Schema(description = "消息上传")
@Serializable
data class MessageDto(
    @Schema(description = "消息的具体数据")
    val chain: MutableList<Message>,
    @Schema(description = "该消息的tag")
    val tags: MutableSet<Long>
)

@Schema(description = "消息更新")
@Serializable
data class MessageUpdateDto(
    val id: String,
    @Schema(description = "消息的具体数据")
    val chain: MutableList<Message>,
    @Schema(description = "该消息的tag")
    val tags: MutableSet<Long>
)

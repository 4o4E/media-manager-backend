package top.e404.media.entity.message

import io.swagger.v3.oas.annotations.media.Schema
import kotlinx.serialization.Serializable

@Schema(description = "消息上传")
@Serializable
data class MessageDto(
    @Schema(description = "消息的具体诗句")
    val chain: List<Message>,
    @Schema(description = "该消息的tag")
    val tags: MutableSet<String>
)

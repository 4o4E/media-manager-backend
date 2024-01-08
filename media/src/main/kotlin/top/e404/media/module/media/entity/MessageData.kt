package top.e404.media.module.media.entity

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotEmpty
import kotlinx.serialization.Serializable
import top.e404.media.module.media.entity.comment.MessageCommentList
import top.e404.media.module.media.entity.data.Message
import top.e404.media.module.media.entity.info.MessageInfo

@Serializable
@Schema(description = "完整的一条消息")
data class MessageData(
    @Schema(description = "消息信息")
    val info: MessageInfo,
    @NotEmpty
    @Schema(description = "消息内容")
    val data: List<Message>,
    @Schema(description = "消息评论")
    val comment: MessageCommentList
)


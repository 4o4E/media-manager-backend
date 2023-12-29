package top.e404.media.entity.message.comment

import io.swagger.v3.oas.annotations.media.Schema
import kotlinx.serialization.Serializable

@Serializable
@Schema(description = "消息的评论区")
data class MessageCommentList(
    @Schema(description = "所有评论")
    val list: List<MessageComment> = emptyList(),
    @Schema(description = "评论条数")
    val current: Int = 0
)

@Serializable
@Schema(description = "消息的评论")
data class MessageCommentDto(
    @Schema(description = "发送者id")
    val sender: Long,
    @Schema(description = "评论格式")
    val type: CommentType,
    @Schema(description = "评论内容")
    val content: String
)

@Serializable
@Schema(description = "消息的评论")
data class MessageComment(
    @Schema(description = "消息序号(几楼)")
    val index: Long,
    @Schema(description = "发送者id")
    val sender: Long,
    @Schema(description = "评论格式")
    val type: CommentType,
    @Schema(description = "评论内容")
    val content: String,
    @Schema(description = "评论时间")
    val time: Long,
)


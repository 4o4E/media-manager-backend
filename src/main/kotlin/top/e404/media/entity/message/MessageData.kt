package top.e404.media.entity.message

import jakarta.validation.constraints.NotEmpty
import kotlinx.serialization.Serializable
import top.e404.media.entity.message.comment.MessageCommentList
import top.e404.media.entity.message.data.Message
import top.e404.media.entity.message.info.MessageInfo

/**
 * 完整的一条消息
 *
 * @property info 消息元数据
 * @property data 消息组成
 */
@Serializable
data class MessageData(
    val info: MessageInfo,
    @NotEmpty
    val data: List<Message>,
    val comment: MessageCommentList
)


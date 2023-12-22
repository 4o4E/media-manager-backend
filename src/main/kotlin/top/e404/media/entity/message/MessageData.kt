package top.e404.media.entity.message

import jakarta.validation.constraints.NotEmpty
import kotlinx.serialization.Serializable

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
)


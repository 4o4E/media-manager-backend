package top.e404.media.module.media.entity.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import top.e404.media.module.media.entity.MessageVisitor

/**
 * 发言消息, 用于组成聊天记录
 *
 * @property senderName 发送者名字
 * @property senderFace 发送者头像url
 * @property time 发送时间戳
 * @property message 发送的内容
 */
@Serializable
@SerialName(SpeakMessage.IDENTIFY)
data class SpeakMessage(
    val senderName: String,
    val senderFace: String,
    val time: Long,
    val message: Message,
) : Message {
    companion object {
        const val IDENTIFY = "speak"
    }

    @Transient
    val type = IDENTIFY

    override fun sign(visitor: MessageVisitor) {
        visitor.visit(senderName)
        visitor.visit(senderFace)
        visitor.visit(time)
    }
}
package top.e404.media.module.media.entity.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import top.e404.media.module.media.entity.MessageVisitor

/**
 * 聊天记录: 多个发言的集合
 */
@Serializable
@SerialName(DiscussMessage.IDENTIFY)
data class DiscussMessage(val content: List<SpeakMessage>) : Message {
    companion object {
        const val IDENTIFY = "discuss"
    }

    override fun sign(visitor: MessageVisitor) {
        for (message in content) message.sign(visitor)
    }
}
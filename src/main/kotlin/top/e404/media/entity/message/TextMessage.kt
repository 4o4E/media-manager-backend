package top.e404.media.entity.message

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 文本消息
 *
 * @property content 文本
 */
@Serializable
@SerialName(TextMessage.IDENTIFY)
data class TextMessage(var content: String) : Message {
    companion object {
        const val IDENTIFY = "text"
    }

    override fun sign(visitor: MessageVisitor) = visitor.visit(content)
}
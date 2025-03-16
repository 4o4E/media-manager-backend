package top.e404.media.module.media.entity.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import top.e404.media.module.media.entity.MediaElementVisitor

/**
 * 文本消息
 *
 * @property content 文本
 */
@Serializable
@SerialName(TextElement.IDENTIFY)
data class TextElement(var content: String) : MediaElement {
    companion object {
        const val IDENTIFY = "text"
    }

    @Transient
    val type = IDENTIFY

    override fun sign(visitor: MediaElementVisitor) = visitor.visit(content)
}
package top.e404.media.module.media.entity.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import top.e404.media.module.media.entity.MediaElementVisitor

/**
 * 聊天记录: 多个发言的集合
 */
@Serializable
@SerialName(DiscussElement.IDENTIFY)
data class DiscussElement(val content: List<SpeakElement>) : MediaElement {
    companion object {
        const val IDENTIFY = "discuss"
    }

    @Transient
    val type = IDENTIFY

    override fun sign(visitor: MediaElementVisitor) {
        for (message in content) message.sign(visitor)
    }
}
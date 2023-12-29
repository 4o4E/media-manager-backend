package top.e404.media.entity.message.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import top.e404.media.entity.message.MessageVisitor

/**
 * 聊天记录: 多个发言的集合
 */
@Serializable
@SerialName(DiscussMessage.IDENTIFY)
class DiscussMessage : ArrayList<SpeakMessage>, Message {
    companion object {
        const val IDENTIFY = "discuss"
    }

    @Suppress("UNUSED")
    constructor(initialCapacity: Int) : super(initialCapacity)

    @Suppress("UNUSED")
    constructor() : super()

    @Suppress("UNUSED")
    constructor(c: MutableCollection<out SpeakMessage>) : super(c)

    @Suppress("UNUSED")
    constructor(vararg c: SpeakMessage) : super(c.toMutableList())

    override fun sign(visitor: MessageVisitor) {
        for (message in this) message.sign(visitor)
    }
}
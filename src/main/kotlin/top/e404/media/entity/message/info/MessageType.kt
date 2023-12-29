package top.e404.media.entity.message.info

import kotlinx.serialization.Serializable
import top.e404.media.entity.message.data.*

/**
 * 消息类型, 用于在`MessageChain`中仅有一条消息时确定消息类型
 */
@Serializable
enum class MessageType(vararg val format: String) {
    /**
     * 纯文本
     */
    TEXT,

    /**
     * 单张图片
     */
    IMAGE("png", "jpg", "jpeg", "bmp", "gif"),

    /**
     * 单个视频
     */
    VIDEO("mp4", "mkv", "flv", "avi", "webm"),

    /**
     * 单个音频
     */
    AUDIO("mp3", "flac", "ogg", "m4a"),

    /**
     * 单个文件
     */
    FILE,

    /**
     * 发言
     */
    SPEAK,

    /**
     * 聊天记录
     */
    DISCUSS,

    /**
     * 复合类型
     */
    COMPOSITE;

    companion object {
        fun byMessage(chain: Collection<Message>): MessageType {
            require(chain.isNotEmpty())
            if (chain.size != 1) return COMPOSITE
            return when (val m = chain.first()) {
                is TextMessage -> TEXT
                is BinaryMessage -> entries.firstOrNull { it.format.contains(m.format) } ?: FILE
                is DiscussMessage -> DISCUSS
                is SpeakMessage -> SPEAK
            }
        }
    }
}
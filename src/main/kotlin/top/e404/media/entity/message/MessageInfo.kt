package top.e404.media.entity.message

import kotlinx.serialization.Serializable
import top.e404.media.entity.message.meta.Meta
import top.e404.media.serializator.UuidSerializer
import java.util.*

/**
 * 消息信息
 *
 * @property sha 消息的sha, 作为唯一id使用, 验证消息唯一, 计算来源的内容仅包含消息的内容
 * @property time 上传时间
 * @property type 消息的类型
 * @property tags 消息的tag
 * @property approved 是否审核通过
 * @property metas 消息的元数据
 */
@Serializable
data class MessageInfo(
    val sha: String,
    @Serializable(UuidSerializer::class)
    val upload: UUID,
    val time: Long,
    val type: MessageType,
    val approved: Boolean,
    val tags: MutableSet<String>,
    val metas: MutableList<out Meta>
)

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
     * 单个文件
     */
    SPEAK,

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
                is DiscussMessage -> COMPOSITE
                is SpeakMessage -> SPEAK
            }
        }
    }
}
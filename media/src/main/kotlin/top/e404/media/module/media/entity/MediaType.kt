package top.e404.media.module.media.entity

import com.baomidou.mybatisplus.annotation.EnumValue
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import top.e404.media.module.media.entity.data.*

/**
 * 消息类型, 用于在`MessageChain`中仅有一条消息时确定消息类型
 */
@Serializable
enum class MediaType(@field:EnumValue val code: Int, vararg val format: String) {
    /**
     * 纯文本
     */
    @SerialName("text")
    TEXT(1),

    /**
     * 单张图片
     */
    @SerialName("image")
    IMAGE(2, "png", "jpg", "jpeg", "bmp", "gif"),

    /**
     * 单个视频
     */
    @SerialName("video")
    VIDEO(3, "mp4", "mkv", "flv", "avi", "webm"),

    /**
     * 单个音频
     */
    @SerialName("audio")
    AUDIO(4, "mp3", "flac", "ogg", "m4a"),

    /**
     * 单个文件
     */
    @SerialName("file")
    FILE(5),

    /**
     * 发言
     */
    @SerialName("speak")
    SPEAK(6),

    /**
     * 聊天记录
     */
    @SerialName("discuss")
    DISCUSS(7),

    /**
     * 复合类型
     */
    @SerialName("composite")
    COMPOSITE(8);

    companion object {
        fun byMessage(chain: Collection<MediaElement>): MediaType {
            require(chain.isNotEmpty()) { "最少包含一条消息" }
            if (chain.size != 1) return COMPOSITE
            return when (chain.first()) {
                is TextElement -> TEXT
                is BinaryElement -> FILE
                is DiscussElement -> DISCUSS
                is SpeakElement -> SPEAK
                is ImageElement -> IMAGE
                is VideoElement -> VIDEO
                is AudioElement -> AUDIO
            }
        }
    }
}
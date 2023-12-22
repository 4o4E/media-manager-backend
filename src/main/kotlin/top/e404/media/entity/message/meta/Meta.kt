package top.e404.media.entity.message.meta

import kotlinx.serialization.Polymorphic
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 元数据
 */
@Serializable
@Polymorphic
sealed interface Meta {
    companion object {
        fun uploader(platform: UploaderMeta.PlatformType, uploader: String) = UploaderMeta(platform, uploader)
    }
}

/**
 * 标记上传者信息的meta
 *
 * @property platform 上传平台
 * @property uploader 上传者唯一id
 */
@Serializable
@SerialName(UploaderMeta.IDENTIFY)
data class UploaderMeta(val platform: PlatformType, val uploader: String) : Meta {
    companion object {
        const val IDENTIFY = "uploader"
    }

    enum class PlatformType {
        QQ,
        WEB,
    }
}
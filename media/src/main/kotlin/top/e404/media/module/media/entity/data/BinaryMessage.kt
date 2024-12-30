package top.e404.media.module.media.entity.data

import jakarta.validation.constraints.Pattern
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import top.e404.media.module.media.entity.MediaElementVisitor

/**
 * 包含了二进制数据的基础消息, 包括图片, 视频, 音频
 *
 * 上传时先通过文件接口获取path
 *
 * @property id 二进制数据的文件路径, 包含了后缀名
 * @property format 文件的后缀名, 二进制文件在存储时不区分类型, 仅在`BinaryMessage`中标记类型
 * @property file 该消息是否上传为文件, 若false则意为是专门类型的消息, 如图片和图片文件的区别
 */
@Serializable
@SerialName(BinaryMessage.IDENTIFY)
data class BinaryMessage(
    @Pattern(regexp = "[a-f0-9]{64}")
    val id: String,
    val format: String,
    val file: Boolean,
) : Message {
    companion object {
        const val IDENTIFY = "binary"
    }

    @Transient
    val type = IDENTIFY

    override fun sign(visitor: MediaElementVisitor) = visitor.visit(id)
}
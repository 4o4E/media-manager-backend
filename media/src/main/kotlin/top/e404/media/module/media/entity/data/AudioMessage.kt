package top.e404.media.module.media.entity.data

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.Pattern
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import top.e404.media.module.media.entity.MessageVisitor

/**
 * 音频消息
 *
 * @property id 二进制数据的文件路径, 包含了后缀名
 * @property format 文件的后缀名, 二进制文件在存储时不区分类型, 在对应的message中标记类型
 * @property file 该消息是否上传为文件, 若false则意为是专门类型的消息, 如图片和图片文件的区别
 * @property length 音频时长
 */
@Serializable
@SerialName(AudioMessage.IDENTIFY)
data class AudioMessage(
    @Pattern(regexp = "[a-f0-9]{64}")
    val id: String,
    val format: String,
    val file: Boolean,
    @Min(1)
    val length: Long,
) : Message {
    companion object {
        const val IDENTIFY = "audio"
    }

    @Transient
    val type = IDENTIFY

    override fun sign(visitor: MessageVisitor) = visitor.visit(id)
}
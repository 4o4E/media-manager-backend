package top.e404.media.module.media.entity.data

import kotlinx.serialization.Serializable
import top.e404.media.module.media.entity.MediaElementVisitor

/**
 * 消息接口, 用于多态序列化
 */
@Serializable
sealed interface Message {
    /**
     * 用于遍历所有内部成员, 获取完整的散列值作为签名
     *
     * @param visitor 对每个内部成员进行迭代
     */
    fun sign(visitor: MediaElementVisitor)
}
package top.e404.media.entity.message.info

import kotlinx.serialization.Serializable
import top.e404.media.entity.message.meta.Meta

/**
 * 消息信息
 *
 * @property id 消息的sha, 作为唯一id使用, 验证消息唯一, 计算来源的内容仅包含消息的内容
 * @property upload 上传用户
 * @property time 上传时间
 * @property type 消息的类型
 * @property tags 消息的tag
 * @property approved 审核状态
 * @property metas 消息的元数据
 */
@Serializable
data class MessageInfo(
    val id: String,
    val upload: Long,
    val time: Long,
    val type: MessageType,
    val approved: ApprovedState,
    val tags: MutableSet<String>,
    val metas: MutableList<out Meta>
)
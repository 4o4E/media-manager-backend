package top.e404.media.module.media.entity.info

import io.swagger.v3.oas.annotations.media.Schema
import kotlinx.serialization.Serializable
import top.e404.media.module.media.entity.meta.Meta

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
@Schema(description = "消息信息")
@Serializable
data class MessageInfo(
    @Schema(description = "消息唯一id")
    val id: String,
    @Schema(description = "上传者")
    val upload: Long,
    @Schema(description = "上传时间")
    val time: Long,
    @Schema(description = "消息类型")
    val type: MessageType,
    @Schema(description = "审核状态")
    val approved: ApprovedState,
    @Schema(description = "tag")
    val tags: MutableSet<String>,
    @Schema(description = "元数据")
    val metas: MutableList<out Meta>
)
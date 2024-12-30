package top.e404.media.module.media.entity

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotEmpty
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import top.e404.media.module.media.entity.data.Message

@Serializable
@Schema(description = "完整的一条消息")
data class MessageData(
    @Schema(description = "消息唯一id")
    @SerialName("_id")
    val id: String,
    @Schema(description = "消息签名")
    val sha: String,
    @Schema(description = "上传者")
    val upload: Long,
    @Schema(description = "上传时间")
    val created: Long,
    @Schema(description = "最近更新时间")
    val updated: Long,
    @Schema(description = "消息类型")
    val type: MediaType,
    @Schema(description = "审核状态")
    val approved: ApprovedState,
    @NotEmpty
    @Schema(description = "tag")
    val tags: MutableSet<Long>,
    @NotEmpty
    @Schema(description = "消息内容")
    val content: List<Message>,
)

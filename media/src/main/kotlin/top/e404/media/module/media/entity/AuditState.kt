package top.e404.media.module.media.entity

import io.swagger.v3.oas.annotations.media.Schema
import kotlinx.serialization.Serializable

@Schema(description = "审核状态")
@Serializable
enum class AuditState {
    @Schema(description = "等待审核")
    WAIT,

    @Schema(description = "通过审核")
    PASS,

    @Schema(description = "未通过审核")
    REJECT;
}
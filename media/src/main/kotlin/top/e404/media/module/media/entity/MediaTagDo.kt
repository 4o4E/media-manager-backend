package top.e404.media.module.media.entity

import com.baomidou.mybatisplus.annotation.*
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Null
import kotlinx.serialization.Serializable
import top.e404.media.module.common.entity.SaveValid
import top.e404.media.module.common.entity.UpdateValid
import top.e404.media.module.common.entity.typeHandler.TextListTypeHandler


@Serializable
@TableName("media_tag", autoResultMap = true)
data class MediaTagDo(
    @field:TableId(type = IdType.ASSIGN_ID)
    var id: Long? = null,
    @field:TableField(typeHandler = TextListTypeHandler::class)
    var names: List<String>? = null,
    var remark: String? = null,

    @field:Version
    @field:TableField(fill = FieldFill.INSERT)
    var version: Long? = null,
    @field:TableField(fill = FieldFill.INSERT)
    var createBy: Long? = null,
    @field:TableField(fill = FieldFill.INSERT)
    var createTime: Long? = null,
    @field:TableField(fill = FieldFill.INSERT_UPDATE)
    var updateBy: Long? = null,
    @field:TableField(fill = FieldFill.INSERT_UPDATE)
    var updateTime: Long? = null,
)

@Serializable
@Schema(description = "媒体标签")
data class MediaTagDto(
    @Schema(description = "字典id")
    @Null(groups = [SaveValid::class])
    @NotNull(groups = [UpdateValid::class])
    var id: Long? = null,
    @Schema(description = "标签别名")
    @NotNull(groups = [SaveValid::class])
    var names: List<String>? = null,
    @Schema(description = "标签备注")
    @NotNull(groups = [SaveValid::class])
    var remark: String? = null,
)

@Serializable
@Schema(description = "媒体标签")
data class MediaTagVo(
    @Schema(description = "字典id")
    var id: Long? = null,
    @Schema(description = "标签别名")
    var names: List<String>? = null,
    @Schema(description = "标签备注")
    var remark: String? = null,
)

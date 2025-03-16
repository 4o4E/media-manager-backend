package top.e404.media.module.common.entity.database

import com.baomidou.mybatisplus.annotation.*
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Null
import kotlinx.serialization.Serializable
import top.e404.media.module.common.entity.SaveValid
import top.e404.media.module.common.entity.UpdateValid

@Serializable
@TableName("sys_dict")
data class DictDo(
    @field:TableId(type = IdType.ASSIGN_ID)
    var id: Long? = null,
    var type: String? = null,
    var value: String? = null,
    var label: String? = null,
    var enabled: Boolean? = null,

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
@Schema(description = "字典条目")
data class DictDto(
    @Schema(description = "字典id")
    @Null(groups = [SaveValid::class])
    @NotNull(groups = [UpdateValid::class])
    var id: Long? = null,
    @Schema(description = "字典类型")
    @NotNull(groups = [SaveValid::class])
    var type: String? = null,
    @Schema(description = "字典编码")
    @NotNull(groups = [SaveValid::class])
    var value: String? = null,
    @Schema(description = "字典文本")
    @NotNull(groups = [SaveValid::class])
    var label: String? = null,
    @Schema(description = "是否启用")
    @NotNull(groups = [SaveValid::class])
    var enabled: Boolean? = null,
)

@Serializable
@Schema(description = "字典条目")
data class DictVo(
    @Schema(description = "字典id")
    var id: Long? = null,
    @Schema(description = "字典类型")
    var type: String? = null,
    @Schema(description = "字典编码")
    var value: String? = null,
    @Schema(description = "字典文本")
    var label: String? = null,
    @Schema(description = "是否启用")
    var enabled: Boolean? = null,
)

@Serializable
@Schema(description = "字典集合")
data class DictCollection(
    @Schema(description = "字典类型")
    var type: String,
    var values: String?,
    @Schema(description = "字典数据")
    var list: List<DictVo>,
) {
    @Suppress("UNUSED")
    constructor(type: String, value: String?) : this(type, value, emptyList())
}

@Serializable
@Schema(description = "字典查询条件")
data class DictQuery(
    @Schema(description = "字典类型, 模糊查询")
    var type: String? = null,
    @Schema(description = "字典标签, 模糊查询")
    var label: String? = null,
    @Schema(description = "字典编码, 模糊查询")
    var value: String? = null,
)
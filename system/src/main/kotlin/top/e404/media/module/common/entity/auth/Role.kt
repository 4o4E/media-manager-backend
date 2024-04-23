package top.e404.media.module.common.entity.auth

import com.baomidou.mybatisplus.annotation.*
import io.swagger.v3.oas.annotations.media.Schema
import kotlinx.serialization.Serializable

@Serializable
@TableName("sys_role")
data class RoleDo(
    @field:TableId(type = IdType.AUTO)
    var id: Long? = null,
    var name: String? = null,
    var description: String? = null,

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
@Schema(description = "用户角色")
data class RoleDto(
    @Schema(description = "角色id")
    var id: Long? = null,
    @Schema(description = "角色名字")
    var name: String? = null,
    @Schema(description = "角色备注")
    var description: String? = null,
)

@Serializable
@Schema(description = "用户角色")
data class RoleVo(
    @Schema(description = "角色id")
    var id: Long? = null,
    @Schema(description = "角色名字")
    var name: String? = null,
    @Schema(description = "角色备注")
    var description: String? = null,
)
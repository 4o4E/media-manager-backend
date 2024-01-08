package top.e404.media.module.common.entity.auth

import com.baomidou.mybatisplus.annotation.FieldFill
import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableName
import com.baomidou.mybatisplus.annotation.Version
import io.swagger.v3.oas.annotations.media.Schema
import kotlinx.serialization.Serializable

@Serializable
@TableName("sys_user_role")
data class UserRoleDo(
    var userId: Long? = null,
    var roleId: Long? = null,

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
data class UserRoleDto(
    @Schema(description = "用户id")
    val userId: Long,
    @Schema(description = "角色id")
    val roleId: Long,
)

@Serializable
@Schema(description = "用户绑定")
data class UserRoleVo(
    @Schema(description = "用户id")
    val userId: Long,
    @Schema(description = "角色id")
    val roleId: Long,
)
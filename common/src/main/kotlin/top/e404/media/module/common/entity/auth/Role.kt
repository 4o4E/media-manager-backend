package top.e404.media.module.common.entity.auth

import com.baomidou.mybatisplus.annotation.FieldFill
import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableName
import com.baomidou.mybatisplus.annotation.Version
import kotlinx.serialization.Serializable

@Serializable
@TableName("sys_role")
data class RoleDo(
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
data class RoleDto(
    var id: Long? = null,
    var name: String? = null,
    var description: String? = null,
)

@Serializable
data class RoleVo(
    var id: Long? = null,
    var name: String? = null,
    var description: String? = null,
)
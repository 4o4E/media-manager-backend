package top.e404.media.module.common.entity.auth

import com.baomidou.mybatisplus.annotation.*
import io.swagger.v3.oas.annotations.media.Schema
import kotlinx.serialization.Serializable

@Serializable
@Schema(description = "用户绑定")
data class UserBindDto(
    @Schema(description = "绑定记录id")
    var id: Long? = null,
    @Schema(description = "用户id")
    var userId: Long? = null,
    @Schema(description = "绑定类型")
    var type: BindType? = null,
    @Schema(description = "绑定具体数值 邮箱/手机号")
    var value: String? = null,
)

@Serializable
data class UserBindVo(
    @Schema(description = "绑定记录id")
    var id: Long? = null,
    @Schema(description = "用户id")
    var userId: Long? = null,
    @Schema(description = "绑定类型")
    var type: BindType? = null,
    @Schema(description = "绑定具体数值 邮箱/手机号")
    var value: String? = null,
)

@Serializable
@TableName("sys_user_bind")
data class UserBindDo(
    @field:TableId(type = IdType.AUTO)
    var id: Long? = null,
    var userId: Long? = null,
    var type: BindType? = null,
    var value: String? = null,

    @field:TableLogic
    @field:TableField(fill = FieldFill.INSERT)
    var deleted: Boolean? = null,
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

fun UserBindDo.toVo() = UserBindVo(this.id, this.userId, this.type, this.value)
fun UserBindDto.toDo() = UserBindDo(this.id, this.userId, this.type, this.value)
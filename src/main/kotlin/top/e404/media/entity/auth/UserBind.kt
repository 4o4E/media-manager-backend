package top.e404.media.entity.auth

import com.baomidou.mybatisplus.annotation.*
import kotlinx.serialization.Serializable

@Serializable
data class UserBindDto(
    var id: Long? = null,
    var userId: Long? = null,
    var type: BindType? = null,
    var value: String? = null,
)

@Serializable
data class UserBindVo(
    var id: Long? = null,
    var userId: Long? = null,
    var type: BindType? = null,
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
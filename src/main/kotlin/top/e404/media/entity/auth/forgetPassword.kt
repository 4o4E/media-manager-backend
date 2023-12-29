package top.e404.media.entity.auth

import com.baomidou.mybatisplus.annotation.*
import jakarta.validation.constraints.NotNull
import kotlinx.serialization.Serializable

@Serializable
data class ForgetPasswordDto(
    @NotNull
    val type: BindType,
    @NotNull
    val value: String,
)

@Serializable
data class ResetPasswordDto(
    @NotNull
    val token: String,
    @NotNull
    val password: String,
)

@Serializable
data class SetPasswordDto(
    @NotNull
    val old: String,
    @NotNull
    val new: String,
)

@Serializable
@TableName("sys_user_forget_password")
data class ForgetPasswordDo(
    @field:TableId(type = IdType.AUTO)
    var id: Long? = null,
    var userId: Long? = null,
    var token: String? = null,
    var valid: Boolean? = null,

    @field:Version
    @field:TableField(fill = FieldFill.INSERT)
    var version: Long? = null,
    @field:TableField(fill = FieldFill.INSERT)
    var createTime: Long? = null,
    @field:TableField(fill = FieldFill.INSERT_UPDATE)
    var updateTime: Long? = null,
)
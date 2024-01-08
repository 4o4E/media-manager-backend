package top.e404.media.module.common.entity.auth

import com.baomidou.mybatisplus.annotation.*
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull
import kotlinx.serialization.Serializable

@Serializable
@Schema(description = "用户忘记密码后申请重置")
data class ForgetPasswordDto(
    @Schema(description = "重置类型")
    @NotNull
    val type: BindType,
    @Schema(description = "重置数值 邮箱或者手机号")
    @NotNull
    val value: String,
)

@Serializable
@Schema(description = "通过邮箱/手机短信的token重置")
data class ResetPasswordDto(
    @NotNull
    @Schema(description = "重置token")
    val token: String,
    @NotNull
    @Schema(description = "新的密码")
    val password: String,
)

@Serializable
@Schema(description = "用户更新密码")
data class SetPasswordDto(
    @NotNull
    @Schema(description = "用户旧密码")
    val old: String,
    @NotNull
    @Schema(description = "用户新密码")
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
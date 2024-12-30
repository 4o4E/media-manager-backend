package top.e404.media.module.common.entity.database

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull
import kotlinx.serialization.Serializable

@Serializable
@Schema(description = "用户注册")
data class RegisterDto(
    @NotNull
    @Schema(description = "注册绑定类型, 0邮箱, 1手机")
    val type: BindType,
    @NotNull
    @Schema(description = "注册绑定具体数值 邮箱/手机号")
    val value: String,
    @NotNull
    @Schema(description = "用户名 可重复")
    val username: String,
    @NotNull
    @Schema(description = "用户密码 使用公钥rsa加密")
    val password: String,
)
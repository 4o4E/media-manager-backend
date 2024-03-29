package top.e404.media.module.common.entity.auth

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull
import kotlinx.serialization.Serializable

@Serializable
@Schema(description = "用户登录")
data class LoginDto(
    @NotNull
    @Schema(description = "用户名")
    val username: String,
    @NotNull
    @Schema(description = "用户密码, 使用rsa公钥加密的")
    val password: String
)

@Serializable
@Schema(description = "用户基本信息")
data class LoginVo(
    @Schema(description = "用户id")
    val userId: Long,
    @Schema(description = "用户api调用token")
    val token: String,
    @Schema(description = "用户token过期时间戳")
    val expire: Long,
    @Schema(description = "用户拥有的权限")
    val perms: Set<String>,
)
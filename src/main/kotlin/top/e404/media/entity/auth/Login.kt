package top.e404.media.entity.auth

import jakarta.validation.constraints.NotNull
import kotlinx.serialization.Serializable

@Serializable
data class LoginDto(
    @NotNull
    val username: String,
    @NotNull
    val password: String
)

@Serializable
data class LoginVo(
    val userId: Long,
    val token: String,
    val expire: Long,
    val perms: Set<String>,
)
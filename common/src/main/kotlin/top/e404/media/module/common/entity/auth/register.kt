package top.e404.media.module.common.entity.auth

import jakarta.validation.constraints.NotNull
import kotlinx.serialization.Serializable

@Serializable
data class RegisterDto(
    @NotNull
    val type: BindType,
    @NotNull
    val value: String,
    @NotNull
    val username: String,
    @NotNull
    val password: String,
)
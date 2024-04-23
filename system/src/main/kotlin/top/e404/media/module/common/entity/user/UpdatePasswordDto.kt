package top.e404.media.module.common.entity.user

import kotlinx.serialization.Serializable

@Serializable
data class UpdatePasswordDto(
    val id: Long,
    val password: String
)

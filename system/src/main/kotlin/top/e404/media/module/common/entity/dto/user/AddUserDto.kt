package top.e404.media.module.common.entity.dto.user

import kotlinx.serialization.Serializable

@Serializable
data class AddUserDto(
    val name: String,
    val password: String,
    val point: Int,
)

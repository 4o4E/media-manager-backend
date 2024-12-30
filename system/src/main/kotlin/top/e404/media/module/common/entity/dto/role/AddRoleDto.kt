package top.e404.media.module.common.entity.dto.role

import kotlinx.serialization.Serializable

@Serializable
data class AddRoleDto(
    val name: String,
    val description: String,
)

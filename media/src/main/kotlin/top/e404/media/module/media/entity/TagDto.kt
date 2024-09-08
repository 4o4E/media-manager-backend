package top.e404.media.module.media.entity

import kotlinx.serialization.Serializable

@Serializable
data class TagDto(
    val id: Long,
    val name: String,
    val description: String,
    val alias: List<String>
)
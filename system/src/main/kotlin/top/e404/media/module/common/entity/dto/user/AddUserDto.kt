package top.e404.media.module.common.entity.dto.user

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import kotlinx.serialization.Serializable

@Serializable
data class AddUserDto(
    @NotNull(message = "不可为空")
    @Size(min = 3, max = 16, message = "用户名必须是3-16个字符长度")
    val name: String,
    @NotNull(message = "不可为空")
    @Size(min = 6, max = 32, message = "密码长度必须是6-32个字符")
    val password: String,
    val point: Int,
)

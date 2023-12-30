package top.e404.media.module.common.entity.auth

import com.baomidou.mybatisplus.annotation.*
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Null
import jakarta.validation.constraints.Size
import kotlinx.serialization.Serializable
import top.e404.media.module.common.entity.SaveValid
import top.e404.media.module.common.entity.UpdateValid

@Schema(description = "用户")
@Serializable
data class UserDto(
    @Schema(description = "用户id")
    @NotNull(message = "更新时必须", groups = [UpdateValid::class])
    val id: Long? = null,
    @Schema(description = "用户名")
    @NotNull(message = "创建时必须", groups = [SaveValid::class])
    @Size(min = 3, max = 16, message = "用户名必须是3-16个字符长度")
    val name: String? = null,
    @Schema(description = "用户点数")
    @Null(message = "创建时必须为空", groups = [SaveValid::class])
    var point: Int? = null,
)

@Schema(description = "用户")
@Serializable
data class UserVo(
    @Schema(description = "用户id")
    val id: Long?,
    @Schema(description = "用户名")
    val name: String?,
    var point: Int? = null,
)

@Serializable
@TableName("sys_user")
data class UserDo(
    @field:TableId(type = IdType.AUTO)
    var id: Long? = null,
    var name: String? = null,
    var password: String? = null,
    var point: Int? = null,

    @field:TableLogic
    @field:TableField(fill = FieldFill.INSERT)
    var deleted: Boolean? = null,
    @field:Version
    @field:TableField(fill = FieldFill.INSERT)
    var version: Long? = null,
    @field:TableField(fill = FieldFill.INSERT)
    var createBy: Long? = null,
    @field:TableField(fill = FieldFill.INSERT)
    var createTime: Long? = null,
    @field:TableField(fill = FieldFill.INSERT_UPDATE)
    var updateBy: Long? = null,
    @field:TableField(fill = FieldFill.INSERT_UPDATE)
    var updateTime: Long? = null,
)

fun UserDo.toVo() = UserVo(this.id, this.name)
fun UserDto.toDo() = UserDo(this.id, this.name)
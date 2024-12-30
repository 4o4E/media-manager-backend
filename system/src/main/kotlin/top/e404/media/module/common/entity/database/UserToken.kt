package top.e404.media.module.common.entity.database

import com.baomidou.mybatisplus.annotation.*
import kotlinx.serialization.Serializable

@Serializable
@TableName("sys_user_token")
data class UserTokenDo(
    @field:TableId(type = IdType.AUTO)
    var id: Long? = null,
    var userId: Long? = null,
    var token: String? = null,
    var expireTime: Long? = null,

    @field:Version
    @field:TableField(fill = FieldFill.INSERT)
    var version: Long? = null,
    @field:TableField(fill = FieldFill.INSERT)
    var createTime: Long? = null,
    @field:TableField(fill = FieldFill.INSERT_UPDATE)
    var updateTime: Long? = null,
)

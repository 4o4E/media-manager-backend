package top.e404.media.module.media.entity

import com.baomidou.mybatisplus.annotation.*
import kotlinx.serialization.Serializable
import top.e404.media.module.common.entity.`do`.AuditState
import top.e404.media.module.common.entity.typeHandler.TextListTypeHandler

@Serializable
@TableName("media_content")
data class MediaContentDo(
    @field:TableId(type = IdType.AUTO)
    val id: Long,
    val auditState: AuditState,
    val type: MediaType,
    @field:TableField(typeHandler = TextListTypeHandler::class)
    val names: List<String>,
    val remark: String,

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
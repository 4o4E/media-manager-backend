package top.e404.media.module.media.entity

import com.baomidou.mybatisplus.annotation.*
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Null
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import top.e404.media.module.common.entity.SaveValid
import top.e404.media.module.common.entity.UpdateValid
import top.e404.media.module.common.entity.database.AuditState
import top.e404.media.module.common.entity.typeHandler.LongListTypeHandler
import top.e404.media.module.media.entity.data.MediaElement
import top.e404.media.module.media.typeHandler.MediaContentTypeHandler

@Serializable
@TableName("media_content", autoResultMap = true)
data class MediaContentDo(
    @field:TableId(type = IdType.ASSIGN_ID)
    var id: Long? = null,
    var auditState: AuditState? = null,
    var type: MediaType? = null,
    var title: String? = null,
    @field:TableField(typeHandler = LongListTypeHandler::class)
    var tags: List<Long>? = null,
    @field:TableField(typeHandler = MediaContentTypeHandler::class)
    var content: List<MediaElement>? = null,
    var sign: String? = null,
    var likeCount: Long? = null,

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

@Serializable
@Schema(description = "媒体内容")
data class MediaContentDto(
    @Null(groups = [SaveValid::class])
    @NotNull(groups = [UpdateValid::class])
    var id: Long? = null,
    @Schema(description = "审核状态")
    @Null(groups = [SaveValid::class, UpdateValid::class])
    var auditState: AuditState? = null,
    @Null(groups = [SaveValid::class])
    @NotNull(groups = [UpdateValid::class])
    var type: MediaType? = null,
    var title: String? = null,
    @field:TableField(typeHandler = LongListTypeHandler::class)
    var tags: List<Long>? = null,
    var content: List<MediaElement>? = null,
    @Null(groups = [SaveValid::class])
    @NotNull(groups = [UpdateValid::class])
    var sign: String? = null,
    @Null(groups = [SaveValid::class, UpdateValid::class], message = "不应由接口更新")
    var likeCount: Long? = null,
)

@Serializable
@Schema(description = "媒体内容")
data class MediaContentVo(
    @Null(groups = [SaveValid::class])
    @NotNull(groups = [UpdateValid::class])
    var id: Long? = null,
    @Schema(description = "审核状态")
    @Null(groups = [SaveValid::class, UpdateValid::class])
    var auditState: AuditState? = null,
    @Null(groups = [SaveValid::class])
    @NotNull(groups = [UpdateValid::class])
    var type: MediaType? = null,
    var title: String? = null,
    @field:TableField(typeHandler = LongListTypeHandler::class)
    var tags: List<Long>? = null,
    var content: List<MediaElement>? = null,
    @Null(groups = [SaveValid::class])
    @NotNull(groups = [UpdateValid::class])
    var sign: String? = null,
    @Null(groups = [SaveValid::class, UpdateValid::class], message = "不应由接口更新")
    var likeCount: Long? = null,
    var liked: Boolean? = null,
)

val mediaListSerializer = ListSerializer(MediaElement.serializer())
fun MediaContentDo.toDto() = MediaContentDto(id, auditState, type, title, tags, content, sign, likeCount)
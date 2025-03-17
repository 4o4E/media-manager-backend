package top.e404.media.module.media.entity

import com.baomidou.mybatisplus.annotation.*
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Null
import kotlinx.serialization.Serializable
import top.e404.media.module.common.entity.SaveValid


@Serializable
@TableName("media_like", autoResultMap = true)
data class MediaLikeDo(
    @field:TableId(type = IdType.ASSIGN_ID)
    var id: Long? = null,
    var userId: Long? = null,
    var mediaId: Long? = null,

    @field:TableField(fill = FieldFill.INSERT)
    var createTime: Long? = null,
)

@Serializable
@Schema(description = "媒体点赞数据")
data class MediaLikeDto(
    @Schema(description = "字典id")
    @Null(groups = [SaveValid::class])
    var id: Long? = null,
    @Schema(description = "用户id")
    @NotNull(groups = [SaveValid::class])
    var userId: Long? = null,
    @Schema(description = "媒体id")
    @NotNull(groups = [SaveValid::class])
    var mediaId: Long? = null,
    var createTime: Long? = null,
)

@Serializable
@Schema(description = "媒体点赞数据")
data class MediaLikeVo(
    @Schema(description = "用户id")
    var userId: Long? = null,
    @Schema(description = "媒体id")
    var mediaId: Long? = null,
    var createTime: Long? = null,
)

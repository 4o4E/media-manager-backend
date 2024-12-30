package top.e404.media.module.media.entity

import io.swagger.v3.oas.annotations.media.Schema
import kotlinx.serialization.Serializable

@Schema(description = "消息管理接口查询选项")
@Serializable
data class MediaListOption(
    @Schema(description = "分页页数")
    val page: Long = 0,
    @Schema(description = "分页大小")
    val size: Long = 10,
    @Schema(description = "匹配模式")
    @Serializable(QueryMode.QueryModeSerializer::class)
    val queryMode: QueryMode = QueryMode.ALL,
    @Schema(description = "目标tag, 多个tag表示精准匹配", required = true)
    val tags: MutableSet<Long>,
    @Schema(description = "指定消息类型")
    val type: MediaType? = null,
)
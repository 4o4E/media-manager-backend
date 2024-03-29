package top.e404.media.module.common.entity.query

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "分页数据")
data class PageRequest(
    @Schema(description = "分页页数")
    val pageNum: Int = 0,
    @Schema(description = "分页大小")
    val pageSize: Int = 10
)

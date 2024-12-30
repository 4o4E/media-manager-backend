package top.e404.media.module.common.entity.dto.page

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "分页数据")
data class PageInfo(
    @Schema(description = "分页页数")
    val page: Long = 0,
    @Schema(description = "分页大小")
    val size: Long = 10,
    @field:Schema(description = "排序, 示例: name,asc;age,desc")
    val sort: String = ""
)

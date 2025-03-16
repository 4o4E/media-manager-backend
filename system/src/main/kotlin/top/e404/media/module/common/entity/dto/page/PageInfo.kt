package top.e404.media.module.common.entity.dto.page

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull

@Schema(description = "分页数据")
data class PageInfo(
    @Schema(description = "分页页数")
    val page: Long = 0,
    @Schema(description = "分页大小")
    val size: Long = 10,
    @Schema(description = "排序, 示例: name,asc;age,desc")
    val sort: String = ""
)

@Schema(description = "分页数据")
data class PageQuery<T : Any>(
    @Schema(description = "分页数据")
    val page: PageInfo = PageInfo(),
    @Schema(description = "扩展数据")
    @NotNull(message = "不可为空")
    val extra: T
)

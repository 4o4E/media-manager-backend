package top.e404.media.module.common.entity.page

import io.swagger.v3.oas.annotations.media.Schema
import kotlinx.serialization.Serializable

@Schema(description = "分页结果")
@Serializable
data class PageResult<T : Any, R : Any>(
    @Schema(description = "当前页数据")
    val data: Collection<T>,
    @Schema(description = "总数")
    val total: Long,
    @Schema(description = "附加数据")
    val extra: R? = null
)
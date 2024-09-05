package top.e404.media.module.common.util

import com.baomidou.mybatisplus.core.metadata.IPage
import com.baomidou.mybatisplus.core.metadata.OrderItem
import com.baomidou.mybatisplus.core.toolkit.StringUtils
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import top.e404.media.module.common.entity.page.PageInfo
import top.e404.media.module.common.entity.page.PageResult

/**
 * 通过pageable生成Mybatis的Page
 *
 * @param orderBy 是否使用orderBy
 * @param searchCount 是否在搜索完成后搜索全部count
 * @return Mybatis的Page
 */
fun <T : Any> PageInfo.toMybatisPage(
    orderBy: Boolean = true,
    searchCount: Boolean = true,
): IPage<T> {
    val page = Page<T>(page, size)
    if (orderBy && sort.isNotBlank()) for (order in sort.split(";")) {
        val (field, mode) = order.split(",")
        val orderItem = OrderItem()
            .setColumn(StringUtils.camelToUnderline(field))
            .setAsc(mode.equals("asc", true))
        page.addOrder(orderItem)
    }
    page.setSearchCount(searchCount)
    return page
}

/**
 * 将分页查询的结果转换为PageResult
 */
fun <T : Any> IPage<T>.toPageResult() = PageResult<T, Void>(records ?: emptyList(), total)

/**
 * 将分页查询的结果进行转换并转换为PageResult
 */
inline fun <T : Any, R : Any> IPage<T>.toPageResult(map: (T) -> R) =
    PageResult<R, Void>(records?.map(map) ?: emptyList(), total)

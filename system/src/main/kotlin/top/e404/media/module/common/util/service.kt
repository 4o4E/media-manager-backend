package top.e404.media.module.common.util

import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import com.baomidou.mybatisplus.extension.kotlin.KtUpdateWrapper
import com.baomidou.mybatisplus.extension.service.IService

inline fun <reified T : Any> IService<T>.listBy(
    query: (KtQueryWrapper<T>.() -> Unit)
) = KtQueryWrapper(T::class.java)
    .apply(query)
    .let { list(it) }

inline fun <reified T : Any> IService<T>.updateBy(
    entity: T? = null,
    update: (KtUpdateWrapper<T>.() -> Unit)
) = KtUpdateWrapper(T::class.java)
    .apply(update)
    .let { update(entity, it) }
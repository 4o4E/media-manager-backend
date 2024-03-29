package top.e404.media.module.common.util

import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import com.baomidou.mybatisplus.extension.kotlin.KtUpdateWrapper

inline fun <reified T : Any> query(block: KtQueryWrapper<T>.() -> Unit): KtQueryWrapper<T> {
    return KtQueryWrapper(T::class.java).apply(block)
}

inline fun <reified T : Any> update(block: KtUpdateWrapper<T>.() -> Unit): KtUpdateWrapper<T> {
    return KtUpdateWrapper(T::class.java).apply(block)
}
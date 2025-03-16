package top.e404.media.module.common.util

import com.baomidou.mybatisplus.core.metadata.IPage
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

/**
 * 复制bean实体到另一个实体, 复制其中同名的property, 遇到非空必须参数未提供时抛出[IllegalArgumentException]
 */
inline fun <reified T : Any, reified R : Any> T.convert(
    to: KClass<R>,
    vararg cover: Pair<String, Any?>
) = convertImpl(T::class, to, true, *cover)!!

/**
 * 复制bean实体到另一个实体, 复制其中同名的property, 遇到非空必须参数未提供时返回null
 */
inline fun <reified T : Any, reified R : Any> T.convertOrNull(
    to: KClass<R>,
    vararg cover: Pair<String, Any?>
) = convertImpl(T::class, to, false, *cover)

@PublishedApi
internal fun <T : Any, R : Any> T.convertImpl(
    from: KClass<T>,
    to: KClass<R>,
    throwOnConflict: Boolean = false,
    vararg cover: Pair<String, Any?>
): R? {
    val pc = to.primaryConstructor!!
    val fromProps = from.memberProperties.associate { it.name to it.get(this) }
    val args: MutableMap<KParameter, Any?> = HashMap(pc.parameters.size)
    val coverMap = mutableMapOf(*cover)
    for (parameter in pc.parameters) {
        if (coverMap.containsKey(parameter.name)) {
            val value = coverMap[parameter.name]
            // 若参数不可空
            if (value == null && !parameter.type.isMarkedNullable) {
                if (throwOnConflict) throw IllegalArgumentException("${parameter.name} require nonnull arg")
                else return null
            }
            args[parameter] = value
        }
        val prop = fromProps[parameter.name]
        if (prop == null) {
            // 跳过未指定的默认参数
            if (parameter.isOptional) continue
            // 若参数不可空
            if (!parameter.type.isMarkedNullable) {
                if (throwOnConflict) throw IllegalArgumentException("${parameter.name} require nonnull arg")
                else return null
            }
        }
        args[parameter] = prop
    }
    return pc.callBy(args)
}

/**
 * @see convert
 */
inline fun <reified T : Any, reified R : Any> Iterable<T>.convertList(
    to: KClass<R>,
    vararg cover: Pair<String, Any?>
) = convertListImpl(T::class, to, *cover)

@PublishedApi
internal fun <T : Any, R : Any> Iterable<T>.convertListImpl(
    from: KClass<T>,
    to: KClass<R>,
    vararg cover: Pair<String, Any?>
) = map { it.convertImpl(from, to, true, *cover)!! }

inline fun <From : Any, reified To : Any> IPage<From>.convert(
    to: KClass<To>,
    vararg cover: Pair<String, Any?>
) = Page<To>(current, size, total).apply {
    records = records.convertList(to, *cover)
}
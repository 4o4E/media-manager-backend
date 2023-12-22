package top.e404.media.util

import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

/**
 * 复制bean实体到另一个实体, 复制其中同名的property, 遇到非空必须参数未提供时抛出[IllegalArgumentException]
 */
inline fun <reified T : Any, reified R : Any> T.copyAs(to: KClass<R>): R {
    val from = T::class
    val pc = to.primaryConstructor!!
    val fromProps = from.memberProperties.associate { it.name to it.get(this) }
    val args: MutableMap<KParameter, Any?> = HashMap(pc.parameters.size)
    for (parameter in pc.parameters) {
        val prop = fromProps[parameter.name]
        if (prop == null) {
            // 跳过未指定的默认参数
            if (parameter.isOptional) continue
            // 若参数不可空
            if (!parameter.type.isMarkedNullable) throw IllegalArgumentException("${parameter.name} require nonnull arg")
        }
        args[parameter] = prop
    }
    return pc.callBy(args)
}

/**
 * 复制bean实体到另一个实体, 复制其中同名的property, 遇到非空必须参数未提供时返回null
 */
inline fun <reified T : Any, reified R : Any> T.copyAsOrNull(to: KClass<R>): R? {
    val from = T::class
    val pc = to.primaryConstructor!!
    val fromProps = from.memberProperties.associate { it.name to it.get(this) }
    val args: MutableMap<KParameter, Any?> = HashMap(pc.parameters.size)
    for (parameter in pc.parameters) {
        val prop = fromProps[parameter.name]
        if (prop == null) {
            // 跳过未指定的默认参数
            if (parameter.isOptional) continue
            // 若参数不可空
            if (!parameter.type.isMarkedNullable) return null
        }
        args[parameter] = prop
    }
    return pc.callBy(args)
}
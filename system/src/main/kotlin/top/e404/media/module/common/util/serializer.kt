package top.e404.media.module.common.util

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor

inline fun <reified T> KSerializer<T>.primitive(kind: PrimitiveKind = PrimitiveKind.STRING) =
    PrimitiveSerialDescriptor(T::class.java.name, kind)
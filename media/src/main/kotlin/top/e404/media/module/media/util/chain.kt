package top.e404.media.module.media.util

import top.e404.media.module.media.entity.MediaElementVisitor
import top.e404.media.module.media.entity.data.MediaElement

/**
 * 计算消息的SHA256
 */
fun Iterable<MediaElement>.sign() = MediaElementVisitor().also {
    for (message in this) message.sign(it)
}.result
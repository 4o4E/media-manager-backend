package top.e404.media.module.media.util

import top.e404.media.module.media.entity.MessageVisitor
import top.e404.media.module.media.entity.data.Message

/**
 * 计算消息的SHA256
 */
fun Iterable<Message>.sha() = MessageVisitor().also {
    for (message in this) message.sign(it)
}.result
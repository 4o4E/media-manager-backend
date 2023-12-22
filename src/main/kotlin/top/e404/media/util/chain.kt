package top.e404.media.util

import top.e404.media.entity.message.Message
import top.e404.media.entity.message.MessageVisitor

fun Iterable<Message>.sha() = MessageVisitor().also {
    for (message in this) message.sign(it)
}.result
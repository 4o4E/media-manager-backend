package top.e404.media.module.media.util

import com.github.jershell.kbson.KBson
import org.bson.BsonDocument
import org.springframework.beans.factory.annotation.Autowired
import top.e404.media.module.media.entity.MessageData

@set:Autowired
lateinit var kbson: KBson

fun BsonDocument.toMessageData() = kbson.load(MessageData.serializer(), this)
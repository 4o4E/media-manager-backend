package top.e404.media.module.media.util

import com.github.jershell.kbson.KBson
import com.mongodb.client.MongoIterable
import org.bson.BsonDocument
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import top.e404.media.module.media.entity.MessageData

// 自动装配该kbson
@Component
class BsonConvert {
    @Autowired
    fun initKBson(bson: KBson) {
        kbson = bson
    }
}

lateinit var kbson: KBson

/**
 * 将bsonDocument转换为MessageData
 */
fun BsonDocument.toMessageData(): MessageData = kbson.parse(MessageData.serializer(), this)
fun MongoIterable<BsonDocument>.toMessageData() = map { it.toMessageData() }.toList()
fun MessageData.toBsonDocument() = kbson.stringify(MessageData.serializer(), this)
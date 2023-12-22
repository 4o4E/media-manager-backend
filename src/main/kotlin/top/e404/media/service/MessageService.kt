package top.e404.media.service

import com.github.jershell.kbson.KBson
import com.mongodb.client.MongoCollection
import org.bson.BsonDocument
import org.bson.BsonString
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import top.e404.media.entity.message.Message
import top.e404.media.entity.message.MessageData
import top.e404.media.entity.message.MessageInfo
import top.e404.media.entity.message.MessageType
import top.e404.media.entity.message.meta.Meta
import top.e404.media.util.sha
import top.e404.media.util.toMessageData
import java.util.*


interface MessageService {
    fun getById(id: String): MessageData?
    fun save(
        chain: List<Message>,
        upload: UUID,
        tags: MutableSet<String>,
        metas: MutableList<Meta>
    ): MessageData
}

@Service
class MessageServiceImpl : MessageService {
    @Autowired
    lateinit var media: MongoCollection<BsonDocument>

    @Autowired
    lateinit var kBson: KBson

    override fun getById(id: String): MessageData? = media
        .find(BsonDocument("info.sha", BsonString(id)))
        .firstOrNull()
        ?.toMessageData()

    override fun save(
        chain: List<Message>,
        upload: UUID,
        tags: MutableSet<String>,
        metas: MutableList<Meta>
    ): MessageData {
        val sha = chain.sha()
        val exists = media.find(BsonDocument("info.sha", BsonString(sha))).firstOrNull()
        if (exists != null) return kBson.load(MessageData.serializer(), exists)

        val data = MessageData(
            MessageInfo(sha, upload, System.currentTimeMillis(), MessageType.byMessage(chain), false, tags, metas),
            chain
        )

        val result = media.insertOne(kBson.stringify(MessageData.serializer(), data))
        if (!result.wasAcknowledged()) throw IllegalArgumentException("already exists")
        return data
    }
}
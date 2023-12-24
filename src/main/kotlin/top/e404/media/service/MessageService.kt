package top.e404.media.service

import com.github.jershell.kbson.KBson
import com.mongodb.client.MongoCollection
import org.bson.BsonDocument
import org.bson.BsonString
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import top.e404.media.advice.currentUser
import top.e404.media.entity.message.*
import top.e404.media.util.sha
import top.e404.media.util.toMessageData


interface MessageService {
    fun getById(id: String): MessageData?
    fun save(
        dto: MessageDto
    ): MessageData
}

@Service
class MessageServiceImpl : MessageService {
    @Autowired
    lateinit var media: MongoCollection<BsonDocument>
    @Autowired
    lateinit var fileService: FileService

    @Autowired
    lateinit var kBson: KBson

    override fun getById(id: String): MessageData? = media
        .find(BsonDocument("info.id", BsonString(id)))
        .firstOrNull()
        ?.toMessageData()

    override fun save(dto: MessageDto): MessageData {
        val (chain, tags) = dto
        require(fileService.allExists(chain.filterIsInstance<BinaryMessage>().map(BinaryMessage::id))) { "消息中的二进制文件不存在" }
        val id = chain.sha()
        val upload = currentUser!!.user.id!!
        val exists = media.find(BsonDocument("info.id", BsonString(id))).firstOrNull()
        if (exists != null) return kBson.load(MessageData.serializer(), exists)

        val data = MessageData(
            MessageInfo(
                id = id,
                upload = upload,
                time = System.currentTimeMillis(),
                type = MessageType.byMessage(chain),
                approved = ApprovedState.WAIT,
                tags = tags,
                metas = mutableListOf()
            ),
            chain
        )

        val result = media.insertOne(kBson.stringify(MessageData.serializer(), data))
        if (!result.wasAcknowledged()) throw IllegalArgumentException("already exists")
        return data
    }
}
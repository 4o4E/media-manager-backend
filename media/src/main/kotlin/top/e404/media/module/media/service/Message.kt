package top.e404.media.module.media.service

import com.github.jershell.kbson.KBson
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.FindOneAndUpdateOptions
import com.mongodb.client.model.ReturnDocument
import com.mongodb.client.result.UpdateResult
import org.bson.BsonDocument
import org.bson.BsonString
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import top.e404.media.module.common.advice.currentUser
import top.e404.media.module.common.entity.query.PageRequest
import top.e404.media.module.media.util.sha
import top.e404.media.module.media.entity.MessageData
import top.e404.media.module.media.entity.MessageDto
import top.e404.media.module.media.entity.MessageQueryDto
import top.e404.media.module.media.entity.QueryMode
import top.e404.media.module.media.entity.comment.MessageComment
import top.e404.media.module.media.entity.comment.MessageCommentDto
import top.e404.media.module.media.entity.comment.MessageCommentList
import top.e404.media.module.media.entity.data.BinaryMessage
import top.e404.media.module.media.entity.info.ApprovedState
import top.e404.media.module.media.entity.info.MessageInfo
import top.e404.media.module.media.entity.info.MessageType
import top.e404.media.module.media.util.*
import top.e404.media.module.media.util.toMessageData


interface MessageService {
    /**
     * 通过id获取
     */
    fun getById(id: String): MessageData?

    /**
     * 高级查询
     */
    fun query(dto: MessageQueryDto): List<MessageData>

    /**
     * 上传新的message
     */
    fun save(dto: MessageDto): MessageData

    /**
     * 添加tag
     */
    fun addTags(id: String, tags: Set<String>): UpdateResult

    /**
     * 设置tag
     */
    fun setTags(id: String, tags: Set<String>): UpdateResult

    /**
     * 删除tag
     */
    fun delTags(id: String, tags: Set<String>): UpdateResult

    /**
     * 发送评论
     */
    fun addComment(id: String, dto: MessageCommentDto): MessageComment
    fun getComment(id: String, page: PageRequest): List<MessageComment>
}

@Service
class MessageServiceImpl : MessageService {
    @set:Autowired
    lateinit var media: MongoCollection<BsonDocument>

    @set:Autowired
    lateinit var fileService: FileService

    @set:Autowired
    lateinit var kBson: KBson

    override fun getById(id: String): MessageData? = media
        .find(bson("info.id", id))
        .firstOrNull()
        ?.toMessageData()

    override fun query(dto: MessageQueryDto): List<MessageData> {
        val query = when (dto.queryMode) {
            QueryMode.ANY -> bson("info.id", bsonIn(bsonArray(dto.tags)))
            QueryMode.ALL -> bson("info.id", bsonAny(bsonArray(dto.tags)))
        }
        return media
            .find(query)
            .projection(bson("comment", 0))
            .limit(dto.count)
            .map(BsonDocument::toMessageData)
            .toList()
    }

    override fun save(dto: MessageDto): MessageData {
        val (chain, tags) = dto
        require(fileService.allExists(chain.filterIsInstance<BinaryMessage>().map(BinaryMessage::id))) {
            "消息中的二进制文件不存在"
        }
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
            chain,
            MessageCommentList()
        )

        val result = media.insertOne(kBson.stringify(MessageData.serializer(), data))
        if (!result.wasAcknowledged()) throw IllegalArgumentException("already exists")
        return data
    }

    override fun addTags(id: String, tags: Set<String>) = media.updateOne(
        bson("info.id", id),
        bsonAddToSet("info.tags", bsonEach(bsonArray(tags)))
    )

    override fun setTags(id: String, tags: Set<String>) = media.updateOne(
        bson("info.id", id),
        bsonSet("info.tags", bsonArray(tags))
    )

    override fun delTags(id: String, tags: Set<String>) = media.updateOne(
        bson("info.id", id),
        bsonPull("info.tags", bsonEach(bsonArray(tags)))
    )

    override fun addComment(id: String, dto: MessageCommentDto): MessageComment {
        val index = media.findOneAndUpdate(
            bson("info.id", id),
            bsonInc("comment.current", bson(1)),
            FindOneAndUpdateOptions()
                .returnDocument(ReturnDocument.AFTER)
                .projection(bson("_id" to bson(0), "current" to bson(1)))
        )?.getInt64("current")?.value
        requireNotNull(index) { "无法找到对应消息" }
        val comment = MessageComment(index, dto.sender, dto.type, dto.content, System.currentTimeMillis())
        media.updateOne(
            bson("info.id", id),
            bsonPush(kBson.stringify(MessageComment.serializer(), comment))
        )
        return comment
    }

    override fun getComment(id: String, page: PageRequest) = media.aggregate(
        mutableListOf(
            bsonMatch(bson("info.id", id)),
            bson().deepPut(
                "\$project", "list", "\$slice",
                value = bsonArray(
                    bson("\$comment.list"),
                    bson(page.pageNum * page.pageSize),
                    bson(page.pageSize)
                )
            )
        )
    ).map { kBson.parse(MessageComment.serializer(), it) }.toList()
}
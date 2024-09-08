package top.e404.media.module.media.service

import com.github.jershell.kbson.KBson
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.FindOneAndUpdateOptions
import com.mongodb.client.model.ReturnDocument
import com.mongodb.client.result.UpdateResult
import org.bson.BsonDocument
import org.bson.BsonString
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import top.e404.media.module.common.advice.currentUser
import top.e404.media.module.common.entity.page.PageInfo
import top.e404.media.module.media.entity.MessageData
import top.e404.media.module.media.entity.MessageDto
import top.e404.media.module.media.entity.MessageQueryDto
import top.e404.media.module.media.entity.QueryMode
import top.e404.media.module.media.entity.comment.MessageComment
import top.e404.media.module.media.entity.comment.MessageCommentDto
import top.e404.media.module.media.entity.data.BinaryMessage
import top.e404.media.module.media.entity.ApprovedState
import top.e404.media.module.media.entity.MessageType
import top.e404.media.module.media.util.*
import java.time.LocalDateTime
import java.util.*


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
     * 导入message
     */
    fun import(dto: MessageDto, time: LocalDateTime): MessageData

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
    fun postComment(id: String, dto: MessageCommentDto): MessageComment

    /**
     * 获取评论
     */
    fun listComment(id: String, page: PageInfo): List<MessageComment>

    /**
     * 分页获取
     */
    fun random(count: Long): List<MessageData>
}

@Service
class MessageServiceImpl : MessageService {
    @set:Qualifier("media")
    @set:Autowired
    lateinit var media: MongoCollection<BsonDocument>

    @set:Autowired
    lateinit var fileService: FileService

    @set:Autowired
    lateinit var kBson: KBson

    override fun getById(id: String): MessageData? = media
        .find(bson("id", id))
        .firstOrNull()
        ?.toMessageData()

    override fun query(dto: MessageQueryDto): List<MessageData> {
        val query = when (dto.queryMode) {
            QueryMode.ANY -> bson("tags", bsonIn(bsonArray(dto.tags)))
            QueryMode.ALL -> bson("tags", bsonAll(bsonArray(dto.tags)))
        }
        return media
            .aggregate(
                mutableListOf(
                    bsonSample(dto.count),
                    bsonMatch(query)
                )
            )
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
        val exists = media.find(BsonDocument("id", BsonString(id))).firstOrNull()
        if (exists != null) return kBson.load(MessageData.serializer(), exists)

        val data = MessageData(
            id,
            upload,
            System.currentTimeMillis(),
            MessageType.byMessage(chain),
            ApprovedState.WAIT,
            tags,
            chain
        )

        val result = media.insertOne(kBson.stringify(MessageData.serializer(), data))
        if (!result.wasAcknowledged()) throw IllegalArgumentException("already exists")
        return data
    }

    override fun import(dto: MessageDto, time: LocalDateTime): MessageData {
        val (chain, tags) = dto
        require(fileService.allExists(chain.filterIsInstance<BinaryMessage>().map(BinaryMessage::id))) {
            "消息中的二进制文件不存在"
        }
        val id = chain.sha()
        val upload = currentUser!!.user.id!!
        val exists = media.find(BsonDocument("id", BsonString(id))).firstOrNull()
        if (exists != null) return kBson.load(MessageData.serializer(), exists)

        val data = MessageData(
            id,
            upload,
            time.atZone(TimeZone.getDefault().toZoneId()).toEpochSecond() * 1000,
            MessageType.byMessage(chain),
            ApprovedState.PASS,
            tags,
            chain,
        )

        val result = media.insertOne(kBson.stringify(MessageData.serializer(), data))
        if (!result.wasAcknowledged()) throw IllegalArgumentException("already exists")
        return data
    }

    override fun addTags(id: String, tags: Set<String>) = media.updateOne(
        bson("id", id),
        bsonAddToSet("tags", bsonEach(bsonArray(checkTag(tags))))
    )

    override fun setTags(id: String, tags: Set<String>) = media.updateOne(
        bson("id", id),
        bsonSet("tags", bsonArray(checkTag(tags)))
    )

    override fun delTags(id: String, tags: Set<String>) = media.updateOne(
        bson("id", id),

        bsonPull("tags", bsonEach(bsonArray((tags))))
    )

    @Value("\${application.media.tag.rule}")
    lateinit var rule: String

    private val ruleRegex by lazy { Regex(rule) }

    /**
     * 检查tag是否符合要求的正则
     */
    private fun checkTag(tags: Set<String>): Set<String> {
        for (it in tags) {
            require(ruleRegex.matches(it)) { "tag格式错误" }
        }
        return tags
    }

    override fun postComment(id: String, dto: MessageCommentDto): MessageComment {
        val index = media.findOneAndUpdate(
            bson("id", id),
            bsonInc("comment.current", bson(1)),
            FindOneAndUpdateOptions()
                .returnDocument(ReturnDocument.AFTER)
                .projection(bson("_id" to bson(0), "current" to bson(1)))
        )?.getInt64("current")?.value
        requireNotNull(index) { "无法找到对应消息" }
        val comment = MessageComment(index, dto.sender, dto.type, dto.content, System.currentTimeMillis())
        media.updateOne(
            bson("id", id),
            bsonPush(kBson.stringify(MessageComment.serializer(), comment))
        )
        return comment
    }

    override fun listComment(id: String, page: PageInfo) = media.aggregate(
        mutableListOf(
            bsonMatch(bson("id", id)),
            bson().deepPut(
                "\$project", "list", "\$slice",
                value = bsonArray(
                    bson("\$comment.list"),
                    bson(page.page * page.size),
                    bson(page.size)
                )
            )
        )
    ).map { kBson.parse(MessageComment.serializer(), it) }.toList()

    override fun random(count: Long): List<MessageData> {
        return media.aggregate(
            mutableListOf(
                bsonSample(count)
            )
        ).map { kBson.parse(MessageData.serializer(), it) }.toList()
    }
}
@file:Suppress("INVISIBLE_REFERENCE", "INVISIBLE_MEMBER")

package top.e404.media.module.media.service

import com.mongodb.client.MongoCollection
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import org.bson.BsonDocument
import org.jetbrains.skia.Image
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import top.e404.media.module.common.advice.CurrentUser
import top.e404.media.module.common.advice.currentUsers
import top.e404.media.module.common.entity.`do`.RoleDo
import top.e404.media.module.common.entity.`do`.RolePermDo
import top.e404.media.module.common.entity.`do`.UserTokenDo
import top.e404.media.module.common.service.database.PermService
import top.e404.media.module.common.service.database.RoleService
import top.e404.media.module.common.service.database.UserService
import top.e404.media.module.common.util.log
import top.e404.media.module.common.util.primitive
import top.e404.media.module.common.util.query
import top.e404.media.module.media.entity.MessageDto
import top.e404.media.module.media.entity.TagDo
import top.e404.media.module.media.entity.data.ImageMessage
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * 初始化数据库数据
 */
@Component
class MongodbIndexInitRunner : ApplicationRunner {
    companion object {
        var inInitialize = false
            private set
        const val INIT_FILE_NAME = ".index-version"
    }

    private val log = log()

    @set:Autowired
    lateinit var userService: UserService

    @set:Autowired
    lateinit var roleService: RoleService

    @set:Autowired
    lateinit var permService: PermService

    @set:Autowired
    lateinit var fileService: FileService

    @set:Autowired
    lateinit var messageService: MessageService

    @set:Autowired
    lateinit var tagService: TagService

    @set:Qualifier("media")
    @set:Autowired
    lateinit var media: MongoCollection<BsonDocument>

    override fun run(args: ApplicationArguments) {

        // 已经创建过就不要创建, 启动快
        // val currentIndexVersion = "0"
        // File(INIT_FILE_NAME).run {
        //     if (exists()) {
        //         val version = readText()
        //         if (version == currentIndexVersion) {
        //             return
        //         }
        //     }
        //     log.info("更新mongodb索引")
        //     writeText(currentIndexVersion)
        // }

        if (true) return
        val json = Json {
            ignoreUnknownKeys = true
        }
        val dir = File("D:\\Desktop\\pic\\pic")
        val serializer = MapSerializer(String.serializer(), Pic.serializer())
        val map = dir.resolve("data.json").readText().let { json.decodeFromString(serializer, it) }
        val store = dir.resolve("store")
        val tokenDo = UserTokenDo()
        val userDo = userService.getById(1)
        val roles = roleService.getRoleByUserId(1).toSet()
        val perms = if (roles.isEmpty()) emptyList() else permService.listByIds(roles.flatMap { it.perms ?: emptyList() })
        val current = CurrentUser(userDo, tokenDo, roles, perms)
        currentUsers.set(current)
        for ((name, pic) in map.entries) {
            val image = store.resolve(name)
            val bytes = image.readBytes()
            val id = fileService.upload(bytes)
            val format = image.name.substringAfterLast(".")
            val img = Image.makeFromEncoded(bytes)
            val message = MessageDto(
                mutableListOf(ImageMessage(id, format, false, img.width, img.height)),
                pic.tags.map {
                    tagService.getOne(query {
                        eq(TagDo::name, it)
                    })?.id ?: tagService.createTag(it, "", listOf(it)).id!!
                }.toMutableSet()
            )
            messageService.import(message, pic.time)

            log.info("save: $id")
        }
    }

    @Serializable
    data class Pic(
        @Serializable(LDTSerializer::class)
        val time: LocalDateTime,
        val name: String,
        val tags: MutableSet<String>
    )

    object LDTSerializer : KSerializer<LocalDateTime> {
        override val descriptor = primitive()
        private val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss")

        override fun deserialize(decoder: Decoder): LocalDateTime {
            return LocalDateTime.parse(decoder.decodeString(), formatter)
        }

        override fun serialize(encoder: Encoder, value: LocalDateTime) {
            encoder.encodeString(formatter.format(value))
        }

    }
}
@file:Suppress("INVISIBLE_REFERENCE", "INVISIBLE_MEMBER")

package top.e404.media.module.media.service

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import org.jetbrains.skia.Image
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import top.e404.media.module.common.advice.CurrentUser
import top.e404.media.module.common.advice.currentUsers
import top.e404.media.module.common.entity.database.UserTokenDo
import top.e404.media.module.common.service.database.RoleService
import top.e404.media.module.common.service.database.UserService
import top.e404.media.module.common.util.log
import top.e404.media.module.common.util.primitive
import top.e404.media.module.media.entity.MediaTagDto
import top.e404.media.module.media.entity.data.ImageElement
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * 初始化数据库数据
 */
@Component
class DataInitRunner : ApplicationRunner {
    private val log = log()

    @set:Autowired
    lateinit var userService: UserService

    @set:Autowired
    lateinit var roleService: RoleService

    @set:Autowired
    lateinit var fileService: FileService

    @set:Autowired
    lateinit var mediaContentService: MediaContentService

    @set:Autowired
    lateinit var mediaTagService: MediaTagService

    override fun run(args: ApplicationArguments) {
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
        val perms = roles.flatMap { it.perms ?: emptyList() }.toSet()
        val current = CurrentUser(userDo, tokenDo, roles, perms)
        currentUsers.set(current)
        for ((name, pic) in map.entries) {
            val image = store.resolve(name)
            val bytes = image.readBytes()
            val id = fileService.upload(bytes)
            val format = image.name.substringAfterLast(".")
            val img = Image.makeFromEncoded(bytes)
            mediaContentService.import(
                mutableListOf(ImageElement(id, format, false, img.width, img.height)),
                pic.tags.map { tag ->
                    mediaTagService.getByName(tag)?.id ?: mediaTagService.createTag(MediaTagDto(null, listOf(tag), tag)).id!!
                }, pic.time)

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
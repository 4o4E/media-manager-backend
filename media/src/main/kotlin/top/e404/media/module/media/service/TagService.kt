package top.e404.media.module.media.service

import com.baomidou.mybatisplus.extension.service.IService
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import top.e404.media.module.common.exception.HttpRequestException
import top.e404.media.module.common.exception.NoChangeException
import top.e404.media.module.common.exception.NotFoundException
import top.e404.media.module.common.util.query
import top.e404.media.module.common.util.update
import top.e404.media.module.media.entity.TagAliasDo
import top.e404.media.module.media.entity.TagDo
import top.e404.media.module.media.entity.TagDto
import top.e404.media.module.media.mapper.TagMapper

interface TagService : IService<TagDo> {
    fun listTags(key: String?, lastUpdated: Long?): List<TagDto>
    fun createTag(name: String, description: String): TagDo
    fun editTag(id: Long, name: String, description: String)
    fun allExist(tags: MutableSet<Long>): Boolean
}

@Service
class TagServiceImpl : TagService, ServiceImpl<TagMapper, TagDo>() {
    @set:Autowired
    lateinit var tagAliasService: TagAliasService

    override fun listTags(key: String?, lastUpdated: Long?): List<TagDto> {
        if (lastUpdated != null) {
            val lastUpdateTime = baseMapper.getLastUpdateTime()
            if (lastUpdateTime <= lastUpdated) {
                throw NoChangeException()
            }
        }
        val list = baseMapper.listTags(key)
        val aliases = tagAliasService.list(query {
            val tagIds = list.map { it.id }
            `in`(tagIds.isNotEmpty(), TagAliasDo::tagId, tagIds)
        }).groupBy { it.tagId }
        return list.map { tag ->
            TagDto(
                tag.id!!,
                tag.name!!,
                tag.description!!,
                aliases[tag.id]?.map { it.name!! } ?: emptyList()
            )
        }
    }

    @Transactional(rollbackFor = [Throwable::class])
    override fun createTag(name: String, description: String): TagDo {
        val hasExists = tagAliasService.count(query {
            eq(TagAliasDo::name, name)
        }) > 0
        if (hasExists) throw HttpRequestException("标签已存在")
        val tagDo = TagDo(name = name, description = description)
        save(tagDo)
        tagAliasService.save(TagAliasDo(tagId = tagDo.id, name = name))
        return tagDo
    }

    override fun editTag(id: Long, name: String, description: String) {
        val success = update(update {
            eq(TagDo::id, id)
            set(TagDo::name, name)
            set(TagDo::description, description)
        })
        if (!success) throw NotFoundException("标签不存在")
    }

    override fun allExist(tags: MutableSet<Long>): Boolean {
        val count = count(query {
            `in`(tags.isNotEmpty(), TagDo::id, tags)
        })
        return count == tags.size.toLong()
    }


}
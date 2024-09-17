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
import top.e404.media.module.media.entity.TagAliasDo
import top.e404.media.module.media.entity.TagDo
import top.e404.media.module.media.entity.TagVo
import top.e404.media.module.media.mapper.TagMapper

interface TagService : IService<TagDo> {
    fun listTags(key: String?, lastUpdated: Long?): List<TagVo>
    fun createTag(name: String, description: String, alias: List<String>): TagDo
    fun editTag(id: Long, name: String, description: String)
    fun allExist(tags: MutableSet<Long>): Boolean
    fun addTagAlias(tagId: Long, alias: String)
    fun removeTagAlias(tagId: Long, alias: String)
}

@Service
class TagServiceImpl : TagService, ServiceImpl<TagMapper, TagDo>() {
    @set:Autowired
    lateinit var tagAliasService: TagAliasService

    private var lastModify = System.currentTimeMillis()

    override fun listTags(key: String?, lastUpdated: Long?): List<TagVo> {
        if (lastUpdated != null) {
            if (lastModify <= lastUpdated) {
                throw NoChangeException()
            }
        }
        val list = baseMapper.listTags(key)
        val aliases = tagAliasService.list(query {
            val tagIds = list.map { it.id }
            `in`(tagIds.isNotEmpty(), TagAliasDo::tagId, tagIds)
        }).groupBy { it.tagId }
        return list.map { tag ->
            TagVo(
                tag.id!!,
                tag.name!!,
                tag.description!!,
                aliases[tag.id]?.map { it.name!! } ?: emptyList()
            )
        }
    }

    @Transactional(rollbackFor = [Throwable::class])
    override fun createTag(name: String, description: String, alias: List<String>): TagDo {
        val hasExists = tagAliasService.count(query {
            eq(TagAliasDo::name, name)
        }) > 0
        if (hasExists) throw HttpRequestException("标签已存在")
        val tagDo = TagDo(name = name, description = description)
        save(tagDo)
        tagAliasService.save(TagAliasDo(tagId = tagDo.id, name = name))
        lastModify = System.currentTimeMillis()
        return tagDo
    }

    override fun editTag(id: Long, name: String, description: String) {
        val success = updateById(TagDo(id, name, description))
        if (!success) throw NotFoundException("标签不存在")
        lastModify = System.currentTimeMillis()
    }

    override fun allExist(tags: MutableSet<Long>): Boolean {
        val count = count(query {
            `in`(tags.isNotEmpty(), TagDo::id, tags)
        })
        return count == tags.size.toLong()
    }

    override fun addTagAlias(tagId: Long, alias: String) {
        tagAliasService.save(TagAliasDo(tagId = tagId, name = alias))
        lastModify = System.currentTimeMillis()
    }

    override fun removeTagAlias(tagId: Long, alias: String) {
        tagAliasService.remove(query {
            eq(TagAliasDo::tagId, tagId)
            eq(TagAliasDo::name, alias)
        })
        lastModify = System.currentTimeMillis()
    }
}
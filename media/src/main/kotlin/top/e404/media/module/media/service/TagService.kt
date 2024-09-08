package top.e404.media.module.media.service

import com.baomidou.mybatisplus.core.metadata.IPage
import com.baomidou.mybatisplus.extension.service.IService
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import top.e404.media.module.common.entity.BaseResp
import top.e404.media.module.common.entity.page.PageInfo
import top.e404.media.module.common.entity.page.PageResult
import top.e404.media.module.common.exception.NotFoundException
import top.e404.media.module.common.util.query
import top.e404.media.module.common.util.update
import top.e404.media.module.common.util.toMybatisPage
import top.e404.media.module.common.util.toPageResult
import top.e404.media.module.media.entity.TagAliasDo
import top.e404.media.module.media.entity.TagDo
import top.e404.media.module.media.entity.TagDto
import top.e404.media.module.media.mapper.TagMapper

interface TagService : IService<TagDo> {
    fun listTags(pageInfo: PageInfo, key: String?): BaseResp<PageResult<TagDto, Void>>
    fun createTag(name: String, description: String)
    fun editTag(id: Long, name: String, description: String)
}

@Service
class TagServiceImpl : TagService, ServiceImpl<TagMapper, TagDo>() {
    @set:Autowired
    lateinit var tagAliasService: TagAliasService

    override fun listTags(pageInfo: PageInfo, key: String?): BaseResp<PageResult<TagDto, Void>> {
        val page = pageInfo.toMybatisPage<TagDo>()
        baseMapper.listTags(page, key)
        val aliases = tagAliasService.list(query {
            `in`(TagAliasDo::tagId, page.records.map { it.id })
        }).groupBy { it.tagId }
        return page.toPageResult { tag ->
            TagDto(
                tag.id!!,
                tag.name!!,
                tag.description!!,
                aliases[tag.id]?.map { it.name!! } ?: emptyList()
            )
        }
    }

    @Transactional(rollbackFor = [Throwable::class])
    override fun createTag(name: String, description: String) {
        tagAliasService.count(query {

        })
        val tagDo = TagDo(name = name, description = description)
        save(tagDo)
        tagAliasService.save(TagAliasDo(tagId = tagDo.id, name = name))
    }

    override fun editTag(id: Long, name: String, description: String) {
        val success = update(update {
            eq(TagDo::id, id)
            set(TagDo::name, name)
            set(TagDo::description, description)
        })
        if (!success) throw NotFoundException("标签不存在")
    }


}
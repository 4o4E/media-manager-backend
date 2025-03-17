package top.e404.media.module.media.service

import com.baomidou.mybatisplus.extension.service.IService
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import top.e404.media.module.common.exception.exists
import top.e404.media.module.common.exception.notChanged
import top.e404.media.module.common.exception.notFound
import top.e404.media.module.common.util.convert
import top.e404.media.module.common.util.convertList
import top.e404.media.module.common.util.listBy
import top.e404.media.module.common.util.query
import top.e404.media.module.media.entity.MediaTagDo
import top.e404.media.module.media.entity.MediaTagDto
import top.e404.media.module.media.entity.MediaTagVo
import top.e404.media.module.media.mapper.MediaTagMapper

interface MediaTagService : IService<MediaTagDo> {
    fun getByName(name: String): MediaTagDo?
    fun exists(names: List<String>): Boolean
    fun listTags(key: String?, lastUpdated: Long?): List<MediaTagVo>
    fun createTag(dto: MediaTagDto): MediaTagDo
    fun editTag(dto: MediaTagDto)
    fun allExist(tags: MutableSet<Long>): Boolean
    fun remove(tagId: Long): Boolean
}

@Service
class MediaTagServiceImpl : MediaTagService, ServiceImpl<MediaTagMapper, MediaTagDo>() {
    private var lastModify = System.currentTimeMillis()

    override fun getByName(name: String): MediaTagDo? {
        return baseMapper.getByName(name)
    }

    override fun exists(names: List<String>): Boolean {
        return baseMapper.exists(names)
    }

    override fun listTags(key: String?, lastUpdated: Long?): List<MediaTagVo> {
        if (lastUpdated != null && lastModify <= lastUpdated) {
            notChanged()
        }
        return listBy { orderByAsc(MediaTagDo::id) }.convertList(MediaTagVo::class)
    }

    @Transactional(rollbackFor = [Exception::class, Error::class])
    override fun createTag(dto: MediaTagDto): MediaTagDo {
        val hasExists = exists(dto.names!!)
        if (hasExists) exists("别名")
        val tagDo = dto.convert(MediaTagDo::class)
        save(tagDo)
        lastModify = System.currentTimeMillis()
        return tagDo
    }

    override fun editTag(dto: MediaTagDto) {
        val success = updateById(dto.convert(MediaTagDo::class))
        if (!success) notFound("标签")
        lastModify = System.currentTimeMillis()
    }

    override fun allExist(tags: MutableSet<Long>): Boolean {
        val count = count(query {
            `in`(tags.isNotEmpty(), MediaTagDo::id, tags)
        })
        return count == tags.size.toLong()
    }

    override fun remove(tagId: Long): Boolean {
        val success = removeById(tagId)
        if (success) lastModify = System.currentTimeMillis()
        return success
    }
}
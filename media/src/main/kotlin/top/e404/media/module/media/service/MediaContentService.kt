package top.e404.media.module.media.service

import com.baomidou.mybatisplus.core.metadata.IPage
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.baomidou.mybatisplus.extension.service.IService
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import org.springframework.stereotype.Service
import top.e404.media.module.common.entity.database.AuditState
import top.e404.media.module.common.entity.dto.page.PageInfo
import top.e404.media.module.common.util.convert
import top.e404.media.module.media.entity.*
import top.e404.media.module.media.entity.comment.MessageComment
import top.e404.media.module.media.entity.comment.MessageCommentDto
import top.e404.media.module.media.entity.data.MediaElement
import top.e404.media.module.media.mapper.MediaContentMapper
import top.e404.media.module.media.util.sign
import java.time.LocalDateTime

interface MediaContentService : IService<MediaContentDo> {

    /**
     * 通过id获取
     */
    fun get(id: Long): MediaContentDto?

    /**
     * 高级查询
     */
    fun query(dto: MessageQueryDto): List<MediaContentDto>

    /**
     * 上传新的message
     */
    fun save(dto: MediaContentDto): MediaContentDto

    /**
     * 更新message
     */
    fun update(dto: MediaContentDto)

    /**
     * 导入message
     */
    fun import(content: List<MediaElement>, tags: List<Long>, time: LocalDateTime): MediaContentDto

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
    fun random(count: Long): List<MediaContentDto>

    /**
     * 按顺序分页搜索
     */
    fun list(dto: MediaListOption): IPage<MediaContentDo>
}

@Service
class MediaContentServiceImpl : MediaContentService, ServiceImpl<MediaContentMapper, MediaContentDo>() {
    override fun get(id: Long): MediaContentDto? {
        return getById(id)?.toDto()
    }

    override fun query(dto: MessageQueryDto): List<MediaContentDto> {
        return baseMapper.query(dto).toDto()
    }

    override fun save(dto: MediaContentDto): MediaContentDto {
        val entity = dto.run {
            MediaContentDo(
                auditState = AuditState.PENDING,
                type = MediaType.byMessage(dto.content!!),
                title = dto.title,
                tags = dto.tags,
                content = dto.content,
                sign = dto.content!!.sign(),
            )
        }
        save(entity)
        return entity.toDto()
    }

    override fun update(dto: MediaContentDto) {
        updateById(dto.convert(MediaContentDo::class))
    }

    override fun import(content: List<MediaElement>, tags: List<Long>, time: LocalDateTime): MediaContentDto {
        val entity = MediaContentDo(
            auditState = AuditState.PASS,
            type = MediaType.IMAGE,
            title = "untitled",
            tags = tags,
            content = content,
            sign = content.sign()
        )
        save(entity)
        return entity.toDto()
    }

    override fun postComment(id: String, dto: MessageCommentDto): MessageComment {
        TODO("Not yet implemented")
    }

    override fun listComment(id: String, page: PageInfo): List<MessageComment> {
        TODO("Not yet implemented")
    }

    override fun random(count: Long): List<MediaContentDto> {
        return baseMapper.random(count).toDto()
    }

    override fun list(dto: MediaListOption): IPage<MediaContentDo> {
        return baseMapper.page(Page(dto.page, dto.size), dto)
    }
}
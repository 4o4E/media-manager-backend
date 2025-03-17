package top.e404.media.module.media.service

import com.baomidou.mybatisplus.core.metadata.IPage
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.baomidou.mybatisplus.extension.service.IService
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import top.e404.media.module.common.advice.currentUser
import top.e404.media.module.common.entity.database.AuditState
import top.e404.media.module.common.entity.dto.page.PageInfo
import top.e404.media.module.common.exception.exists
import top.e404.media.module.common.exception.notFound
import top.e404.media.module.common.util.convert
import top.e404.media.module.common.util.removeBy
import top.e404.media.module.common.util.updateBy
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
    fun query(dto: MessageQueryDto): List<MediaContentVo>

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
    fun random(count: Long): List<MediaContentVo>

    /**
     * 按顺序分页搜索
     */
    fun list(dto: MediaListOption): IPage<MediaContentVo>

    /**
     * 点赞
     */
    fun like(mediaId: Long)

    /**
     * 取消点赞
     */
    fun undoLike(mediaId: Long)
}

@Service
class MediaContentServiceImpl : MediaContentService, ServiceImpl<MediaContentMapper, MediaContentDo>() {
    @set:Autowired
    lateinit var mediaLikeService: MediaLikeService

    override fun get(id: Long): MediaContentDto? {
        return getById(id)?.toDto()
    }

    override fun query(dto: MessageQueryDto): List<MediaContentVo> {
        return baseMapper.query(dto)
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

    override fun random(count: Long): List<MediaContentVo> {
        return baseMapper.random(count)
    }

    override fun list(dto: MediaListOption): IPage<MediaContentVo> {
        return baseMapper.page(Page(dto.page, dto.size), dto)
    }

    @Transactional(rollbackFor = [Exception::class, Error::class])
    override fun like(mediaId: Long) {
        val success = mediaLikeService.save(MediaLikeDo(userId = currentUser!!.user.id, mediaId = mediaId))
        if (!success) exists("该内容的点赞")
        updateBy {
            eq(MediaContentDo::id, mediaId)
            setSql("like_count = like_count + 1")
        }
    }

    @Transactional(rollbackFor = [Exception::class, Error::class])
    override fun undoLike(mediaId: Long) {
        val success = mediaLikeService.removeBy {
            eq(MediaLikeDo::userId, currentUser!!.user.id)
            eq(MediaLikeDo::mediaId, mediaId)
        }
        if (!success) notFound("该内容的点赞")
        updateBy {
            eq(MediaContentDo::id, mediaId)
            setSql("like_count = like_count - 1")
        }
    }
}
package top.e404.media.module.media.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.constraints.Max
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import top.e404.media.module.common.advice.LogAccess
import top.e404.media.module.common.annotation.RequirePerm
import top.e404.media.module.common.entity.BaseResp
import top.e404.media.module.common.entity.dto.page.PageInfo
import top.e404.media.module.common.entity.toResp
import top.e404.media.module.common.enums.SysPerm
import top.e404.media.module.media.entity.MediaContentDto
import top.e404.media.module.media.entity.MediaContentVo
import top.e404.media.module.media.entity.MessageQueryDto
import top.e404.media.module.media.entity.comment.MessageComment
import top.e404.media.module.media.entity.comment.MessageCommentDto
import top.e404.media.module.media.service.MediaContentService

@RestController
@RequestMapping("/api/media")
@Tag(name = "媒体信息接口")
class MediaContentController {
    @set:Autowired
    lateinit var mediaContentService: MediaContentService

    // @LogAccess
    // @GetMapping("/{id}")
    // @RequirePerm("message:get")
    // @Operation(summary = "通过id获取message")
    // fun getById(@PathVariable id: String) = mediaContentService.getById(id) ?: throw NotFoundException

    @LogAccess
    @PostMapping("/query")
    @RequirePerm(SysPerm.MEDIA_QUERY)
    @Operation(summary = "通过高级查询获取message")
    fun queryMessage(@RequestBody dto: MessageQueryDto): BaseResp<List<MediaContentVo>> {
        return mediaContentService.query(dto).toResp()
    }

    @LogAccess
    @GetMapping("/random")
    @RequirePerm(SysPerm.MEDIA_RANDOM)
    @Operation(summary = "随机获取message")
    fun listMessage(@Validated @Max(20) count: Long): BaseResp<List<MediaContentVo>> {
        return mediaContentService.random(count).toResp()
    }

    @LogAccess
    @PostMapping("")
    @RequirePerm(SysPerm.MEDIA_UPLOAD)
    @Operation(summary = "上传media content", description = "需要先上传二进制文件")
    fun saveMessage(@RequestBody dto: MediaContentDto): BaseResp<MediaContentDto> {
        return mediaContentService.save(dto).toResp()
    }

    @LogAccess
    @PutMapping("")
    @RequirePerm(SysPerm.MEDIA_EDIT)
    @Operation(summary = "更新message", description = "上传message前需要先上传二进制文件")
    fun updateMessage(@RequestBody dto: MediaContentDto): BaseResp<Unit> {
        return mediaContentService.update(dto).toResp()
    }

    // 点赞

    @LogAccess
    @PostMapping("/{mediaId}/like")
    @RequirePerm(SysPerm.MEDIA_COMMENT_LIKE)
    @Operation(summary = "喜欢")
    fun like(@PathVariable mediaId: Long): BaseResp<Unit> {
        return mediaContentService.like(mediaId).toResp()
    }

    @LogAccess
    @PostMapping("/{mediaId}/undoLike")
    @RequirePerm(SysPerm.MEDIA_COMMENT_LIKE)
    @Operation(summary = "取消喜欢")
    fun undoLike(@PathVariable mediaId: Long): BaseResp<Unit> {
        return mediaContentService.undoLike(mediaId).toResp()
    }

    // 评论

    @LogAccess
    @GetMapping("/{id}/comment")
    @RequirePerm(SysPerm.MEDIA_COMMENT_VIEW)
    @Operation(summary = "获取评论")
    fun listComment(
        @PathVariable id: String,
        page: PageInfo
    ): BaseResp<List<MessageComment>> {
        return mediaContentService.listComment(id, page).toResp()
    }

    @LogAccess
    @PutMapping("/{id}/comment")
    @RequirePerm(SysPerm.MEDIA_COMMENT_POST)
    @Operation(summary = "发送评论")
    fun postComment(
        @PathVariable id: String,
        @RequestBody dto: MessageCommentDto
    ): BaseResp<MessageComment> {
        return mediaContentService.postComment(id, dto).toResp()
    }
}
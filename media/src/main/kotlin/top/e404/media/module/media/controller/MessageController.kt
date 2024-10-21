package top.e404.media.module.media.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.constraints.Max
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import top.e404.media.module.common.advice.LogAccess
import top.e404.media.module.common.annontation.RequirePerm
import top.e404.media.module.common.entity.page.PageInfo
import top.e404.media.module.common.entity.toResp
import top.e404.media.module.common.enums.SysPerm
import top.e404.media.module.media.entity.MessageDto
import top.e404.media.module.media.entity.MessageQueryDto
import top.e404.media.module.media.entity.comment.MessageCommentDto
import top.e404.media.module.media.service.MessageService

@RestController
@RequestMapping("/api/message")
@Tag(name = "消息接口")
class MessageController {
    @set:Autowired
    lateinit var messageService: MessageService

    // @LogAccess
    // @GetMapping("/{id}")
    // @RequirePerm("message:get")
    // @Operation(summary = "通过id获取message")
    // fun getById(@PathVariable id: String) = messageService.getById(id) ?: throw NotFoundException

    @LogAccess
    @PostMapping("/query")
    @RequirePerm(SysPerm.MESSAGE_QUERY)
    @Operation(summary = "通过高级查询获取message")
    fun queryMessage(@RequestBody dto: MessageQueryDto) = messageService.query(dto).toResp()

    @LogAccess
    @GetMapping("/random")
    @RequirePerm(SysPerm.MESSAGE_RANDOM)
    @Operation(summary = "随机获取message")
    fun listMessage(@Validated @Max(20) count: Long) = messageService.random(count).toResp()

    @LogAccess
    @PutMapping("")
    @RequirePerm(SysPerm.MESSAGE_UPLOAD)
    @Operation(summary = "上传message", description = "上传message前需要先上传二进制文件")
    fun saveMessage(@RequestBody dto: MessageDto) = messageService.save(dto).toResp()

    // 评论

    @LogAccess
    @GetMapping("/{id}/comment")
    @RequirePerm(SysPerm.MESSAGE_COMMENT_VIEW)
    @Operation(summary = "获取评论")
    fun listComment(
        @PathVariable id: String,
        page: PageInfo
    ) = messageService.listComment(id, page).toResp()

    @LogAccess
    @PutMapping("/{id}/comment")
    @RequirePerm(SysPerm.MESSAGE_COMMENT_POST)
    @Operation(summary = "发送评论")
    fun postComment(
        @PathVariable id: String,
        @RequestBody dto: MessageCommentDto
    ) = messageService.postComment(id, dto).toResp()
}
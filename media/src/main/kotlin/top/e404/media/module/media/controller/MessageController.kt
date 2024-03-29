package top.e404.media.module.media.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import top.e404.media.module.common.advice.LogAccess
import top.e404.media.module.common.annontation.RequirePerm
import top.e404.media.module.media.entity.MessageDto
import top.e404.media.module.media.entity.MessageQueryDto
import top.e404.media.module.media.entity.comment.MessageCommentDto
import top.e404.media.module.common.entity.query.PageRequest
import top.e404.media.module.media.service.MessageService

@RestController
@RequestMapping("/api/message")
@Tag(name = "消息接口")
class MessageController {
    @set:Autowired
    lateinit var messageService: MessageService

    @LogAccess
    @GetMapping("/{id}")
    @RequirePerm("message:get")
    @Operation(summary = "通过id获取message")
    fun getById(@PathVariable id: String) = ResponseEntity.ok(messageService.getById(id))

    @LogAccess
    @GetMapping
    @RequirePerm("message:query")
    @Operation(summary = "通过高级查询获取message")
    fun query(dto: MessageQueryDto) = ResponseEntity.ok(messageService.query(dto))

    @LogAccess
    @PutMapping
    @RequirePerm("message:upload")
    @Operation(summary = "上传message", description = "上传message前需要先上传二进制文件")
    fun save(dto: MessageDto) = ResponseEntity.ok(messageService.save(dto))

    // 评论

    @LogAccess
    @GetMapping("/{id}/comment")
    @Operation(summary = "获取评论")
    fun listComment(
        @PathVariable id: String,
        page: PageRequest
    ) = ResponseEntity.ok(messageService.getComment(id, page))

    @LogAccess
    @PutMapping("/{id}/comment")
    @RequirePerm("messageComment:upload")
    @Operation(summary = "发送评论")
    fun addComment(
        @PathVariable id: String,
        dto: MessageCommentDto
    ) = ResponseEntity.ok(messageService.addComment(id, dto))
}
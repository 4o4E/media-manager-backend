package top.e404.media.module.media.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import top.e404.media.module.common.advice.LogAccess
import top.e404.media.module.common.annontation.RequirePerm
import top.e404.media.module.common.entity.page.PageInfo
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
    @PostMapping("")
    @RequirePerm("message:query")
    @Operation(summary = "通过高级查询获取message")
    fun query(@RequestBody dto: MessageQueryDto) = messageService.query(dto)

    @LogAccess
    @GetMapping("/random")
    @RequirePerm("message:query")
    @Operation(summary = "随机获取message")
    fun page(count: Long) = messageService.random(count)

    @LogAccess
    @PutMapping("")
    @RequirePerm("message:upload")
    @Operation(summary = "上传message", description = "上传message前需要先上传二进制文件")
    fun save(@RequestBody dto: MessageDto) = messageService.save(dto)

    // 评论

    @LogAccess
    @GetMapping("/{id}/comment")
    @Operation(summary = "获取评论")
    fun listComment(
        @PathVariable id: String,
        page: PageInfo
    ) = messageService.getComment(id, page)

    @LogAccess
    @PutMapping("/{id}/comment")
    @RequirePerm("messageComment:upload")
    @Operation(summary = "发送评论")
    fun addComment(
        @PathVariable id: String,
        @RequestBody dto: MessageCommentDto
    ) = messageService.addComment(id, dto)
}
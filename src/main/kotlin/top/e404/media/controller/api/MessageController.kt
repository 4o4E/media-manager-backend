package top.e404.media.controller.api

import io.swagger.v3.oas.annotations.Operation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import top.e404.media.entity.message.MessageDto
import top.e404.media.service.MessageService

@RestController
@RequestMapping("/api/message")
class MessageController {
    @set:Autowired
    lateinit var messageService: MessageService

    @GetMapping("/{id}")
    @Operation(summary = "通过id获取message")
    fun getById(@PathVariable id: String) = ResponseEntity.ok(messageService.getById(id))

    @PutMapping
    @Operation(summary = "上传message", description = "上传message前需要先上传二进制文件")
    fun save(dto: MessageDto) = ResponseEntity.ok(messageService.save(dto))
}
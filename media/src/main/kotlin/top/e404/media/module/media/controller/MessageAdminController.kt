package top.e404.media.module.media.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import top.e404.media.module.common.advice.LogAccess
import top.e404.media.module.common.annontation.RequirePerm
import top.e404.media.module.common.entity.toResp
import top.e404.media.module.common.enums.SysPerm
import top.e404.media.module.media.entity.MediaListOption
import top.e404.media.module.media.service.MessageService

@RestController
@RequestMapping("/api/admin/message")
@Tag(name = "消息管理接口")
class MessageAdminController {
    @set:Autowired
    lateinit var messageService: MessageService

    @LogAccess
    @PostMapping("")
    @RequirePerm(SysPerm.MESSAGE_QUERY)
    @Operation(summary = "通过高级查询获取message")
    fun listMessage(
        @RequestBody dto: MediaListOption
    ) = messageService.list(dto).toResp()

}
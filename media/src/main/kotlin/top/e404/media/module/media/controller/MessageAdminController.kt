package top.e404.media.module.media.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import top.e404.media.module.common.advice.LogAccess
import top.e404.media.module.common.annotation.RequirePerm
import top.e404.media.module.common.entity.BaseResp
import top.e404.media.module.common.entity.dto.page.PageResult
import top.e404.media.module.common.enums.SysPerm
import top.e404.media.module.common.util.convert
import top.e404.media.module.common.util.toPageResp
import top.e404.media.module.media.entity.MediaContentDto
import top.e404.media.module.media.entity.MediaListOption
import top.e404.media.module.media.service.MediaContentService

@RestController
@RequestMapping("/api/admin/media")
@Tag(name = "消息管理接口")
class MessageAdminController {
    @set:Autowired
    lateinit var messageService: MediaContentService

    @LogAccess
    @PostMapping("")
    @RequirePerm(SysPerm.MEDIA_LIST)
    @Operation(summary = "通过高级查询获取media")
    fun page(
        @RequestBody dto: MediaListOption
    ): BaseResp<PageResult<MediaContentDto, Void>> {
        return messageService.list(dto).toPageResp {
            it.convert(MediaContentDto::class)
        }
    }

}

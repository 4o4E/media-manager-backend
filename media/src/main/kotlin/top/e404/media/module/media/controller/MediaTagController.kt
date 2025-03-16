package top.e404.media.module.media.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import top.e404.media.module.common.advice.LogAccess
import top.e404.media.module.common.annotation.RequirePerm
import top.e404.media.module.common.entity.BaseResp
import top.e404.media.module.common.entity.SaveValid
import top.e404.media.module.common.entity.UpdateValid
import top.e404.media.module.common.entity.dto.page.PageInfo
import top.e404.media.module.common.entity.dto.page.PageResult
import top.e404.media.module.common.entity.toResp
import top.e404.media.module.common.enums.SysPerm
import top.e404.media.module.common.util.convert
import top.e404.media.module.common.util.toMybatisPage
import top.e404.media.module.common.util.toPageResp
import top.e404.media.module.media.entity.MediaTagDto
import top.e404.media.module.media.entity.MediaTagVo
import top.e404.media.module.media.service.MediaTagService

@RestController
@RequestMapping("/api/tags")
class MediaTagController {
    @set:Autowired
    lateinit var mediaTagService: MediaTagService

    @LogAccess
    @GetMapping("/all")
    fun listTags(
        key: String?,
        lastUpdated: Long?
    ) = mediaTagService.listTags(key, lastUpdated).toResp()

    @LogAccess
    @GetMapping("")
    fun page(pageInfo: PageInfo): BaseResp<PageResult<Any, Void>> {
        return mediaTagService.page(pageInfo.toMybatisPage()).toPageResp {
            it.convert(MediaTagVo::class)
        }
    }

    @LogAccess
    @PostMapping("")
    @RequirePerm(SysPerm.TAG_VIEW)
    fun createTag(
        @RequestBody @Validated(SaveValid::class) dto: MediaTagDto
    ) = mediaTagService.createTag(dto).toResp()

    @LogAccess
    @PatchMapping("")
    @RequirePerm(SysPerm.TAG_EDIT)
    fun editTag(
        @RequestBody @Validated(UpdateValid::class) dto: MediaTagDto
    ) = mediaTagService.editTag(dto).toResp()

    @LogAccess
    @DeleteMapping("/{tagId}")
    @RequirePerm(SysPerm.TAG_EDIT)
    fun deleteTag(
        @PathVariable tagId: Long,
    ) = mediaTagService.remove(tagId).toResp()
}
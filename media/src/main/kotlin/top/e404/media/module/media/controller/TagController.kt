package top.e404.media.module.media.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import top.e404.media.module.common.annontation.RequirePerm
import top.e404.media.module.common.entity.toResp
import top.e404.media.module.common.enums.SysPerm
import top.e404.media.module.media.entity.CreateTagDto
import top.e404.media.module.media.entity.TagDto
import top.e404.media.module.media.service.TagService

@RestController
@RequestMapping("/api/tags")
class TagController {
    @set:Autowired
    lateinit var tagService: TagService

    @GetMapping
    fun listTags(
        key: String?,
        lastUpdated: Long?
    ) = tagService.listTags(key, lastUpdated).toResp()

    @PostMapping
    @RequirePerm(SysPerm.TAG_VIEW)
    fun createTag(
        @RequestBody dto: CreateTagDto
    ) = tagService.createTag(dto.name, dto.description, dto.alias).toResp()

    @PutMapping("/{tagId}")
    @RequirePerm(SysPerm.TAG_EDIT)
    fun editTag(
        @PathVariable tagId: Long,
        @RequestBody dto: TagDto
    ) = tagService.editTag(tagId, dto.name, dto.description).toResp()

    @DeleteMapping("/{tagId}")
    @RequirePerm(SysPerm.TAG_EDIT)
    fun deleteTag(
        @PathVariable tagId: Long,
        @RequestBody dto: TagDto
    ) = tagService.editTag(tagId, dto.name, dto.description).toResp()

    @PostMapping("/{tagId}/alias/{alias}")
    @RequirePerm(SysPerm.TAG_EDIT)
    fun addTagAlias(
        @PathVariable tagId: Long,
        @PathVariable alias: String
    ) = tagService.addTagAlias(tagId, alias).toResp()

    @DeleteMapping("/{tagId}/alias/{alias}")
    @RequirePerm(SysPerm.TAG_EDIT)
    fun removeTagAlias(
        @PathVariable tagId: Long,
        @PathVariable alias: String
    ) = tagService.removeTagAlias(tagId, alias).toResp()
}
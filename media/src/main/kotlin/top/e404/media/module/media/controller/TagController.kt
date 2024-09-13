package top.e404.media.module.media.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import top.e404.media.module.common.annontation.RequirePerm
import top.e404.media.module.common.entity.toResp
import top.e404.media.module.common.enums.SysPerm
import top.e404.media.module.media.service.TagService

@RestController
@RequestMapping("/api/tags")
class TagController {
    @set:Autowired
    lateinit var tagService: TagService

    @GetMapping
    fun listTags(key: String?, lastUpdated: Long?) = tagService.listTags(key, lastUpdated).toResp()

    @PostMapping
    @RequirePerm(SysPerm.TAG_VIEW)
    fun createTag(name: String, description: String) = tagService.createTag(name, description).toResp()

    @PutMapping("/{tagId}")
    @RequirePerm(SysPerm.TAG_EDIT)
    fun editTag(@PathVariable tagId: Long, name: String, description: String) =
        tagService.editTag(tagId, name, description).toResp()

    @DeleteMapping("/{tagId}")
    @RequirePerm(SysPerm.TAG_EDIT)
    fun deleteTag(@PathVariable tagId: Long, name: String, description: String) =
        tagService.editTag(tagId, name, description).toResp()
}
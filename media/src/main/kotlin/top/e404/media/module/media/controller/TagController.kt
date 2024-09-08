package top.e404.media.module.media.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import top.e404.media.module.common.entity.toResp
import top.e404.media.module.media.service.TagService

@RestController
@RequestMapping("/api/tags")
class TagController {
    @set:Autowired
    lateinit var tagService: TagService

    @GetMapping
    fun listTags(key: String?) = tagService.listTags(key).toResp()

    @PostMapping
    fun createTag(name: String, description: String) = tagService.createTag(name, description).toResp()

    @PutMapping("/{tagId}")
    fun editTag(@PathVariable tagId: Long, name: String, description: String) =
        tagService.editTag(tagId, name, description).toResp()
}
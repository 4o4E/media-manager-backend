package top.e404.media.module.common.controller.system

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import top.e404.media.module.common.advice.LogAccess
import top.e404.media.module.common.entity.BaseResp
import top.e404.media.module.common.entity.database.DictVo
import top.e404.media.module.common.entity.dto.page.PageQuery
import top.e404.media.module.common.entity.dto.page.PageResult
import top.e404.media.module.common.entity.toResp
import top.e404.media.module.common.service.system.DictService

@Validated
@RestController
@RequestMapping("/api/dict")
@Tag(name = "字典")
class DictController {
    @set:Autowired
    lateinit var dictService: DictService

    @LogAccess
    @GetMapping("")
    @Operation(summary = "获取字典列表")
    fun list(@RequestBody query: PageQuery<String>): BaseResp<PageResult<DictVo, Void>> {
        return dictService
            .listByType(query.extra, query.page)
            .toResp()
    }

    @LogAccess
    @GetMapping("/{type}")
    @Operation(summary = "查询字典")
    fun get(@PathVariable type: String, values: String): BaseResp<List<DictVo>> {
        return dictService
            .get(type, values.split(","))
            .toResp()
    }
}
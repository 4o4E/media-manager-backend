package top.e404.media.module.common.controller.admin

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import top.e404.media.module.common.advice.LogAccess
import top.e404.media.module.common.annotation.RequirePerm
import top.e404.media.module.common.entity.BaseResp
import top.e404.media.module.common.entity.SaveValid
import top.e404.media.module.common.entity.UpdateValid
import top.e404.media.module.common.entity.database.*
import top.e404.media.module.common.entity.dto.page.PageInfo
import top.e404.media.module.common.entity.dto.page.PageResult
import top.e404.media.module.common.entity.toResp
import top.e404.media.module.common.enums.SysPerm
import top.e404.media.module.common.service.system.DictService
import top.e404.media.module.common.util.convert
import top.e404.media.module.common.util.toMybatisPage

@Validated
@RestController
@RequestMapping("/api/admin/dict")
@Tag(name = "字典")
class DictAdminController {
    @set:Autowired
    lateinit var dictService: DictService
    @set:Autowired
    lateinit var json: Json

    @RequirePerm(SysPerm.DICT_MANAGE)
    @LogAccess
    @GetMapping("")
    @Operation(summary = "获取字典列表")
    fun list(page: PageInfo, query: DictQuery): BaseResp<PageResult<DictCollection, Void>> {
        return dictService
            .listAll(page.toMybatisPage(), query)
            .also {
                for (collection in it.data) {
                    collection.list = json.decodeFromString(ListSerializer(DictVo.serializer()), collection.values!!)
                    collection.values = null
                }
            }
            .toResp()
    }

    @RequirePerm(SysPerm.DICT_MANAGE)
    @LogAccess
    @PostMapping("")
    @Operation(summary = "新增字典条目")
    fun save(@RequestBody @Validated(SaveValid::class) dict: DictDto): BaseResp<Boolean> {
        return dictService
            .save(dict.convert(DictDo::class))
            .toResp()
    }

    @RequirePerm(SysPerm.DICT_MANAGE)
    @LogAccess
    @PatchMapping("")
    @Operation(summary = "更新字典条目")
    fun update(@RequestBody @Validated(UpdateValid::class) dict: DictDto): BaseResp<Boolean> {
        return dictService
            .updateById(dict.convert(DictDo::class))
            .toResp()
    }

    @RequirePerm(SysPerm.DICT_MANAGE)
    @LogAccess
    @DeleteMapping("/{id}")
    @Operation(summary = "删除字典条目")
    fun remove(@PathVariable id: Long): BaseResp<Boolean> {
        return dictService
            .removeById(id)
            .toResp()
    }
}
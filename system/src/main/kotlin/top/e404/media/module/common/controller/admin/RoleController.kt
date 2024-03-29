package top.e404.media.module.common.controller.admin

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import top.e404.media.module.common.advice.LogAccess
import top.e404.media.module.common.annontation.RequirePerm
import top.e404.media.module.common.entity.UpdateValid
import top.e404.media.module.common.entity.auth.*
import top.e404.media.module.common.exception.CustomMessageException
import top.e404.media.module.common.service.database.RolePermService
import top.e404.media.module.common.service.database.RoleService
import top.e404.media.module.common.util.copyAs
import top.e404.media.module.common.util.query

@Validated
@RestController
@RequestMapping("/api/admin/roles")
@Tag(name = "角色接口")
class RoleController {
    @set:Autowired
    lateinit var roleService: RoleService

    @set:Autowired
    lateinit var rolePermService: RolePermService

    @LogAccess
    @GetMapping("/{id}")
    @RequirePerm("role:get")
    @Operation(summary = "通过id获取角色信息")
    fun getRoleById(@PathVariable id: Long): ResponseEntity<RoleDo> {
        return ResponseEntity.ok(roleService.getById(id))
    }

    @LogAccess
    @GetMapping("/{id}/perms")
    @RequirePerm("role:get")
    @Operation(summary = "通过id获取角色所有权限节点")
    fun getPermById(@PathVariable id: Long): ResponseEntity<List<String>> {
        return ResponseEntity.ok(roleService.getPermByRoleId(id))
    }

    @LogAccess
    @PostMapping("/{id}/perms/{perm}")
    @RequirePerm("role_perm:edit")
    @Operation(summary = "通过id为角色添加权限节点")
    fun addPermById(@PathVariable id: Long, @PathVariable perm: String): ResponseEntity<RolePermVo> {
        val rolePermDo = RolePermDo(id, perm)
        if (!rolePermService.save(rolePermDo)) throw CustomMessageException("没有对应的角色", HttpStatus.NOT_FOUND)
        return ResponseEntity.ok(rolePermDo.copyAs(RolePermVo::class))
    }

    @LogAccess
    @DeleteMapping("/{id}/perms/{perm}")
    @RequirePerm("role_perm:edit")
    @Operation(summary = "通过id为角色移除权限节点")
    fun removePermById(@PathVariable id: Long, @PathVariable perm: String): ResponseEntity<RolePermVo> {
        val rolePermDo = RolePermDo(id, perm)
        if (!rolePermService.remove(
                query {
                    eq(RolePermDo::role, id).eq(RolePermDo::perm, perm)
                }
            )
        ) throw CustomMessageException("没有对应的角色权限", HttpStatus.NOT_FOUND)
        return ResponseEntity.ok(rolePermDo.copyAs(RolePermVo::class))
    }

    @LogAccess
    @PostMapping
    @RequirePerm("role:save")
    @Operation(summary = "创建角色")
    fun save(@RequestBody @Validated dto: RoleDto): ResponseEntity<RoleVo> {
        val roleDo = dto.copyAs(RoleDo::class)
        roleService.save(roleDo)
        return ResponseEntity.ok(roleDo.copyAs(RoleVo::class))
    }

    @LogAccess
    @DeleteMapping("/{id}")
    @RequirePerm("role:remove")
    @Operation(summary = "删除角色")
    fun remove(@PathVariable id: Long): ResponseEntity<Void> {
        if (!roleService.remove(id)) throw CustomMessageException("没有对应的角色", HttpStatus.NOT_FOUND)
        return ResponseEntity.ok().build()
    }

    @LogAccess
    @PatchMapping
    @RequirePerm("role:update")
    @Operation(summary = "更新角色数据")
    fun update(@RequestBody @Validated(UpdateValid::class) dto: RoleDto): RoleVo {
        val roleDo = dto.copyAs(RoleDo::class)
        roleService.updateById(roleDo)
        return roleDo.copyAs(RoleVo::class)
    }
}
package top.e404.media.module.common.controller.admin

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import top.e404.media.module.common.advice.LogAccess
import top.e404.media.module.common.annontation.RequirePerm
import top.e404.media.module.common.entity.BaseResp
import top.e404.media.module.common.entity.UpdateValid
import top.e404.media.module.common.entity.database.RoleDo
import top.e404.media.module.common.entity.database.RoleDto
import top.e404.media.module.common.entity.database.RoleVo
import top.e404.media.module.common.entity.dto.page.PageInfo
import top.e404.media.module.common.entity.dto.role.AddRoleDto
import top.e404.media.module.common.entity.resp
import top.e404.media.module.common.entity.toResp
import top.e404.media.module.common.enums.SysPerm
import top.e404.media.module.common.exception.notFound
import top.e404.media.module.common.service.database.RoleService
import top.e404.media.module.common.util.*

@Validated
@RestController
@RequestMapping("/api/admin/roles")
@Tag(name = "角色接口")
class RoleController {
    @set:Autowired
    lateinit var roleService: RoleService

    @LogAccess
    @GetMapping("/allPerm")
    @RequirePerm()
    @Operation(summary = "获取所有权限节点")
    fun allPerm() = resp(roleService.allPerm())

    @LogAccess
    @GetMapping("")
    @RequirePerm(SysPerm.ROLE_VIEW)
    @Operation(summary = "分页获取角色")
    fun listRole(pageInfo: PageInfo) = roleService.page(pageInfo.toMybatisPage()).toPageResult {
        it.copyAs(RoleVo::class)
    }

    @LogAccess
    @GetMapping("/all")
    @RequirePerm(SysPerm.ROLE_VIEW)
    @Operation(summary = "获取所有角色")
    fun listAllRole() = roleService.list().copyAsList(RoleVo::class).toResp()

    @LogAccess
    @GetMapping("/{id}")
    @RequirePerm(SysPerm.ROLE_VIEW)
    @Operation(summary = "通过id获取角色信息")
    fun getRoleById(@PathVariable id: Long) = roleService.getById(id).copyAs(RoleVo::class).toResp()

    @LogAccess
    @GetMapping("/{id}/perms")
    @RequirePerm(SysPerm.ROLE_PERM_VIEW)
    @Operation(summary = "通过id获取角色所有权限节点")
    fun getPermById(@PathVariable id: Long) = roleService.getRolePerms(id).toResp()

    @LogAccess
    @PostMapping("/{id}/perms/{perm}")
    @RequirePerm(SysPerm.ROLE_PERM_EDIT)
    @Operation(summary = "通过id为角色添加权限节点")
    fun addPerm(@PathVariable id: Long, @PathVariable perm: String): BaseResp<Nothing> {
        val success = roleService.update(null, update {
            eq(RoleDo::id, id)
            setSql("perms = IF(array_position(perms, {0}) IS NULL, array_append(perms, {1}), perms)", perm, perm)
        })
        if (!success) notFound("角色")
        return BaseResp.ok()
    }

    @LogAccess
    @DeleteMapping("/{id}/perms/{perm}")
    @RequirePerm(SysPerm.ROLE_PERM_EDIT)
    @Operation(summary = "通过id为角色移除权限节点")
    fun removePerm(@PathVariable id: Long, @PathVariable perm: String): BaseResp<Nothing> {
        val success = roleService.update(null, update {
            eq(RoleDo::id, id)
            setSql("perms = array_remove(perms, {0})", perm)
        })
        if (!success) notFound("角色")
        return BaseResp.ok()
    }

    @LogAccess
    @PostMapping("")
    @RequirePerm(SysPerm.ROLE_EDIT)
    @Operation(summary = "创建角色")
    fun addRole(@RequestBody @Validated dto: AddRoleDto): BaseResp<RoleVo> {
        val roleDo = dto.copyAs(RoleDo::class)
        roleService.save(roleDo)
        return roleDo.copyAs(RoleVo::class).toResp()
    }

    @LogAccess
    @DeleteMapping("/{id}")
    @RequirePerm(SysPerm.ROLE_EDIT)
    @Operation(summary = "删除角色")
    @Transactional
    fun removeRole(@PathVariable id: Long): BaseResp<Nothing> {
        if (!roleService.remove(id)) notFound("角色")
        return BaseResp.ok()
    }

    @LogAccess
    @PatchMapping("")
    @RequirePerm(SysPerm.ROLE_EDIT)
    @Operation(summary = "更新角色数据")
    fun updateRole(@RequestBody @Validated(UpdateValid::class) dto: RoleDto): BaseResp<RoleVo> {
        val roleDo = dto.copyAs(RoleDo::class)
        roleService.updateById(roleDo)
        return roleDo.copyAs(RoleVo::class).toResp()
    }
}
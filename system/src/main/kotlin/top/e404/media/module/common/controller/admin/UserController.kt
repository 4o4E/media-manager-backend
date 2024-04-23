package top.e404.media.module.common.controller.admin

import at.favre.lib.crypto.bcrypt.BCrypt
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import top.e404.media.module.common.advice.LogAccess
import top.e404.media.module.common.annontation.RequirePerm
import top.e404.media.module.common.entity.UpdateValid
import top.e404.media.module.common.entity.auth.*
import top.e404.media.module.common.entity.page.PageInfo
import top.e404.media.module.common.entity.user.AddUserDto
import top.e404.media.module.common.entity.user.UpdatePasswordDto
import top.e404.media.module.common.service.database.UserRoleService
import top.e404.media.module.common.service.database.UserService
import top.e404.media.module.common.util.*

@Validated
@RestController
@RequestMapping("/api/admin/users")
@Tag(name = "用户接口")
class UserController {
    @set:Autowired
    lateinit var userService: UserService

    @set:Autowired
    lateinit var userRoleService: UserRoleService

    @LogAccess
    @GetMapping("")
    @RequirePerm("user:get")
    @Operation(summary = "分页获取用户")
    fun list(pageInfo: PageInfo) = userService.page(pageInfo.toMybatisPage()).toPageResult {
        it.copyAs(UserVo::class)
    }

    @LogAccess
    @GetMapping("/{id}")
    @RequirePerm("user:get")
    @Operation(summary = "通过id获取用户信息")
    fun getUserById(@PathVariable id: Long) = userService.getById(id).copyAs(UserVo::class)

    @LogAccess
    @GetMapping("/{id}/roles")
    @RequirePerm("user:get")
    @Operation(summary = "通过id获取用户角色信息")
    fun getRoleById(@PathVariable id: Long) = userService.getRoleById(id).map { it.copyAs(RoleVo::class) }

    @LogAccess
    @PostMapping("/{userId}/roles/{roleId}")
    @RequirePerm("user:edit")
    @Operation(summary = "添加用户角色关联")
    fun putRoleForUser(
        @PathVariable userId: Long,
        @PathVariable roleId: Long
    ) {
        if (!userRoleService.save(UserRoleDo(userId, roleId))) error("该角色与用户已有分配")
    }

    @LogAccess
    @DeleteMapping("/{userId}/roles/{roleId}")
    @RequirePerm("user:edit")
    @Operation(summary = "解除用户角色关联")
    fun removeRoleForUser(
        @PathVariable userId: Long,
        @PathVariable roleId: Long
    ) {
        if (userId == 1L) error("不可删除")
        userRoleService.remove(query {
            eq(UserRoleDo::userId, userId)
            eq(UserRoleDo::roleId, roleId)
        })
    }

    @LogAccess
    @GetMapping("/{id}/perms")
    @RequirePerm("user:get")
    @Operation(summary = "通过id获取用户权限信息")
    fun getPermById(@PathVariable id: Long) = userService.getPermById(id).map { it.copyAs(RolePermVo::class) }

    @LogAccess
    @PostMapping("")
    @RequirePerm("user:save")
    @Operation(summary = "创建用户")
    fun save(@RequestBody @Validated dto: AddUserDto): UserVo {
        val userDo = dto.copyAs(UserDo::class)
        userService.save(userDo)
        return userDo.copyAs(UserVo::class)
    }

    @LogAccess
    @DeleteMapping("/{id}")
    @RequirePerm("user:remove")
    @Operation(summary = "删除用户")
    @Transactional
    fun remove(@PathVariable id: Long) {
        if (id == 1L) error("该用户不可删除")
        userRoleService.remove(query {
            eq(UserRoleDo::userId, id)
        })
        userService.removeById(id)
    }

    @LogAccess
    @PatchMapping("")
    @RequirePerm("user:update")
    @Operation(summary = "更新用户数据")
    fun update(@RequestBody @Validated(UpdateValid::class) dto: UserDto): UserVo {
        val userDo = dto.copyAs(UserDo::class)
        userService.updateById(userDo)
        return userDo.copyAs(UserVo::class)
    }

    @LogAccess
    @PostMapping("/password")
    @RequirePerm("user:update")
    @Operation(summary = "更新用户密码")
    fun updatePassword(@RequestBody req: UpdatePasswordDto) {
        userService.update(update {
            eq(UserDo::id, req.id)
            set(UserDo::password, String(BCrypt.withDefaults().hashToChar(12, req.password.toCharArray())))
        })
    }
}
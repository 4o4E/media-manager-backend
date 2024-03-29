package top.e404.media.module.common.controller.admin

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import top.e404.media.module.common.advice.LogAccess
import top.e404.media.module.common.annontation.RequirePerm
import top.e404.media.module.common.entity.UpdateValid
import top.e404.media.module.common.entity.auth.*
import top.e404.media.module.common.service.database.UserService
import top.e404.media.module.common.util.copyAs

@Validated
@RestController
@RequestMapping("/api/admin/users")
@Tag(name = "用户接口")
class UserController {
    @set:Autowired
    lateinit var userService: UserService

    @LogAccess
    @GetMapping("/{id}")
    @RequirePerm("user:get")
    @Operation(summary = "通过id获取用户信息")
    fun getUserById(@PathVariable id: Long): ResponseEntity<UserDo> {
        return ResponseEntity.ok(userService.getById(id))
    }

    @LogAccess
    @GetMapping("/{id}/roles")
    @RequirePerm("user:get")
    @Operation(summary = "通过id获取用户角色信息")
    fun getRoleById(@PathVariable id: Long): ResponseEntity<List<UserRoleVo>> {
        return ResponseEntity.ok(userService.getRoleById(id).map { it.copyAs(UserRoleVo::class) })
    }

    @LogAccess
    @PutMapping("/{userId}/roles/{roleId}")
    @RequirePerm("user:edit")
    @Operation(summary = "通过id修改用户角色信息")
    fun putRoleForUser(
        @PathVariable userId: Long,
        @PathVariable roleId: Long
    ): ResponseEntity<List<UserRoleVo>> {
        return ResponseEntity.ok(userService.getRoleById(userId).map { it.copyAs(UserRoleVo::class) })
    }

    @LogAccess
    @GetMapping("/{id}/perms")
    @RequirePerm("user:get")
    @Operation(summary = "通过id获取用户权限信息")
    fun getPermById(@PathVariable id: Long): ResponseEntity<List<RolePermVo>> {
        return ResponseEntity.ok(userService.getPermById(id).map { it.copyAs(RolePermVo::class) })
    }

    @LogAccess
    @PostMapping
    @RequirePerm("user:save")
    @Operation(summary = "创建用户")
    fun save(@RequestBody @Validated dto: UserDto): ResponseEntity<UserVo> {
        val userDo = dto.copyAs(UserDo::class)
        userService.save(userDo)
        return ResponseEntity.ok(userDo.copyAs(UserVo::class))
    }

    @LogAccess
    @DeleteMapping("/{id}")
    @RequirePerm("user:remove")
    @Operation(summary = "删除用户")
    fun remove(@PathVariable id: Long) {
        userService.removeById(id)
    }

    @LogAccess
    @PatchMapping
    @RequirePerm("user:update")
    @Operation(summary = "更新用户数据")
    fun update(@RequestBody @Validated(UpdateValid::class) dto: UserDto): UserVo {
        val userDo = dto.copyAs(UserDo::class)
        userService.updateById(userDo)
        return userDo.copyAs(UserVo::class)
    }
}
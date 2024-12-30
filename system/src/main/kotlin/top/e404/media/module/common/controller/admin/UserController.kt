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
import top.e404.media.module.common.entity.BaseResp
import top.e404.media.module.common.entity.UpdateValid
import top.e404.media.module.common.entity.database.RoleVo
import top.e404.media.module.common.entity.database.UserDo
import top.e404.media.module.common.entity.database.UserDto
import top.e404.media.module.common.entity.database.UserVo
import top.e404.media.module.common.entity.dto.page.PageInfo
import top.e404.media.module.common.entity.dto.user.AddUserDto
import top.e404.media.module.common.entity.dto.user.UpdatePasswordDto
import top.e404.media.module.common.entity.toResp
import top.e404.media.module.common.enums.SysPerm
import top.e404.media.module.common.exception.CommonFail
import top.e404.media.module.common.exception.fail
import top.e404.media.module.common.exception.notFound
import top.e404.media.module.common.service.database.UserService
import top.e404.media.module.common.util.copyAs
import top.e404.media.module.common.util.toMybatisPage
import top.e404.media.module.common.util.toPageResult
import top.e404.media.module.common.util.update

@Validated
@RestController
@RequestMapping("/api/admin/users")
@Tag(name = "用户接口")
class UserController {
    @set:Autowired
    lateinit var userService: UserService

    @LogAccess
    @GetMapping("")
    @RequirePerm(SysPerm.USER_VIEW)
    @Operation(summary = "分页获取用户")
    fun listUser(pageInfo: PageInfo) = userService.page(pageInfo.toMybatisPage()).toPageResult {
        it.copyAs(UserVo::class)
    }

    @LogAccess
    @GetMapping("/{id}")
    @RequirePerm(SysPerm.USER_VIEW)
    @Operation(summary = "通过id获取用户信息")
    fun getUserById(@PathVariable id: Long) = userService.getById(id).copyAs(UserVo::class).toResp()

    @LogAccess
    @GetMapping("/{id}/roles")
    @RequirePerm(SysPerm.USER_ROLE_VIEW)
    @Operation(summary = "通过id获取用户角色信息")
    fun getRoleById(@PathVariable id: Long) = userService.getUserRoles(id).map { it.copyAs(RoleVo::class) }.toResp()

    @LogAccess
    @PostMapping("/{userId}/roles/{roleId}")
    @RequirePerm(SysPerm.USER_ROLE_EDIT)
    @Operation(summary = "添加用户角色关联")
    fun addRoleForUser(
        @PathVariable userId: Long,
        @PathVariable roleId: Long
    ) {
        val success = userService.update(null, update {
            eq(UserDo::id, userId)
            setSql("roles = IF(array_position(roles, {0}) IS NULL, array_append(roles, {1}), roles)", userId, userId)
        })
        if (!success) notFound("用户")
    }

    @LogAccess
    @DeleteMapping("/{userId}/roles/{roleId}")
    @RequirePerm(SysPerm.USER_ROLE_EDIT)
    @Operation(summary = "解除用户角色关联")
    fun removeRoleForUser(
        @PathVariable userId: Long,
        @PathVariable roleId: Long
    ) {
        if (userId == 1L && roleId == 1L) fail(CommonFail.BAD_OPERATOR, "不可解除该用户和角色的绑定")
        val success = userService.update(null, update {
            eq(UserDo::id, userId)
            setSql("roles = array_remove(roles, {0})", userId)
        })
        if (!success) notFound("用户")
    }

    @LogAccess
    @GetMapping("/{id}/perms")
    @RequirePerm(SysPerm.USER_ROLE_VIEW, SysPerm.ROLE_PERM_VIEW)
    @Operation(summary = "通过id获取用户权限信息")
    fun getPermById(@PathVariable id: Long) = userService.getUserPerms(id).toResp()

    @LogAccess
    @PostMapping("")
    @RequirePerm(SysPerm.USER_EDIT)
    @Operation(summary = "创建用户")
    fun save(@RequestBody @Validated dto: AddUserDto): BaseResp<UserVo> {
        val userDo = dto.copyAs(UserDo::class)
        userService.save(userDo)
        return userDo.copyAs(UserVo::class).toResp()
    }

    @LogAccess
    @DeleteMapping("/{id}")
    @RequirePerm(SysPerm.USER_EDIT)
    @Operation(summary = "删除用户")
    @Transactional
    fun remove(@PathVariable id: Long) {
        if (id == 1L) fail(CommonFail.BAD_OPERATOR, "该用户不可删除")
        userService.removeById(id).toResp()
    }

    @LogAccess
    @PatchMapping("")
    @RequirePerm(SysPerm.USER_EDIT)
    @Operation(summary = "更新用户数据")
    fun update(@RequestBody @Validated(UpdateValid::class) dto: UserDto): BaseResp<UserVo> {
        val userDo = dto.copyAs(UserDo::class)
        userService.updateById(userDo)
        return userDo.copyAs(UserVo::class).toResp()
    }

    @LogAccess
    @PostMapping("/password")
    @RequirePerm(SysPerm.USER_EDIT)
    @Operation(summary = "更新用户密码")
    fun updatePassword(@RequestBody req: UpdatePasswordDto) {
        userService.update(update {
            eq(UserDo::id, req.id)
            set(UserDo::password, String(BCrypt.withDefaults().hashToChar(12, req.password.toCharArray())))
        })
    }
}
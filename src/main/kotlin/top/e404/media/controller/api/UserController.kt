package top.e404.media.controller.api

import io.swagger.v3.oas.annotations.Operation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import top.e404.media.annontation.CheckPerm
import top.e404.media.entity.auth.*
import top.e404.media.service.UserService


@Validated
@RestController
@RequestMapping("/api/user")
class UserController {
    @set:Autowired
    lateinit var userService: UserService

    @GetMapping("/{id}")
    @CheckPerm("user:get")
    @Operation(summary = "通过id获取用户信息")
    fun getById(@PathVariable id: Long): UserDo? {
        return userService.getById(id)
    }

    @PostMapping
    @CheckPerm("user:save")
    @Operation(summary = "创建用户")
    fun save(@RequestBody @Validated dto: UserDto): UserVo {
        val userDo = dto.toDo()
        userService.save(userDo)
        return userDo.toVo()
    }

    @DeleteMapping("/{id}")
    @CheckPerm("user:remove")
    @Operation(summary = "删除用户")
    fun remove(@PathVariable id: Long) {
        userService.removeById(id)
    }

    @PatchMapping
    @CheckPerm("user:update")
    @Operation(summary = "更新用户数据")
    fun update(@RequestBody @Validated dto: UserDto): UserVo {
        val userDo = dto.toDo()
        userService.updateById(userDo)
        return userDo.toVo()
    }
}
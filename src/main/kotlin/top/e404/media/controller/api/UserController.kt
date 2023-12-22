package top.e404.media.controller.api

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import top.e404.media.entity.auth.*
import top.e404.media.service.UserService


@Validated
@RestController
@RequestMapping("/api/user")
class UserController {
    @set:Autowired
    lateinit var userService: UserService

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): UserDo? {
        return userService.getById(id)
    }

    @PostMapping
    fun save(@RequestBody @Validated dto: UserDto): UserVo {
        val userDo = dto.toDo()
        userService.save(userDo)
        return userDo.toVo()
    }

    @DeleteMapping("/{id}")
    fun remove(@PathVariable id: Long) {
        userService.removeById(id)
    }

    @PatchMapping
    fun update(@RequestBody @Validated dto: UserDto): UserVo {
        val userDo = dto.toDo()
        userService.updateById(userDo)
        return userDo.toVo()
    }
}
package top.e404.media.module.common.controller.system

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import top.e404.media.module.common.advice.LogAccess
import top.e404.media.module.common.entity.auth.*
import top.e404.media.module.common.entity.toResp
import top.e404.media.module.common.service.system.AuthService

@Validated
@RestController
@RequestMapping("/api/auth")
@Tag(name = "登录相关接口")
class AuthController {
    @set:Autowired
    lateinit var authService: AuthService

    @LogAccess
    @PostMapping("/login")
    @Operation(summary = "登录")
    fun login(@RequestBody dto: LoginDto) = authService.login(dto).toResp()

    @LogAccess
    @PostMapping("/register")
    @Operation(summary = "注册")
    fun register(@RequestBody dto: RegisterDto) = authService.register(dto).toResp()

    @LogAccess
    @PostMapping("/forgetPassword")
    @Operation(summary = "用户发起忘记密码申请")
    fun forgetPassword(@RequestBody dto: ForgetPasswordDto) = ResponseEntity.status(
        if (authService.forgetPassword(dto)) HttpStatus.OK
        else HttpStatus.NOT_FOUND
    ).build<Unit>()

    @LogAccess
    @PostMapping("/resetPassword")
    @Operation(summary = "重置密码", description = "发送到短信/邮箱的重置链接, 链接走前端解析之后再发送请求到后端")
    fun forgetPassword(@RequestBody dto: ResetPasswordDto) {
        authService.resetPassword(dto)
    }

    @LogAccess
    @PostMapping("/setPassword")
    @Operation(summary = "修改密码")
    fun setPassword(@RequestBody dto: SetPasswordDto) {
        authService.setPassword(dto)
    }
}
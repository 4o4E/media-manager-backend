package top.e404.media.controller.system

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import top.e404.media.entity.auth.ForgetPasswordDto
import top.e404.media.entity.auth.LoginDto
import top.e404.media.entity.auth.RegisterDto
import top.e404.media.entity.auth.ResetPasswordDto
import top.e404.media.service.system.AuthService

@Validated
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "用户登录相关接口")
class AuthController {
    @set:Autowired
    lateinit var authService: AuthService

    @PostMapping("/login")
    @Operation(summary = "登录")
    fun login(dto: LoginDto) = ResponseEntity.ok(authService.login(dto))

    @PostMapping("/register")
    @Operation(summary = "注册")
    fun register(dto: RegisterDto) = ResponseEntity.ok(authService.register(dto))

    @PostMapping("/forgetPassword")
    @Operation(summary = "忘记密码")
    fun forgetPassword(dto: ForgetPasswordDto) = ResponseEntity.status(
        if (authService.forgetPassword(dto)) HttpStatus.OK
        else HttpStatus.NOT_FOUND
    ).build<Unit>()

    @PostMapping("/resetPassword")
    @Operation(summary = "重置密码")
    fun forgetPassword(dto: ResetPasswordDto): ResponseEntity<Unit> {
        authService.resetPassword(dto)
        return ResponseEntity.ok(null)
    }
}
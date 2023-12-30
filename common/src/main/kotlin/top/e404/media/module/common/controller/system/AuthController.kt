package top.e404.media.module.common.controller.system

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import top.e404.media.module.common.entity.auth.*
import top.e404.media.module.common.service.system.AuthService
import top.e404.media.module.common.service.util.RsaService

@Validated
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "用户登录相关接口")
class AuthController {
    @set:Autowired
    lateinit var authService: AuthService

    @set:Autowired
    lateinit var rsaService: RsaService

    @GetMapping("/key")
    @Operation(summary = "获取rsa公钥, 用于客户端加密敏感数据")
    fun key() = ResponseEntity.ok(rsaService.publicKeyString)

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

    @PostMapping("/setPassword")
    @Operation(summary = "修改密码")
    fun setPassword(dto: SetPasswordDto): ResponseEntity<Unit> {
        authService.setPassword(dto)
        return ResponseEntity.ok(null)
    }
}
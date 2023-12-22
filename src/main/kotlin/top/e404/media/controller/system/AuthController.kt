package top.e404.media.controller.system

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
class AuthController {
    @set:Autowired
    lateinit var authService: AuthService

    @PostMapping("/login")
    fun login(dto: LoginDto) = ResponseEntity.ok(authService.login(dto))

    @PostMapping("/register")
    fun login(dto: RegisterDto) = ResponseEntity.ok(authService.register(dto))

    @PostMapping("/forgetPassword")
    fun forgetPassword(dto: ForgetPasswordDto) = ResponseEntity.status(
        if (authService.forgetPassword(dto)) HttpStatus.OK
        else HttpStatus.NOT_FOUND
    ).build<Unit>()

    @PostMapping("/resetPassword")
    fun forgetPassword(dto: ResetPasswordDto): ResponseEntity<Unit> {
        authService.resetPassword(dto)
        return ResponseEntity.ok(null)
    }
}
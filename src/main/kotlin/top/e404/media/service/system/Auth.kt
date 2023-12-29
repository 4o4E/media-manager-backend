package top.e404.media.service.system

import at.favre.lib.crypto.bcrypt.BCrypt
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import top.e404.media.advice.currentUser
import top.e404.media.entity.auth.*
import top.e404.media.exception.CustomMessageException
import top.e404.media.exception.SimplePasswordException
import top.e404.media.exception.WrongPasswordException
import top.e404.media.service.database.ForgetPasswordService
import top.e404.media.service.database.UserBindService
import top.e404.media.service.database.UserService
import top.e404.media.service.database.UserTokenService
import top.e404.media.service.util.RsaService
import java.security.MessageDigest
import java.time.Duration

interface AuthService {
    fun login(dto: LoginDto): LoginVo
    fun register(dto: RegisterDto): LoginVo
    fun forgetPassword(dto: ForgetPasswordDto): Boolean
    fun resetPassword(dto: ResetPasswordDto)
    fun setPassword(dto: SetPasswordDto)
}

@Service
class AuthServiceImpl : AuthService {
    @Value("\${application.auth.password-regex}")
    lateinit var regex: String

    val passwordRegex by lazy { Regex(regex) }

    @Value("\${application.auth.forget.base-url}")
    lateinit var forgetBaseUrl: String

    @Value("\${application.auth.forget.duration}")
    lateinit var forgetDuration: Duration

    @set:Autowired
    lateinit var userService: UserService

    @set:Autowired
    lateinit var userTokenService: UserTokenService

    @set:Autowired
    lateinit var userBindService: UserBindService

    @set:Autowired
    lateinit var forgetPasswordService: ForgetPasswordService

    @set:Autowired
    lateinit var rsaService: RsaService

    @set:Autowired
    lateinit var executor: ThreadPoolTaskExecutor

    override fun login(dto: LoginDto): LoginVo {
        // 通过邮箱或手机登录
        // 没有该用户
        val bind = userBindService.getOne(
            LambdaQueryWrapper<UserBindDo>()
                .eq(UserBindDo::value, dto.username)
                .eq(UserBindDo::deleted, false)
        ) ?: throw WrongPasswordException
        val userId = bind.userId!!
        val user = userService.getById(userId)
        // 检查密码
        val result = rsaService.decryptByPrivate(dto.password)
            .decodeToString()
            .toCharArray()
            .let { BCrypt.verifyer().verify(it, user.password!!) }
        // 密码错误
        if (!result.verified) throw WrongPasswordException
        val token = userTokenService.generateToken(user)
        val perms = userService.getPermById(userId)

        // 现有token
        return LoginVo(userId, token.token!!, token.expireTime!!, perms)
    }

    @Transactional(rollbackFor = [Exception::class])
    override fun register(dto: RegisterDto): LoginVo {
        val (type, value, username, password) = dto
        if (!passwordRegex.matches(password)) throw SimplePasswordException
        val userDo = UserDo(
            name = username,
            password = rsaService.decryptByPrivate(password)
                .decodeToString()
                .toCharArray()
                .let { BCrypt.withDefaults().hashToString(12, it) },
            point = 0
        )
        userService.save(userDo)
        val userId = userDo.id!!
        userBindService.save(UserBindDo(userId = userId, type = type, value = value))
        val token = userTokenService.generateToken(userDo)
        val perms = userService.getPermById(userId)
        return LoginVo(userId, token.token!!, token.expireTime!!, perms)
    }

    override fun forgetPassword(dto: ForgetPasswordDto): Boolean {
        // 检查是否有对应账号
        val bindDo = userBindService.getOne(
            LambdaQueryWrapper<UserBindDo>()
                .eq(UserBindDo::type, dto.type.code)
                .eq(UserBindDo::value, dto.value)
        ) ?: return false

        @OptIn(ExperimentalStdlibApi::class)
        val token = MessageDigest.getInstance("sha-256").apply {
            update(dto.type.name.encodeToByteArray())
            update(";".encodeToByteArray())
            update(dto.value.encodeToByteArray())
        }.digest().toHexString()
        val forget = ForgetPasswordDo(userId = bindDo.userId!!, token = token)
        forgetPasswordService.save(forget)
        when (dto.type) {
            BindType.EMAIL -> ::sendEmail
            BindType.PHONE -> ::sendSms
        }.invoke(bindDo.value!!, "$forgetBaseUrl/$token")
        return true
    }

    private fun sendEmail(to: String, url: String) {
    }

    private fun sendSms(to: String, url: String) {
    }

    override fun resetPassword(dto: ResetPasswordDto) {
        val (token, password) = dto
        // 校验token
        val forgetPasswordDo = forgetPasswordService.getOne(
            LambdaQueryWrapper<ForgetPasswordDo>()
                .eq(ForgetPasswordDo::token, token)
        ) ?: throw CustomMessageException("invalid token")
        if (!forgetPasswordDo.valid!!) throw CustomMessageException("invalid token")
        // 已过有效期
        if (forgetPasswordDo.createTime!! + forgetDuration.toMillis() < System.currentTimeMillis()) {
            // 异步修改token过期
            executor.execute {
                forgetPasswordService.update(
                    LambdaUpdateWrapper<ForgetPasswordDo>()
                        .eq(ForgetPasswordDo::token, token)
                        .set(ForgetPasswordDo::valid, false)
                )
            }
            throw CustomMessageException("token expired")
        }
        // 校验密码
        if (!passwordRegex.matches(password)) throw SimplePasswordException
        // 更新密码
        userService.update(
            LambdaUpdateWrapper<UserDo>()
                .set(
                    UserDo::password,
                    rsaService.decryptByPrivate(password)
                        .decodeToString()
                        .toCharArray()
                        .let { BCrypt.withDefaults().hashToChar(12, it) }
                )
        )
    }

    override fun setPassword(dto: SetPasswordDto) {
        val (old, new) = dto
        // 检查密码
        val result = rsaService.decryptByPrivate(old)
            .decodeToString()
            .toCharArray()
            .let { BCrypt.verifyer().verify(it, currentUser!!.user.password) }
        // 密码错误
        if (!result.verified) throw WrongPasswordException
        // 校验密码
        if (!passwordRegex.matches(new)) throw SimplePasswordException
        // 更新密码
        userService.update(
            LambdaUpdateWrapper<UserDo>()
                .set(
                    UserDo::password,
                    rsaService.decryptByPrivate(new)
                        .decodeToString()
                        .toCharArray()
                        .let { BCrypt.withDefaults().hashToChar(12, it) }
                )
        )
    }
}
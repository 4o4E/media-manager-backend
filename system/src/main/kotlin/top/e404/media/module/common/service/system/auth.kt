package top.e404.media.module.common.service.system

import at.favre.lib.crypto.bcrypt.BCrypt
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import top.e404.media.module.common.advice.currentUser
import top.e404.media.module.common.entity.database.*
import top.e404.media.module.common.exception.AuthFail
import top.e404.media.module.common.exception.fail
import top.e404.media.module.common.service.database.ForgetPasswordService
import top.e404.media.module.common.service.database.UserBindService
import top.e404.media.module.common.service.database.UserService
import top.e404.media.module.common.service.database.UserTokenService
import top.e404.media.module.common.util.copyAsList
import top.e404.media.module.common.util.log
import top.e404.media.module.common.util.query
import top.e404.media.module.common.util.updateBy
import java.security.MessageDigest
import java.time.Duration

interface AuthService {
    /**
     * 登录
     */
    fun login(dto: LoginDto): LoginVo

    /**
     * 注册
     */
    fun register(dto: RegisterDto): LoginVo

    /**
     * 忘记密码
     */
    fun forgetPassword(dto: ForgetPasswordDto): Boolean

    /**
     * 重置密码
     */
    fun resetPassword(dto: ResetPasswordDto)

    /**
     * 设置密码
     */
    fun setPassword(dto: SetPasswordDto)
}

@Service
class AuthServiceImpl : AuthService {
    private val log = log()

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
    lateinit var executor: ThreadPoolTaskExecutor

    override fun login(dto: LoginDto): LoginVo {
        val user = userService.getOne(query {
            eq(UserDo::name, dto.username)
            eq(UserDo::deleted, false)
        }) ?: run {
            log.warn("没有对应的用户: {}", dto.username)
            fail(AuthFail.WRONG_PASSWORD)
        }
        val userId = user.id!!
        // 检查密码
        val result = BCrypt.verifyer().verify(dto.password.toCharArray(), user.password!!)
        // 密码错误
        if (!result.verified) {
            log.warn("密码错误: {}", dto)
            fail(AuthFail.WRONG_PASSWORD)
        }
        val token = userTokenService.generateToken(user)
        val perms = userService.getUserPerms(userId)
        val roles = userService.getUserRoles(userId).copyAsList(RoleVo::class)

        // 现有token
        return LoginVo(userId, token.token!!, token.expireTime!!, roles, perms)
    }

    @Transactional(rollbackFor = [Exception::class])
    override fun register(dto: RegisterDto): LoginVo {
        val (type, value, username, password) = dto
        if (!passwordRegex.matches(password)) fail(AuthFail.SIMPLE_PASSWORD)
        val userDo = UserDo(
            name = username,
            password = BCrypt.withDefaults().hashToString(12, password.toCharArray()),
            point = 0
        )
        userService.save(userDo)
        val userId = userDo.id!!
        userBindService.save(UserBindDo(userId = userId, type = type, value = value, checked = false))
        val token = userTokenService.generateToken(userDo)
        val perms = userService.getUserPerms(userId)
        val roles = userService.getUserRoles(userId).copyAsList(RoleVo::class)
        return LoginVo(userId, token.token!!, token.expireTime!!, roles, perms)
    }

    override fun forgetPassword(dto: ForgetPasswordDto): Boolean {
        // 检查是否有对应账号
        val bindDo = userBindService.getOne(query {
            eq(UserBindDo::type, dto.type)
            eq(UserBindDo::value, dto.value)
        }) ?: return false

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
        println("邮箱验证: $to, url: $url")
    }

    private fun sendSms(to: String, url: String) {
        println("短信验证: $to, url: $url")
    }

    override fun resetPassword(dto: ResetPasswordDto) {
        val (token, password) = dto
        // 校验token
        val forgetPasswordDo = forgetPasswordService.getOne(query {
            eq(ForgetPasswordDo::token, token)
        }) ?: fail(AuthFail.INVALID_TOKEN)
        if (!forgetPasswordDo.valid!!) fail(AuthFail.INVALID_TOKEN)
        // 已过有效期
        if (forgetPasswordDo.createTime!! + forgetDuration.toMillis() < System.currentTimeMillis()) {
            // 异步修改token过期
            executor.execute {
                forgetPasswordService.updateBy {
                    eq(ForgetPasswordDo::token, token)
                    set(ForgetPasswordDo::valid, false)
                }
            }
            fail(AuthFail.INVALID_TOKEN)
        }
        // 校验密码
        if (!passwordRegex.matches(password)) fail(AuthFail.SIMPLE_PASSWORD)
        // 更新密码
        userService.updateBy {
            eq(UserDo::id, forgetPasswordDo.userId)
            set(
                UserDo::password,
                BCrypt.withDefaults().hashToChar(12, password.toCharArray())
            )
        }
    }

    override fun setPassword(dto: SetPasswordDto) {
        val (old, new) = dto
        // 检查密码
        val result = BCrypt.verifyer().verify(old.toCharArray(), currentUser!!.user.password)
        // 密码错误
        if (!result.verified) fail(AuthFail.WRONG_PASSWORD)
        // 校验密码
        if (!passwordRegex.matches(new)) fail(AuthFail.WRONG_PASSWORD)
        // 更新密码
        userService.updateBy {
            eq(UserDo::id, currentUser!!.user.id)
            set(
                UserDo::password,
                BCrypt.withDefaults().hashToChar(12, new.toCharArray())
            )
        }
    }
}
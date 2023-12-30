package top.e404.media.module.common.service.database

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper
import com.baomidou.mybatisplus.extension.service.IService
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.stereotype.Service
import top.e404.media.module.common.entity.auth.UserDo
import top.e404.media.module.common.entity.auth.UserTokenDo
import top.e404.media.module.common.mapper.UserTokenMapper
import top.e404.media.module.common.util.log
import java.security.MessageDigest
import java.time.Duration

interface UserTokenService : IService<UserTokenDo> {
    fun generateToken(userDo: UserDo): UserTokenDo
}

@Service
class UserTokenServiceImpl : UserTokenService, ServiceImpl<UserTokenMapper, UserTokenDo>() {
    val logger = log()

    @Value("\${application.auth.max-session}")
    var maxSession: Int = 1

    @Value("\${application.auth.duration}")
    lateinit var duration: Duration

    @set:Autowired
    lateinit var executor: ThreadPoolTaskExecutor

    override fun generateToken(userDo: UserDo): UserTokenDo {
        val now = System.currentTimeMillis()
        val userId = userDo.id!!

        // 检查已有的未过期的token
        val tokenExists = list(
            LambdaQueryWrapper<UserTokenDo>()
                .select(UserTokenDo::id, UserTokenDo::createTime)
                .eq(UserTokenDo::userId, userId)
                .ge(UserTokenDo::expireTime, now)
        )
        // 超过最大会话数量时移除最早创建的会话
        if (tokenExists.size > maxSession) executor.execute {
            val waitForRemove = tokenExists
                .sortedBy { it.createTime }
                .take(tokenExists.size - maxSession)
                .map(UserTokenDo::id)
            logger.debug("移除用户{}超过限制的session{}个", userId, waitForRemove.size)
            remove(
                LambdaQueryWrapper<UserTokenDo>()
                    .`in`(UserTokenDo::id, waitForRemove)
            )
        }
        @OptIn(ExperimentalStdlibApi::class)
        val token = MessageDigest.getInstance("sha-256").apply {
            update(userDo.id.toString().encodeToByteArray())
            update(";".encodeToByteArray())
            update(now.toString().encodeToByteArray())
        }.digest().toHexString()
        val tokenDo = UserTokenDo(userId = userId, token = token, expireTime = now + duration.toMillis())
        save(tokenDo)
        return tokenDo
    }

}
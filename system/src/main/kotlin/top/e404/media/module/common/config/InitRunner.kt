package top.e404.media.module.common.config

import at.favre.lib.crypto.bcrypt.BCrypt
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import top.e404.media.module.common.entity.database.RoleDo
import top.e404.media.module.common.entity.database.UserDo
import top.e404.media.module.common.enums.SysPerm
import top.e404.media.module.common.service.database.RoleService
import top.e404.media.module.common.service.database.UserService
import top.e404.media.module.common.util.log
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * 初始化数据库数据
 */
@Component
class InitRunner : ApplicationRunner {
    companion object {
        var inInitialize = false
            private set
        const val INIT_FILE_NAME = ".init"
    }

    private val log = log()

    @set:Autowired
    lateinit var userService: UserService

    @set:Autowired
    lateinit var roleService: RoleService

    override fun run(args: ApplicationArguments) {
        if (File(INIT_FILE_NAME).exists()) return
        inInitialize = true
        log.info("开始初始化")
        val userId = 1L
        val adminRoleId = 1L
        val defaultRoleId = 2L
        userService.save(
            UserDo(
                id = userId,
                name = "admin",
                password = BCrypt.withDefaults().hashToString(12, "123456".toCharArray()),
                point = 0,
                roles = listOf(adminRoleId)
            )
        )
        log.info("初始化数据, 新建管理员用户admin, 密码123456")

        val perms = SysPerm.entries
        roleService.save(
            RoleDo(
                id = adminRoleId,
                name = "admin",
                remark = "超级管理员角色, 默认拥有所有权限",
                perms = perms.map { it.perm }
            )
        )
        log.info("设置超级管理员角色拥有所有权限: ${perms.joinToString(",", "[", "]") { it.perm }}")

        val userPerms = perms.filter { it.default }
        roleService.save(
            RoleDo(
                id = defaultRoleId,
                name = "default",
                remark = "默认用户角色",
                perms = userPerms.map { it.perm }
            )
        )
        log.info("设置默认用户角色拥有权限: ${userPerms.joinToString(",", "[", "]") { it.perm }}")

        val now = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now())
        File(INIT_FILE_NAME).writeText("初始化于$now")
        inInitialize = false
        log.info("完成初始化")
    }
}
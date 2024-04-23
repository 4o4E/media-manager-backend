package top.e404.media.module.common.config

import at.favre.lib.crypto.bcrypt.BCrypt
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping
import top.e404.media.module.common.annontation.RequirePerm
import top.e404.media.module.common.entity.auth.RoleDo
import top.e404.media.module.common.entity.auth.RolePermDo
import top.e404.media.module.common.entity.auth.UserDo
import top.e404.media.module.common.entity.auth.UserRoleDo
import top.e404.media.module.common.service.database.RolePermService
import top.e404.media.module.common.service.database.RoleService
import top.e404.media.module.common.service.database.UserRoleService
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
    lateinit var userRoleService: UserRoleService

    @set:Autowired
    lateinit var roleService: RoleService

    @set:Autowired
    lateinit var rolePermService: RolePermService

    @set:Autowired
    lateinit var applicationContext: WebApplicationContext

    val perms by lazy {
        applicationContext.getBean(RequestMappingHandlerMapping::class.java)
            .handlerMethods
            .values
            .mapNotNull { it.getMethodAnnotation(RequirePerm::class.java) }
            .flatMap { it.perms.asList() }
            .toSet()
    }

    override fun run(args: ApplicationArguments) {
        if (File(INIT_FILE_NAME).exists()) return
        inInitialize = true
        log.info("开始初始化")
        val userId = 1L
        userService.save(
            UserDo(
                id = userId,
                name = "admin",
                password = BCrypt.withDefaults().hashToString(12, "123456".toCharArray()),
                point = 0
            )
        )
        log.info("初始化数据, 新建管理员用户admin, 密码123456")

        val adminRoleId = 1L
        val defaultRoleId = 2L

        roleService.save(RoleDo(id = adminRoleId, name = "admin", description = "超级管理员角色, 默认拥有所有权限"))
        rolePermService.saveBatch(perms.map { perm -> RolePermDo(adminRoleId, perm) })
        log.info("设置超级管理员角色拥有所有权限: $perms")
        userRoleService.save(UserRoleDo(userId = userId, roleId = adminRoleId))
        log.info("设置管理员角色拥有所有权限: $perms")

        roleService.save(RoleDo(id = defaultRoleId, name = "default", description = "默认用户"))

        val now = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now())
        File(INIT_FILE_NAME).writeText("初始化于$now")
        inInitialize = false
        log.info("完成初始化")
    }
}
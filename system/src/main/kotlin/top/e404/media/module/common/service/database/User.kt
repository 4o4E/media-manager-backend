package top.e404.media.module.common.service.database

import com.baomidou.mybatisplus.extension.service.IService
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import top.e404.media.module.common.entity.database.RoleDo
import top.e404.media.module.common.entity.database.UserDo
import top.e404.media.module.common.exception.CommonFail
import top.e404.media.module.common.exception.fail
import top.e404.media.module.common.mapper.UserMapper

interface UserService : IService<UserDo> {
    /**
     * 通过用户id查询其角色
     */
    fun getUserRoles(id: Long): List<RoleDo>

    /**
     * 通过用户id查询其权限
     */
    fun getUserPerms(id: Long): Set<String>
}

@Service
class UserServiceImpl : UserService, ServiceImpl<UserMapper, UserDo>() {
    @set:Autowired
    lateinit var roleService: RoleService

    override fun getUserRoles(id: Long): List<RoleDo> {
        val user = getById(id) ?: fail(CommonFail.NOT_FOUND, "用户")
        return roleService.listByIds(user.roles)
    }

    override fun getUserPerms(id: Long): Set<String> {
        return getUserRoles(id).flatMap { it.perms!! }.toSet()
    }
}
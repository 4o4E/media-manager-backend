package top.e404.media.module.common.service.database

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper
import com.baomidou.mybatisplus.extension.service.IService
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import top.e404.media.module.common.entity.auth.RoleDo
import top.e404.media.module.common.entity.auth.RolePermDo
import top.e404.media.module.common.entity.auth.UserDo
import top.e404.media.module.common.mapper.UserMapper

interface UserService : IService<UserDo> {
    fun getRoleById(id: Long): List<RoleDo>
    fun getPermById(id: Long): Set<String>
}

@Service
class UserServiceImpl : UserService, ServiceImpl<UserMapper, UserDo>() {
    @set:Autowired
    lateinit var roleService: RoleService

    @set:Autowired
    lateinit var userRoleService: RoleService

    @set:Autowired
    lateinit var rolePermService: RolePermService

    override fun getRoleById(id: Long): List<RoleDo> {
        val roles = userRoleService.getRoleByUserId(id).map { it.id!! }
        return roleService.list(
            LambdaQueryWrapper<RoleDo>()
                .`in`(RoleDo::id, roles)
        )
    }

    override fun getPermById(id: Long): Set<String> {
        val roleIdList = getRoleById(id).map { it.id!! }
        return rolePermService.list(
            LambdaQueryWrapper<RolePermDo>()
                .select(RolePermDo::perm)
                .`in`(RolePermDo::role, roleIdList)
        ).map { it.perm!! }.toSet()

    }
}
package top.e404.media.module.common.service.database

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper
import com.baomidou.mybatisplus.extension.service.IService
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import top.e404.media.module.common.entity.auth.RoleDo
import top.e404.media.module.common.entity.auth.RolePermDo
import top.e404.media.module.common.mapper.RoleMapper

interface RoleService : IService<RoleDo> {
    fun getRoleByUserId(userId: Long): List<RoleDo>
    fun getPermByRoleId(id: Long): List<String>
    fun remove(id: Long): Boolean
}

@Service
class RoleServiceImpl : RoleService, ServiceImpl<RoleMapper, RoleDo>() {
    @set:Autowired
    lateinit var rolePermService: RolePermService

    override fun getRoleByUserId(userId: Long): List<RoleDo> {
        return baseMapper.getByUserId(userId)
    }

    override fun getPermByRoleId(id: Long): List<String> {
        return baseMapper.getPermByRoleId(id)
    }

    @Transactional
    override fun remove(id: Long): Boolean {
        return rolePermService.remove(LambdaQueryWrapper<RolePermDo>().eq(RolePermDo::role, id))
    }

}
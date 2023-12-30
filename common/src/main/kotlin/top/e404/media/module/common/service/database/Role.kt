package top.e404.media.module.common.service.database

import com.baomidou.mybatisplus.extension.service.IService
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import org.springframework.stereotype.Service
import top.e404.media.module.common.entity.auth.RoleDo
import top.e404.media.module.common.mapper.RoleMapper

interface RoleService : IService<RoleDo> {
    fun getRoleById(userId: Long): List<RoleDo>
}

@Service
class RoleServiceImpl : RoleService, ServiceImpl<RoleMapper, RoleDo>() {
    override fun getRoleById(userId: Long): List<RoleDo> {
        return baseMapper.getByUserId(userId)
    }

}
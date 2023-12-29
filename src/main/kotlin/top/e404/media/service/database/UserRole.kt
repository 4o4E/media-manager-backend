package top.e404.media.service.database

import com.baomidou.mybatisplus.extension.service.IService
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import org.springframework.stereotype.Service
import top.e404.media.entity.auth.UserRoleDo
import top.e404.media.mapper.UserRoleMapper

interface UserRoleService : IService<UserRoleDo>

@Service
class UserRoleServiceImpl : UserRoleService, ServiceImpl<UserRoleMapper, UserRoleDo>()
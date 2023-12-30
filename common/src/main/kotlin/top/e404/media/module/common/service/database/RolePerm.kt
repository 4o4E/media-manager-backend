package top.e404.media.module.common.service.database

import com.baomidou.mybatisplus.extension.service.IService
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import org.springframework.stereotype.Service
import top.e404.media.module.common.entity.auth.RolePermDo
import top.e404.media.module.common.mapper.RolePermMapper

interface RolePermService : IService<RolePermDo>

@Service
class RolePermServiceImpl : RolePermService, ServiceImpl<RolePermMapper, RolePermDo>()
package top.e404.media.service

import com.baomidou.mybatisplus.extension.service.IService
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import org.springframework.stereotype.Service
import top.e404.media.entity.auth.RolePermDo
import top.e404.media.mapper.RolePermMapper

interface RolePermService : IService<RolePermDo>

@Service
class RolePermServiceImpl : RolePermService, ServiceImpl<RolePermMapper, RolePermDo>()
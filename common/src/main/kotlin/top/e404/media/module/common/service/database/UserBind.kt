package top.e404.media.module.common.service.database

import com.baomidou.mybatisplus.extension.service.IService
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import org.springframework.stereotype.Service
import top.e404.media.module.common.entity.auth.UserBindDo
import top.e404.media.module.common.mapper.UserBindMapper

interface UserBindService : IService<UserBindDo>

@Service
class UserBindServiceImpl : UserBindService, ServiceImpl<UserBindMapper, UserBindDo>()
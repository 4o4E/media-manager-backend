package top.e404.media.service.database

import com.baomidou.mybatisplus.extension.service.IService
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import org.springframework.stereotype.Service
import top.e404.media.entity.auth.ForgetPasswordDo
import top.e404.media.mapper.ForgetPasswordMapper

interface ForgetPasswordService : IService<ForgetPasswordDo>

@Service
class ForgetPasswordServiceImpl : ForgetPasswordService, ServiceImpl<ForgetPasswordMapper, ForgetPasswordDo>()
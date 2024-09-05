package top.e404.media.module.media.service

import com.baomidou.mybatisplus.extension.service.IService
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import org.springframework.stereotype.Service
import top.e404.media.module.media.entity.MessageTagDo
import top.e404.media.module.media.mapper.MessageTagMapper

interface MessageTagService : IService<MessageTagDo>

@Service
class MessageTagServiceImpl : MessageTagService, ServiceImpl<MessageTagMapper, MessageTagDo>()
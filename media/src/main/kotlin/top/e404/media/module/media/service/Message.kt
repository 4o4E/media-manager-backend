package top.e404.media.module.media.service

import com.baomidou.mybatisplus.extension.service.IService
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import org.springframework.stereotype.Service
import top.e404.media.module.media.entity.MessageDo
import top.e404.media.module.media.entity.MessageQueryDto
import top.e404.media.module.media.mapper.MessageMapper
import top.e404.media.module.common.util.query

interface MessageService : IService<MessageDo> {
    fun query(dto: MessageQueryDto): List<MessageDo>
}

@Service
class MessageServiceImpl : MessageService, ServiceImpl<MessageMapper, MessageDo>() {
    override fun query(dto: MessageQueryDto): List<MessageDo> {
        return getBaseMapper().query(dto)
    }
}
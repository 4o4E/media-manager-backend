package top.e404.media.module.media.service

import com.baomidou.mybatisplus.extension.service.IService
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import org.springframework.stereotype.Service
import top.e404.media.module.media.entity.MediaLikeDo
import top.e404.media.module.media.mapper.MediaLikeMapper

interface MediaLikeService : IService<MediaLikeDo>

@Service
class MediaLikeServiceImpl : MediaLikeService, ServiceImpl<MediaLikeMapper, MediaLikeDo>()
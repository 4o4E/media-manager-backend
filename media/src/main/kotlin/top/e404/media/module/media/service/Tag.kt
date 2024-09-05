package top.e404.media.module.media.service

import com.baomidou.mybatisplus.extension.service.IService
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import org.springframework.stereotype.Service
import top.e404.media.module.media.entity.TagDo
import top.e404.media.module.media.mapper.TagMapper

interface TagService : IService<TagDo>

@Service
class TagServiceImpl : TagService, ServiceImpl<TagMapper, TagDo>()
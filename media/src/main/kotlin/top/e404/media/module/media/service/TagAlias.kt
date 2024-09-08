package top.e404.media.module.media.service

import com.baomidou.mybatisplus.extension.service.IService
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import org.springframework.stereotype.Service
import top.e404.media.module.media.entity.TagAliasDo
import top.e404.media.module.media.entity.TagDo
import top.e404.media.module.media.mapper.TagAliasMapper
import top.e404.media.module.media.mapper.TagMapper

interface TagAliasService : IService<TagAliasDo>

@Service
class TagAliasServiceImpl : TagAliasService, ServiceImpl<TagAliasMapper, TagAliasDo>()
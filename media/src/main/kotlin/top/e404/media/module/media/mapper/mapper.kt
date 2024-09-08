package top.e404.media.module.media.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.baomidou.mybatisplus.core.metadata.IPage
import org.apache.ibatis.annotations.Mapper
import top.e404.media.module.media.entity.TagAliasDo
import top.e404.media.module.media.entity.TagDo

@Mapper
interface TagMapper : BaseMapper<TagDo> {
    fun listTags(page: IPage<TagDo>, key: String?)
}

@Mapper
interface TagAliasMapper : BaseMapper<TagAliasDo>
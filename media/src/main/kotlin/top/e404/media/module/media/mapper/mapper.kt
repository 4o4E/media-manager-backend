package top.e404.media.module.media.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import org.apache.ibatis.annotations.Mapper
import top.e404.media.module.media.entity.*

@Mapper
interface MessageMapper : BaseMapper<MessageDo> {
    fun query(dto: MessageQueryDto): List<MessageDo>
}

@Mapper
interface MessageTagMapper : BaseMapper<MessageTagDo>

@Mapper
interface TagMapper : BaseMapper<TagDo>

@Mapper
interface TagAliasMapper : BaseMapper<TagAliasDo>
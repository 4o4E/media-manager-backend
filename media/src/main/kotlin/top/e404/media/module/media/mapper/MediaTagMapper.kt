package top.e404.media.module.media.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import org.apache.ibatis.annotations.Mapper
import top.e404.media.module.media.entity.MediaTagDo

@Mapper
interface MediaTagMapper : BaseMapper<MediaTagDo> {
    fun listTags(key: String?): List<MediaTagDo>
    fun getByName(name: String): MediaTagDo?
    fun exists(names: List<String>): Boolean
}
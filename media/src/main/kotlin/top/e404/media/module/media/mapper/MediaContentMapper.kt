package top.e404.media.module.media.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.baomidou.mybatisplus.core.metadata.IPage
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param
import top.e404.media.module.media.entity.MediaContentDo
import top.e404.media.module.media.entity.MediaListOption
import top.e404.media.module.media.entity.MessageQueryDto

@Mapper
interface MediaContentMapper : BaseMapper<MediaContentDo> {
    fun random(count: Long): List<MediaContentDo>

    fun page(page: IPage<MediaContentDo>, @Param("dto") dto: MediaListOption): IPage<MediaContentDo>

    fun query(@Param("dto") dto: MessageQueryDto): List<MediaContentDo>
}
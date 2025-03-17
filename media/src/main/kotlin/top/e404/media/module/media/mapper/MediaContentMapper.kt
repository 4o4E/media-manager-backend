package top.e404.media.module.media.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.baomidou.mybatisplus.core.metadata.IPage
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param
import top.e404.media.module.common.advice.currentUser
import top.e404.media.module.media.entity.*

@Mapper
interface MediaContentMapper : BaseMapper<MediaContentDo> {
    fun random(
        count: Long,
        userId: Long = currentUser!!.user.id!!
    ): List<MediaContentVo>

    fun page(
        page: IPage<MediaContentVo>,
        @Param("dto") dto: MediaListOption,
        userId: Long = currentUser!!.user.id!!
    ): IPage<MediaContentVo>

    fun query(
        @Param("dto") dto: MessageQueryDto,
        userId: Long = currentUser!!.user.id!!
    ): List<MediaContentVo>
}
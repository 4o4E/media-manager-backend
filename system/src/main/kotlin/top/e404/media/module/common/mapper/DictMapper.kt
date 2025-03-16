package top.e404.media.module.common.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.baomidou.mybatisplus.core.metadata.IPage
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param
import top.e404.media.module.common.entity.database.DictCollection
import top.e404.media.module.common.entity.database.DictDo
import top.e404.media.module.common.entity.database.DictQuery

@Mapper
interface DictMapper : BaseMapper<DictDo> {
    fun listAll(page: IPage<DictCollection>, @Param("extra") extra: DictQuery): IPage<DictCollection>
}
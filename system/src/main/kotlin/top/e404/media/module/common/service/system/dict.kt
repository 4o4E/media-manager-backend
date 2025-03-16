package top.e404.media.module.common.service.system

import com.baomidou.mybatisplus.core.metadata.IPage
import com.baomidou.mybatisplus.extension.service.IService
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import org.springframework.stereotype.Service
import top.e404.media.module.common.entity.database.DictCollection
import top.e404.media.module.common.entity.database.DictDo
import top.e404.media.module.common.entity.database.DictQuery
import top.e404.media.module.common.entity.database.DictVo
import top.e404.media.module.common.entity.dto.page.PageInfo
import top.e404.media.module.common.entity.dto.page.PageResult
import top.e404.media.module.common.mapper.DictMapper
import top.e404.media.module.common.util.*

interface DictService : IService<DictDo> {
    /**
     * 分页搜索
     * @param type 字典类型
     * @param pageInfo 分页信息
     */
    fun listByType(type: String, pageInfo: PageInfo): PageResult<DictVo, Void>

    /**
     * 获取指定类型的字典
     * @param type 字典类型
     * @param values 字典值集合
     */
    fun get(type: String, values: Collection<String>): List<DictVo>
    fun listAll(page: IPage<DictCollection>, extra: DictQuery): PageResult<DictCollection, Void>
}

@Service
class DictServiceImpl : DictService, ServiceImpl<DictMapper, DictDo>() {
    override fun listByType(type: String, pageInfo: PageInfo): PageResult<DictVo, Void> {
        return page(pageInfo.toMybatisPage())
            .convert(DictVo::class)
            .toPageResult()
    }

    override fun get(type: String, values: Collection<String>): List<DictVo> {
        return list(query {
            eq(DictDo::type, type)
            `in`(DictDo::value, values)
        }).convertList(DictVo::class)
    }

    override fun listAll(page: IPage<DictCollection>, extra: DictQuery): PageResult<DictCollection, Void> {
        return baseMapper.listAll(page, extra).toPageResult()
    }
}
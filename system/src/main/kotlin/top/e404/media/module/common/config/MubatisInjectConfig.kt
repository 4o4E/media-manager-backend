package top.e404.media.module.common.config

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler
import com.baomidou.mybatisplus.core.handlers.StrictFill
import org.apache.ibatis.reflection.MetaObject
import org.springframework.stereotype.Component
import top.e404.media.module.common.advice.currentUser

@Component
class MybatisColumnsHandler : MetaObjectHandler {
    val longType = Long::class.javaObjectType
    val boolType = Boolean::class.javaObjectType
    private val defaultId = 1L
    override fun insertFill(metaObject: MetaObject) {
        val currentUserId = currentUser?.user?.id ?: defaultId
        val now = System.currentTimeMillis()
        val tableInfo = findTableInfo(metaObject)
        val list: List<StrictFill<*, *>> = arrayListOf(
            StrictFill("createBy", longType) { currentUserId },
            StrictFill("updateBy", longType) { currentUserId },

            StrictFill("createTime", longType) { now },
            StrictFill("updateTime", longType) { now },

            StrictFill("version", longType) { 0L },
            StrictFill("deleted", boolType) { false },
        )
        strictInsertFill(tableInfo, metaObject, list)
    }

    override fun updateFill(metaObject: MetaObject) {
        val currentUserId = currentUser?.user?.id ?: defaultId
        val now = System.currentTimeMillis()
        val tableInfo = findTableInfo(metaObject)
        val list: List<StrictFill<*, *>> = arrayListOf(
            StrictFill("updateBy", longType) { currentUserId },
            StrictFill("updateTime", longType) { now },
        )
        strictInsertFill(tableInfo, metaObject, list)
    }
}
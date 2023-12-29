package top.e404.media.config

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler
import org.apache.ibatis.reflection.MetaObject
import org.springframework.stereotype.Component
import top.e404.media.advice.currentUser

@Component
class MybatisColumnsHandler : MetaObjectHandler {
    val currentUserId get() = if (InitRunner.inInitialize) 0 else currentUser!!.user.id!!
    val now get() = System.currentTimeMillis()
    override fun insertFill(metaObject: MetaObject) {
        strictInsertFill(metaObject, "createBy", Long::class.java, currentUserId)
        strictInsertFill(metaObject, "updateBy", Long::class.java, currentUserId)

        strictInsertFill(metaObject, "createTime", Long::class.java, now)
        strictInsertFill(metaObject, "updateTime", Long::class.java, now)

        strictInsertFill(metaObject, "revision", Long::class.java, 0L)
        strictInsertFill(metaObject, "deleted", Boolean::class.java, false)

    }

    override fun updateFill(metaObject: MetaObject) {
        this.strictUpdateFill(metaObject, "updateTime", Long::class.java, now)
        this.strictUpdateFill(metaObject, "updateBy", Long::class.java, currentUserId)
    }
}
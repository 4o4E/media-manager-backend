package top.e404.media.config

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler
import org.apache.ibatis.reflection.MetaObject
import org.springframework.stereotype.Component
import top.e404.media.advice.currentUser
import java.time.LocalDateTime

@Component
class MybatisColumnsHandler : MetaObjectHandler {
    override fun insertFill(metaObject: MetaObject) {
        val now = LocalDateTime.now()
        strictInsertFill(
            metaObject, "createTime",
            LocalDateTime::class.java, now
        )
        strictInsertFill(
            metaObject, "updateTime",
            LocalDateTime::class.java, now
        )
        strictInsertFill(metaObject, "revision", Long::class.java, 0L)
        strictInsertFill(metaObject, "deleted", Boolean::class.java, false)

        val currentUserId = currentUser!!.user.id!!
        strictInsertFill(metaObject, "createBy", Long::class.java, currentUserId)
        strictInsertFill(metaObject, "updateBy", Long::class.java, currentUserId)
    }

    override fun updateFill(metaObject: MetaObject) {
        this.strictUpdateFill(
            metaObject, "updateTime",
            LocalDateTime::class.java, LocalDateTime.now()
        )
        this.strictUpdateFill(metaObject, "updateBy", Long::class.java, currentUser!!.user.id!!)
    }
}
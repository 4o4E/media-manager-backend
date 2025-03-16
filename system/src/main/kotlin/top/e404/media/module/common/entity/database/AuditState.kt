package top.e404.media.module.common.entity.database

import com.baomidou.mybatisplus.annotation.EnumValue

enum class AuditState(@field:EnumValue val code: Int) {
    PENDING(1),
    PASS(2),
    REJECT(3),
}
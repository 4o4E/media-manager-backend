package top.e404.media.module.common.entity.database

import com.baomidou.mybatisplus.annotation.EnumValue
import io.swagger.v3.oas.annotations.media.Schema

/**
 * 用户绑定类型
 */

@Schema(description = "用户绑定类型")
enum class BindType(@field:EnumValue val code: Int) {
    @Schema(description = "邮件")
    EMAIL(1),

    @Schema(description = "短信")
    PHONE(2);
}
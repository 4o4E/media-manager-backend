package top.e404.media.module.common.entity.database

import com.baomidou.mybatisplus.annotation.EnumValue
import io.swagger.v3.oas.annotations.media.Schema
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import top.e404.media.module.common.util.primitive

/**
 * 用户绑定类型
 */

@Schema(description = "用户绑定类型")
enum class BindType {
    @Schema(description = "邮件")
    EMAIL,

    @Schema(description = "短信")
    PHONE;
}
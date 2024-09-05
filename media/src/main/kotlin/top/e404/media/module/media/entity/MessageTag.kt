package top.e404.media.module.media.entity

import com.baomidou.mybatisplus.annotation.*
import io.swagger.v3.oas.annotations.media.Schema
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encodeToString
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import org.apache.ibatis.type.BaseTypeHandler
import org.apache.ibatis.type.JdbcType
import top.e404.media.module.common.util.primitive
import top.e404.media.module.media.entity.data.Message
import top.e404.media.module.media.entity.info.ApprovedState
import top.e404.media.module.media.entity.info.MessageType
import java.sql.CallableStatement
import java.sql.PreparedStatement
import java.sql.ResultSet

/**
 * 消息
 *
 * @property messageId 消息id
 * @property tagId 标签id
 * @property version 乐观锁
 * @property createBy 创建者
 * @property createTime 创建于
 * @property updateBy 修改者
 * @property updateTime 修改于
 */
@Serializable
@TableName("media_message_tag")
data class MessageTagDo(
    var messageId: Long? = null,
    var tagId: Long? = null,

    @field:Version
    @field:TableField(fill = FieldFill.INSERT)
    var version: Long? = null,
    @field:TableField(fill = FieldFill.INSERT)
    var createBy: Long? = null,
    @field:TableField(fill = FieldFill.INSERT)
    var createTime: Long? = null,
    @field:TableField(fill = FieldFill.INSERT_UPDATE)
    var updateBy: Long? = null,
    @field:TableField(fill = FieldFill.INSERT_UPDATE)
    var updateTime: Long? = null,
)
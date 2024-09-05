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
 * @property id 唯一id
 * @property content 消息内容
 * @property type 消息类型
 * @property hash 消息hash
 * @property approved 审核状态
 * @property version 乐观锁
 * @property createBy 创建者
 * @property createTime 创建于
 * @property updateBy 修改者
 * @property updateTime 修改于
 */
@Serializable
@TableName("media_message")
data class MessageDo(
    @field:TableId(type = IdType.AUTO)
    var id: Long? = null,
    @field:TableField(typeHandler = MessageListTypeHandler::class)
    var content: List<Message>? = null,
    var type: MessageType? = null,
    var hash: String? = null,
    var approved: ApprovedState? = null,

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

object MessageListTypeHandler : BaseTypeHandler<List<Message>>() {
    private val listSerializer = ListSerializer(Message.serializer())
    override fun getNullableResult(rs: ResultSet?, columnName: String?): List<Message> {
        return Json.decodeFromString(listSerializer, rs?.getString(columnName) ?: "[]")
    }

    override fun getNullableResult(rs: ResultSet, columnIndex: Int): List<Message> {
        return Json.decodeFromString(listSerializer, rs.getString(columnIndex))
    }

    override fun getNullableResult(cs: CallableStatement?, columnIndex: Int): List<Message> {
        return Json.decodeFromString(listSerializer, cs?.getString(columnIndex) ?: "[]")
    }

    override fun setNonNullParameter(ps: PreparedStatement, i: Int, parameter: List<Message>, jdbcType: JdbcType) {
        ps.setString(i, Json.encodeToString(listSerializer, parameter))
    }
}
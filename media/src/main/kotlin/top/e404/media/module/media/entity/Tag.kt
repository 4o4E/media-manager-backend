package top.e404.media.module.media.entity

import com.baomidou.mybatisplus.annotation.*
import kotlinx.serialization.Serializable

/**
 * 标签
 *
 * @property id 唯一id
 * @property name 创建标签时指定的名字, 不用于显示, 以alias为准
 * @property description 标签简介
 * @property version 乐观锁
 * @property createBy 创建者
 * @property createTime 创建于
 * @property updateBy 修改者
 * @property updateTime 修改于
 */
@Serializable
@TableName("media_tag")
data class TagDo(
    @field:TableId(type = IdType.AUTO)
    var id: Long? = null,
    var name: String? = null,
    var description: String? = null,

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

@Serializable
data class TagVo(
    val id: Long,
    val name: String,
    val description: String,
    val alias: List<String>
)

@Serializable
data class CreateTagDto(
    val name: String,
    val description: String,
    val alias: List<String>
)

@Serializable
data class TagDto(
    val name: String,
    val description: String
)
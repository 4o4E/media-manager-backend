package top.e404.media.module.media.entity

import com.baomidou.mybatisplus.annotation.*
import kotlinx.serialization.Serializable

/**
 * 标签别名
 *
 * @property id 唯一id
 * @property tagId 标签id
 * @property name 标签别名
 * @property version 乐观锁
 * @property createBy 创建者
 * @property createTime 创建于
 * @property updateBy 修改者
 * @property updateTime 修改于
 */
@Serializable
@TableName("media_tag_alias")
data class TagAliasDo(
    @field:TableId(type = IdType.AUTO)
    var id: Long? = null,
    var tagId: Long? = null,
    var name: String? = null,

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
package top.e404.media.module.common.enums

import kotlinx.serialization.Serializable

@Serializable
enum class SysPerm(
    val perm: String,
    @Suppress("UNUSED")
    val desc: String,
    val default: Boolean = false
) {
    USER_VIEW("user:view", "浏览用户"),
    USER_EDIT("user:edit", "编辑用户"),
    USER_ROLE_VIEW("user:role:view", "浏览用户角色"),
    USER_ROLE_EDIT("user:role:edit", "编辑用户角色"),
    ROLE_VIEW("role:view", "浏览角色"),
    ROLE_EDIT("role:edit", "编辑角色"),
    ROLE_PERM_VIEW("role:perm:view", "浏览角色权限"),
    ROLE_PERM_EDIT("role:perm:edit", "编辑角色权限"),
    MEDIA_RANDOM("media:random", "随机获取message", true),
    MEDIA_QUERY("media:query", "通过高级查询获取message", true),
    MEDIA_UPLOAD("media:upload", "上传消息"),
    MEDIA_LIST("media:list", "列出消息"),
    MEDIA_EDIT("media:edit", "编辑消息"),
    MEDIA_SKIP_APPROVAL("media:approval:skip", "跳过消息审核"),
    MEDIA_COMMENT_VIEW("media:comment:view", "浏览消息评论", true),
    MEDIA_COMMENT_LIKE("media:comment:like", "喜欢消息评论", true),
    MEDIA_COMMENT_EDIT("media:comment:edit", "编辑消息评论"),
    MEDIA_COMMENT_POST("media:comment:post", "发送消息评论"),
    FILE_EXISTS("file:exists", "检查文件是否已上传"),
    FILE_UPLOAD("file:upload", "上传文件"),
    TAG_VIEW("tag:view", "浏览Tag", true),
    TAG_EDIT("tag:edit", "编辑Tag"),

    DICT_MANAGE("dict:manage", "管理字典"),
    ;
}

data class PermVo(
    val perm: String,
    val desc: String,
    val default: Boolean
)

fun SysPerm.toVo() = PermVo(perm, desc, default)
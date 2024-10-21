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
    MESSAGE_RANDOM("message:random", "随机获取message", true),
    MESSAGE_QUERY("message:query", "通过高级查询获取message", true),
    MESSAGE_UPLOAD("message:upload", "上传消息"),
    MESSAGE_LIST("message:list", "列出消息"),
    MESSAGE_EDIT("message:edit", "编辑消息"),
    MESSAGE_SKIP_APPROVAL("message:approval:skip", "跳过消息审核"),
    MESSAGE_COMMENT_VIEW("message:comment:view", "浏览消息评论", true),
    MESSAGE_COMMENT_EDIT("message:comment:edit", "编辑消息评论"),
    MESSAGE_COMMENT_POST("message:comment:post", "发送消息评论"),
    FILE_EXISTS("file:exists", "检查文件是否已上传"),
    FILE_UPLOAD("file:upload", "上传文件"),
    TAG_VIEW("tag:view", "浏览Tag", true),
    TAG_EDIT("tag:edit", "编辑Tag"),
    ;
}

data class PermVo(
    val perm: String,
    val desc: String,
    val default: Boolean
)

fun SysPerm.toVo() = PermVo(perm, desc, default)
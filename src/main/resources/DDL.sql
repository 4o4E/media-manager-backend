CREATE DATABASE IF NOT EXISTS media;
USE media;
CREATE TABLE IF NOT EXISTS sys_user
(
    id          BIGINT        NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '唯一id',
    name        VARCHAR(16)   NOT NULL COMMENT '用户名',
    password    CHAR(64)      NOT NULL COMMENT '密码',
    point       INT DEFAULT 0 NOT NULL COMMENT '用户点数',

    deleted     BOOL          NOT NULL COMMENT '逻辑删除',
    version     BIGINT        NOT NULL COMMENT '乐观锁',
    create_by   BIGINT        NOT NULL COMMENT '创建者',
    create_time BIGINT        NOT NULL COMMENT '创建于',
    update_by   BIGINT        NOT NULL COMMENT '修改者',
    update_time BIGINT        NOT NULL COMMENT '修改于'
) COMMENT '用户'
    CHARSET UTF8MB4
    COLLATE utf8mb4_general_ci
    ENGINE InnoDB;

CREATE TABLE IF NOT EXISTS sys_user_bind
(
    id          BIGINT           NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '唯一id',
    user_id     BIGINT           NOT NULL COMMENT '用户id',
    type        TINYINT UNSIGNED NOT NULL COMMENT '绑定类型, 0邮箱, 1手机',
    value       VARCHAR(128)     NOT NULL COMMENT '绑定的具体值',
    checked     BOOL             NOT NULL COMMENT '绑定是否已验证',

    deleted     BOOL             NOT NULL COMMENT '逻辑删除',
    version     BIGINT           NOT NULL COMMENT '乐观锁',
    create_by   BIGINT           NOT NULL COMMENT '创建者',
    create_time BIGINT           NOT NULL COMMENT '创建于',
    update_by   BIGINT           NOT NULL COMMENT '修改者',
    update_time BIGINT           NOT NULL COMMENT '修改于',
    FOREIGN KEY (user_id) REFERENCES sys_user (id)
) COMMENT '用户绑定'
    CHARSET UTF8MB4
    COLLATE utf8mb4_general_ci
    ENGINE InnoDB;

CREATE TABLE IF NOT EXISTS sys_user_token
(
    id          BIGINT   NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '唯一id',
    user_id     BIGINT   NOT NULL COMMENT '用户id',
    token       CHAR(64) NOT NULL COMMENT '用户会话token',
    expire_time BIGINT   NOT NULL COMMENT '会话过期时间, 过期后应重登',

    version     BIGINT   NOT NULL COMMENT '乐观锁',
    create_time BIGINT   NOT NULL COMMENT '创建于',
    update_time BIGINT   NOT NULL COMMENT '修改于',
    FOREIGN KEY (user_id) REFERENCES sys_user (id)
) COMMENT '用户会话'
    CHARSET UTF8MB4
    COLLATE utf8mb4_general_ci
    ENGINE InnoDB;

CREATE TABLE IF NOT EXISTS sys_role
(
    id          BIGINT       NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '唯一id',
    name        VARCHAR(64)  NOT NULL COMMENT '角色名字',
    description VARCHAR(256) NOT NULL COMMENT '角色简介',

    version     BIGINT       NOT NULL COMMENT '乐观锁',
    create_by   BIGINT       NOT NULL COMMENT '创建者',
    create_time BIGINT       NOT NULL COMMENT '创建于',
    update_by   BIGINT       NOT NULL COMMENT '修改者',
    update_time BIGINT       NOT NULL COMMENT '修改于'
) COMMENT '角色'
    DEFAULT CHARSET UTF8MB4
    COLLATE utf8mb4_general_ci
    ENGINE InnoDB;

# CREATE TABLE IF NOT EXISTS sys_menu
# (
#     id          BIGINT       NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '唯一id',
#     title       VARCHAR(64)  NOT NULL COMMENT '菜单标题',
#     parent      BIGINT       NULL COMMENT '父菜单id',
#     path        VARCHAR(256) NOT NULL COMMENT '菜单组件路径',
#     icon        VARCHAR(64)  NOT NULL COMMENT '菜单图标, 仅在是文件夹时生效, 菜单应自己提供图标',
#     menu        BOOL         NOT NULL COMMENT '菜单类型, false-文件夹, true-菜单',
#
#     version     BIGINT       NOT NULL COMMENT '乐观锁',
#     create_by   BIGINT       NOT NULL COMMENT '创建者',
#     create_time BIGINT       NOT NULL COMMENT '创建于',
#     update_by   BIGINT       NOT NULL COMMENT '修改者',
#     update_time BIGINT       NOT NULL COMMENT '修改于',
#     FOREIGN KEY (parent) REFERENCES sys_menu (id)
# ) COMMENT '菜单'
#     CHARSET UTF8MB4
#     COLLATE utf8mb4_general_ci
#     ENGINE InnoDB;

CREATE TABLE IF NOT EXISTS sys_user_role
(
    user_id     BIGINT NOT NULL COMMENT '用户id',
    role_id     BIGINT NOT NULL COMMENT '角色id',

    version     BIGINT NOT NULL COMMENT '乐观锁',
    create_by   BIGINT NOT NULL COMMENT '创建者',
    create_time BIGINT NOT NULL COMMENT '创建于',
    update_by   BIGINT NOT NULL COMMENT '修改者',
    update_time BIGINT NOT NULL COMMENT '修改于',
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES sys_user (id),
    FOREIGN KEY (role_id) REFERENCES sys_role (id)
) COMMENT '用户角色, 一个用户有多个角色'
    CHARSET UTF8MB4
    COLLATE utf8mb4_general_ci
    ENGINE InnoDB;

CREATE TABLE IF NOT EXISTS sys_role_perm
(
    role        BIGINT      NOT NULL COMMENT '角色id',
    perm        VARCHAR(64) NOT NULL COMMENT '权限名字',

    version     BIGINT      NOT NULL COMMENT '乐观锁',
    create_by   BIGINT      NOT NULL COMMENT '创建者',
    create_time BIGINT      NOT NULL COMMENT '创建于',
    update_by   BIGINT      NOT NULL COMMENT '修改者',
    update_time BIGINT      NOT NULL COMMENT '修改于',
    PRIMARY KEY (role, perm)
) COMMENT '角色权限, 一个角色有多个权限'
    DEFAULT CHARSET UTF8MB4
    COLLATE utf8mb4_general_ci
    ENGINE InnoDB;

CREATE TABLE IF NOT EXISTS sys_user_forget_password
(
    id          BIGINT             NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '唯一id',
    user_id     BIGINT             NOT NULL COMMENT '用户id',
    token       CHAR(64) UNICODE   NOT NULL COMMENT '用于找回的token',
    valid       BOOL DEFAULT FALSE NOT NULL COMMENT '是否有效',

    version     BIGINT             NOT NULL COMMENT '乐观锁',
    create_time BIGINT             NOT NULL COMMENT '创建于',
    update_time BIGINT             NOT NULL COMMENT '修改于',
    FOREIGN KEY (user_id) REFERENCES sys_user (id)
) COMMENT '用户找回密码的token表'
    CHARSET UTF8MB4
    COLLATE utf8mb4_general_ci
    ENGINE InnoDB;

# CREATE VIEW v_sys_user_menu(id, title, parent, path, icon, menu) AS
# SELECT m.id, m.title, m.parent, m.path, m.icon, m.menu
# FROM sys_menu m
# WHERE m.id in
#       (SELECT rm.menu_id
#        FROM sys_role_menu rm
#        WHERE rm.role_id in
#              (SELECT ur.role_id
#               FROM sys_user_role ur
#               WHERE ur.user_id = @user_id));
#
# SELECT *
# FROM v_sys_user_menu
# WHERE @user_id = 1
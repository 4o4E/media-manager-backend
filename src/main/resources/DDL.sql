USE media;

CREATE TABLE IF NOT EXISTS sys_user
(
    id          BIGINT             NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '唯一id',
    name        VARCHAR(16) UNIQUE NOT NULL COMMENT '用户名',
    password    CHAR(64)           NOT NULL COMMENT '密码',
    point       INT DEFAULT 0      NOT NULL COMMENT '用户点数',

    deleted     BOOL               NOT NULL COMMENT '逻辑删除',
    version     BIGINT             NOT NULL COMMENT '乐观锁',
    create_by   BIGINT             NOT NULL COMMENT '创建者',
    create_time BIGINT             NOT NULL COMMENT '创建于',
    update_by   BIGINT             NOT NULL COMMENT '修改者',
    update_time BIGINT             NOT NULL COMMENT '修改于'
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

CREATE TABLE IF NOT EXISTS media_tag
(
    id          BIGINT       NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '唯一id',
    name        VARCHAR(64)  NOT NULL COMMENT '标签名字',
    description VARCHAR(256) NOT NULL COMMENT '标签简介',

    version     BIGINT       NOT NULL COMMENT '乐观锁',
    create_by   BIGINT       NOT NULL COMMENT '创建者',
    create_time BIGINT       NOT NULL COMMENT '创建于',
    update_by   BIGINT       NOT NULL COMMENT '修改者',
    update_time BIGINT       NOT NULL COMMENT '修改于'
) COMMENT '标签'
    CHARSET UTF8MB4
    COLLATE utf8mb4_general_ci
    ENGINE InnoDB;

CREATE TABLE IF NOT EXISTS media_tag_alias
(
    id          BIGINT      NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '唯一id',
    tag_id      BIGINT      NOT NULL COMMENT '标签id',
    name        VARCHAR(64) NOT NULL COMMENT '标签别名',

    version     BIGINT      NOT NULL COMMENT '乐观锁',
    create_by   BIGINT      NOT NULL COMMENT '创建者',
    create_time BIGINT      NOT NULL COMMENT '创建于',
    update_by   BIGINT      NOT NULL COMMENT '修改者',
    update_time BIGINT      NOT NULL COMMENT '修改于',
    FOREIGN KEY (tag_id) REFERENCES media_tag (id)
) COMMENT '标签别名'
    CHARSET UTF8MB4
    COLLATE utf8mb4_general_ci
    ENGINE InnoDB;

CREATE TABLE IF NOT EXISTS media_message
(
    id          BIGINT   NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '唯一id',
    content     JSON     NOT NULL COMMENT '消息内容',
    type        TINYINT  NOT NULL COMMENT '消息类型',
    hash        CHAR(64) NOT NULL COMMENT '消息hash',
    approved    TINYINT  NOT NULL COMMENT '审核状态',

    version     BIGINT   NOT NULL COMMENT '乐观锁',
    create_by   BIGINT   NOT NULL COMMENT '创建者',
    create_time BIGINT   NOT NULL COMMENT '创建于',
    update_by   BIGINT   NOT NULL COMMENT '修改者',
    update_time BIGINT   NOT NULL COMMENT '修改于'
) COMMENT '消息'
    CHARSET UTF8MB4
    COLLATE utf8mb4_general_ci
    ENGINE InnoDB;

CREATE TABLE IF NOT EXISTS media_message_tag
(
    message_id  BIGINT NOT NULL COMMENT '消息id',
    tag_id      BIGINT NOT NULL COMMENT '标签id',

    version     BIGINT NOT NULL COMMENT '乐观锁',
    create_by   BIGINT NOT NULL COMMENT '创建者',
    create_time BIGINT NOT NULL COMMENT '创建于',
    update_by   BIGINT NOT NULL COMMENT '修改者',
    update_time BIGINT NOT NULL COMMENT '修改于',
    PRIMARY KEY (message_id, tag_id),
    FOREIGN KEY (message_id) REFERENCES media_message (id),
    FOREIGN KEY (tag_id) REFERENCES media_tag (id)
) COMMENT '消息标签, 一个消息有多个标签'
    CHARSET UTF8MB4
    COLLATE utf8mb4_general_ci
    ENGINE InnoDB;


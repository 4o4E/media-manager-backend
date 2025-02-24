CREATE TABLE IF NOT EXISTS sys_role
(
    id          BIGSERIAL     NOT NULL PRIMARY KEY,
    name        VARCHAR(64)   NOT NULL,
    remark      VARCHAR(256)  NOT NULL,
    perms       VARCHAR(64)[] NOT NULL,

    version     BIGINT        NOT NULL,
    create_by   BIGINT        NOT NULL,
    create_time BIGINT        NOT NULL,
    update_by   BIGINT        NOT NULL,
    update_time BIGINT        NOT NULL
);
COMMENT ON TABLE sys_role IS '系统角色';
COMMENT ON COLUMN sys_role.name IS '角色名字';
COMMENT ON COLUMN sys_role.remark IS '角色备注';

CREATE TABLE IF NOT EXISTS sys_user
(
    id          BIGSERIAL          NOT NULL PRIMARY KEY,
    name        VARCHAR(16) UNIQUE NOT NULL,
    password    CHAR(64)           NOT NULL,
    point       INT DEFAULT 0      NOT NULL,
    roles       BIGINT[]           NOT NULL,

    deleted     BOOLEAN            NOT NULL,
    version     BIGINT             NOT NULL,
    create_by   BIGINT             NOT NULL,
    create_time BIGINT             NOT NULL,
    update_by   BIGINT             NOT NULL,
    update_time BIGINT             NOT NULL
);

COMMENT ON TABLE sys_user IS '系统用户';
COMMENT ON COLUMN sys_user.name IS '用户名';
COMMENT ON COLUMN sys_user.password IS '密码';
COMMENT ON COLUMN sys_user.point IS '用户点数';

CREATE TYPE BIND_TYPE AS ENUM ('PHONE', 'EMAIL');
COMMENT ON TYPE BIND_TYPE IS '绑定类型';

CREATE TABLE IF NOT EXISTS sys_user_bind
(
    id          BIGSERIAL    NOT NULL PRIMARY KEY,
    user_id     BIGINT       NOT NULL REFERENCES sys_user (id),
    type        BIND_TYPE    NOT NULL,
    value       VARCHAR(128) NOT NULL,
    checked     BOOLEAN      NOT NULL,

    deleted     BOOLEAN      NOT NULL,
    version     BIGINT       NOT NULL,
    create_by   BIGINT       NOT NULL,
    create_time BIGINT       NOT NULL,
    update_by   BIGINT       NOT NULL,
    update_time BIGINT       NOT NULL,
    FOREIGN KEY (user_id) REFERENCES sys_user (id)
);
COMMENT ON TABLE sys_user_bind IS '用户绑定';
COMMENT ON COLUMN sys_user_bind.user_id IS '用户id';
COMMENT ON COLUMN sys_user_bind.type IS '绑定类型';
COMMENT ON COLUMN sys_user_bind.value IS '绑定的具体值';
COMMENT ON COLUMN sys_user_bind.checked IS '绑定是否已验证';

CREATE TABLE IF NOT EXISTS sys_user_token
(
    id          BIGSERIAL NOT NULL PRIMARY KEY,
    user_id     BIGINT    NOT NULL REFERENCES sys_user (id),
    token       CHAR(64)  NOT NULL,
    expire_time BIGINT    NOT NULL,

    version     BIGINT    NOT NULL,
    create_time BIGINT    NOT NULL,
    update_time BIGINT    NOT NULL,
    FOREIGN KEY (user_id) REFERENCES sys_user (id)
);
COMMENT ON TABLE sys_user_token IS '用户token';
COMMENT ON COLUMN sys_user_token.user_id IS '用户id';
COMMENT ON COLUMN sys_user_token.token IS '用户会话token';
COMMENT ON COLUMN sys_user_token.expire_time IS '会话过期时间, 过期后应重登';

CREATE TABLE IF NOT EXISTS sys_user_forget_password
(
    id          BIGSERIAL             NOT NULL PRIMARY KEY,
    user_id     BIGINT                NOT NULL REFERENCES sys_user (id),
    token       CHAR(64)              NOT NULL,
    valid       BOOLEAN DEFAULT FALSE NOT NULL,

    version     BIGINT                NOT NULL,
    create_time BIGINT                NOT NULL,
    update_time BIGINT                NOT NULL
);
COMMENT ON TABLE sys_user_forget_password IS '系统权限';
COMMENT ON COLUMN sys_user_forget_password.token IS '用于找回的token';
COMMENT ON COLUMN sys_user_forget_password.valid IS '是否有效';

CREATE TABLE IF NOT EXISTS media_tag
(
    id          BIGSERIAL     NOT NULL PRIMARY KEY,
    names       VARCHAR(64)[] NOT NULL UNIQUE,
    remark      VARCHAR(256)  NOT NULL,

    version     BIGINT        NOT NULL,
    create_by   BIGINT        NOT NULL,
    create_time BIGINT        NOT NULL,
    update_by   BIGINT        NOT NULL,
    update_time BIGINT        NOT NULL
);
COMMENT ON TABLE media_tag IS '系统权限';
COMMENT ON COLUMN media_tag.names IS '创建标签时指定的名字,';
COMMENT ON COLUMN media_tag.remark IS '备注';

CREATE TYPE AUDIT_STATE AS ENUM ('WAIT', 'PASS', 'REJECT');
CREATE TYPE MEDIA_TYPE AS ENUM ('COMPLEX', 'IMAGE', 'VIDEO', 'AUDIO', 'TEXT');

CREATE TABLE IF NOT EXISTS media_content
(
    id          BIGSERIAL   NOT NULL PRIMARY KEY,
    audit_state AUDIT_STATE NOT NULL,
    type        MEDIA_TYPE  NOT NULL,
    title       VARCHAR(64) NOT NULL,
    tags        BIGINT[]    NOT NULL,
    content     JSON        NOT NULL,

    version     BIGINT      NOT NULL,
    create_by   BIGINT      NOT NULL,
    create_time BIGINT      NOT NULL,
    update_by   BIGINT      NOT NULL,
    update_time BIGINT      NOT NULL
);
COMMENT ON TABLE media_content IS '媒体数据';
COMMENT ON COLUMN media_content.audit_state IS '审批状态';
COMMENT ON COLUMN media_content.type IS '媒体类型';
COMMENT ON COLUMN media_content.title IS '标题';
COMMENT ON COLUMN media_content.tags IS '标签';
COMMENT ON COLUMN media_content.content IS '内容';

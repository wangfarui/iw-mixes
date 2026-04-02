create table base_file_records
(
    id          int unsigned not null auto_increment comment 'id',
    file_name   varchar(128) not null comment '文件名称(带后缀)',
    file_hash   binary(32)   not null comment '文件hash二进制值',
    file_uri    varchar(255) not null comment '文件路径',
    file_prefix varchar(128) not null comment '文件前缀',
    file_suffix varchar(16)  not null default '' comment '文件后缀',
    create_time datetime     not null default current_timestamp comment '创建时间',
    primary key (id),
    UNIQUE KEY (file_hash)
) comment '文件上传记录表';

create table base_application_account
(
    id          int unsigned auto_increment                 not null comment 'id',
    name        varchar(32)       default ''                not null comment '应用名称',
    address     varchar(255)      default ''                not null comment '应用地址',
    account     varchar(64)       default ''                not null comment '账号',
    password    varchar(128)      default ''                not null comment '密码',
    remark      varchar(255)      default ''                not null comment '备注',
    deleted     tinyint(1)        default 0                 not null comment '是否删除(true表示已删除, 默认false表示未删除)',
    create_time datetime          default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time datetime          default CURRENT_TIMESTAMP not null comment '更新时间',
    user_id     int unsigned      default 0                 not null comment '用户id',
    primary key (id),
    key idx_user_id (user_id)
) comment '应用账号信息表';

create table base_website_navigation
(
    id          int unsigned auto_increment                 not null comment 'id',
    name        varchar(64)       default ''                not null comment '网站名称',
    url         varchar(255)      default ''                not null comment '网站链接',
    description varchar(255)      default ''                not null comment '网站描述',
    icon        varchar(255)      default ''                not null comment '网站图标URL',
    category    varchar(32)       default ''                not null comment '网站分类',
    tags        varchar(1024)     default ''                not null comment '标签(JSON数组)',
    status      tinyint(4) unsigned default 1               not null comment '网站状态(1在线 2离线)',
    deleted     tinyint(1)        default 0                 not null comment '是否删除(true表示已删除, 默认false表示未删除)',
    create_time datetime          default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time datetime          default CURRENT_TIMESTAMP not null comment '更新时间',
    user_id     int unsigned      default 0                 not null comment '用户id',
    primary key (id),
    key idx_user_id (user_id),
    key idx_category (category),
    key idx_status (status)
) comment '网站导航记录表';

alter table base_website_navigation
    add column shared tinyint(1) unsigned default 0 not null comment '是否共享(0不共享 1共享)' after status,
    add index idx_shared (shared);

create table if not exists base_ai_task
(
    id              int unsigned auto_increment                 not null comment 'id',
    title           varchar(80)       default ''                not null comment '任务标题',
    description     varchar(255)      default ''                not null comment '会话任务描述',
    tool_type       tinyint(4) unsigned default 2              not null comment '工具类型(1Codex 2Claude Code 3Gemini CLI)',
    session_key     varchar(128)      default ''                not null comment 'sessionKey',
    task_status     tinyint(4) unsigned default 1              not null comment '任务状态(1进行中 2已完成 3暂停)',
    project_name    varchar(64)       default ''                not null comment '所属项目',
    workspace_path  varchar(255)      default ''                not null comment '工作区路径',
    model_name      varchar(64)       default ''                not null comment '模型名称',
    git_branch      varchar(128)      default ''                not null comment 'git分支',
    transcript_path varchar(512)      default ''                not null comment '记录文件路径',
    resume_command  varchar(255)      default ''                not null comment '恢复命令',
    last_active_at  datetime          default CURRENT_TIMESTAMP not null comment '最近活跃时间',
    deleted         tinyint(1)        default 0                 not null comment '是否删除(true表示已删除, 默认false表示未删除)',
    create_time     datetime          default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time     datetime          default CURRENT_TIMESTAMP not null comment '更新时间',
    user_id         int unsigned      default 0                 not null comment '用户id',
    primary key (id),
    unique key uk_user_tool_session_key (user_id, tool_type, session_key),
    key idx_user_id (user_id),
    key idx_task_status (task_status),
    key idx_tool_type (tool_type),
    key idx_project_name (project_name),
    key idx_session_key (session_key),
    key idx_last_active_at (last_active_at)
) comment 'AI会话任务表';

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

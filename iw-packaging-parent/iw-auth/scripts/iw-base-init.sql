drop table if exists base_file_records;
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

drop table if exists base_application_account;
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


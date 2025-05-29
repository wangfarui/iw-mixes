create table auth_user
(
    id                      int unsigned                           not null primary key auto_increment comment '用户id',
    username                varchar(64)                            not null comment '用户名',
    password                varchar(255)                           not null comment '密码',
    name                    varchar(32)                            not null comment '姓名',
    avatar                  varchar(255) default ''                not null comment '头像',
    account_non_expired     tinyint(1)   default 1                 not null comment '账号过期状态(0已过期, 1未过期)',
    account_non_locked      tinyint(1)   default 1                 not null comment '账号锁定状态(0已锁定, 1未锁定)',
    credentials_non_expired tinyint(1)   default 1                 not null comment '用户凭证过期状态(0已过期, 1未过期)',
    enabled                 tinyint(1)   default 1                 not null comment '是否启用(0否, 1是)',
    create_time             datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    last_login_time         datetime                               null comment '最后登录时间'
) comment '用户表';

alter table auth_user
    add column phone_number char(11) null comment '电话号码' after id,
    add index idx_phone_number (phone_number),
    add index idx_username (username);

alter table auth_user
add column email_address varchar(64) not null default '' comment '邮箱地址';
alter table auth_user
add index idx_email_address (email_address);

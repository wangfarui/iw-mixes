create table iw_auth_user
(
    id                      bigint                               not null primary key auto_increment comment '用户id',
    username                varchar(64)                          not null comment '用户名',
    password                varchar(255)                         not null comment '密码',
    name                    varchar(32)                          not null comment '姓名',
    account_non_expired     tinyint(1) default 1                 not null comment '账号过期状态(0已过期, 1未过期)',
    account_non_locked      tinyint(1) default 1                 not null comment '账号锁定状态(0已锁定, 1未锁定)',
    credentials_non_expired tinyint(1) default 1                 not null comment '用户凭证过期状态(0已过期, 1未过期)',
    enabled                 tinyint(1) default 1                 not null comment '是否启用(0否, 1是)',
    create_time             datetime   default CURRENT_TIMESTAMP not null comment '创建时间',
    last_login_time         datetime                             null comment '最后登录时间'
) comment '用户表';

create table iw_auth_role
(
    id   bigint      not null primary key auto_increment comment '角色id',
    name varchar(64) not null comment '角色名称'
) comment '角色表';

create table iw_auth_user_role
(
    id      bigint not null primary key auto_increment comment '用户角色表id',
    user_id bigint not null comment '用户id',
    role_id bigint not null comment '角色id',
    key idx_user_id (user_id)
) comment '用户角色关联关系表';

create table iw_auth_persistent
(
    username  varchar(64) not null,
    series    varchar(64) not null,
    token     varchar(64) not null,
    last_used datetime    not null,
    primary key (series)
) comment '认证信息持久化表';

insert into iw_auth_user(username, password, name)
values ('root', '{noop}123456', '超级管理员'),
       ('admin', '{noop}123456', '系统管理员'),
       ('user', '{noop}123456', '默认用户');

insert into iw_auth_role(name)
values ('超级管理员'),
       ('系统管理员'),
       ('普通用户');

insert into iw_auth_user_role(user_id, role_id)
values (1, 1),
       (2, 2),
       (3, 3);


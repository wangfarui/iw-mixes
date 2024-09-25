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

insert into auth_user(username, password, name)
values ('wray', '$2a$10$67.UXGSk9a5Ajw4mOfRH.uURUjl/.uVNVzl4oAS/KvCAuEBJWLYU2', 'wray'),
       ('1', '$2a$10$xZrFIUcXgMUvG7NMCAFT9eGTcC7ZPwkzU6aOM/FcaOfjnXnmgR/zG', '1');

insert into auth_user(username, password, name)
values ('joyce', '$2a$10$oTVXo.gi.NIpqqSoY0GZluA2q0.l/2a0w36j07CRRFB3Xm6UdpsZm', '乖乖');

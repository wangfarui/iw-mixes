drop table if exists eat_user;
create table eat_user
(
    id       int         not null auto_increment comment 'id',
    username varchar(32) not null comment '用户名',
    name     varchar(32) not null comment '姓名',
    password varchar(64) not null comment '密码',
    primary key (id)
) comment 'sa用户表';

alter table eat_user
    add column avatar varchar(255) default '' not null comment '头像';

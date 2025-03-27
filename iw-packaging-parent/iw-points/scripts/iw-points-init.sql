## 积分相关表
drop table if exists points_total;
create table points_total
(
    id             int unsigned auto_increment        not null comment 'id',
    points_balance int                                not null comment '积分余额',
    create_time    datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time    datetime default CURRENT_TIMESTAMP not null comment '更新时间',
    user_id        int unsigned                       not null comment '用户id',
    primary key (id),
    unique key udx_user_id (user_id)
) comment '积分合计表';

drop table if exists points_records;
create table points_records
(
    id               int unsigned auto_increment            not null comment 'id',
    transaction_type tinyint(1)   default 1                 not null comment '积分变动分类(1表示增加, 2表示扣减)',
    points           int                                    not null comment '积分变动数量(可以是正数或负数)',
    source           varchar(64)                            not null comment '积分来源',
    source_type      tinyint(1)   default 0                 not null comment '积分来源分类(0表示无分类)',
    remark           varchar(255) default ''                not null comment '积分变动备注',
    deleted          tinyint(1)   default 0                 not null comment '是否删除(true表示已删除, 默认false表示未删除)',
    create_time      datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time      datetime     default CURRENT_TIMESTAMP not null comment '更新时间',
    user_id          int unsigned                           not null comment '用户id',
    primary key (id),
    key idx_user_id (user_id)
) comment '积分记录表';

## 任务相关表
drop table if exists points_task_group;
create table points_task_group
(
    id          int unsigned auto_increment comment 'id',
    parent_id   int unsigned default 0                 not null comment '父分组id',
    group_name  varchar(32)  default ''                not null comment '分组名称',
    is_top      tinyint(1)   default 0                 not null comment '是否置顶任务 0-否 1-是',
    sort        int          default 0                 not null comment '排序 0-默认排序',
    deleted     tinyint(1)   default 0                 not null comment '是否删除(true表示已删除, 默认false表示未删除)',
    create_time datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time datetime     default CURRENT_TIMESTAMP not null comment '更新时间',
    user_id     int unsigned                           not null comment '用户id',
    primary key (id),
    key idx_parent_id (parent_id)
) comment '任务分组表';

drop table if exists points_task_basics;
create table points_task_basics
(
    id            int unsigned auto_increment comment 'id',
    parent_id     int unsigned                           null comment '父任务id',
    task_group_id int unsigned default 0                 not null comment '任务分组id 0-无分组(收集箱)',
    task_name     varchar(128) default ''                not null comment '任务名称',
    task_remark   text                                   null comment '任务备注',
    task_status   tinyint(1)   default 0                 not null comment '任务状态 0-未完成 1-已完成 2-已放弃',
    deadline_date date                                   null comment '截止日期(在重复任务中可被理解为开始日期)',
    deadline_time time                                   null comment '截止时间(在重复任务中可被理解为开始时间)',
    priority      tinyint(1)   default 0                 not null comment '优先级(数值越大,优先级越高) 0-无优先级',
    is_top        tinyint(1)   default 0                 not null comment '是否置顶任务 0-否 1-是',
    sort          int          default 0                 not null comment '排序 0-默认排序',
    deleted       tinyint(1)   default 0                 not null comment '是否删除(true表示已删除, 默认false表示未删除)',
    create_time   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time   datetime     default CURRENT_TIMESTAMP not null comment '更新时间',
    user_id       int unsigned                           not null comment '用户id',
    primary key (id),
    key idx_task_group (task_group_id)
) comment '任务基础表';

drop table if exists points_task_relation;
create table points_task_relation
(
    id int unsigned auto_increment comment 'id',
    task_id       int unsigned not null comment '任务id',
    reward_points         tinyint   default 0                 not null comment '奖励积分',
    punish_points         tinyint   default 0                 not null comment '处罚积分',
    primary key (id),
    key idx_task_id (task_id)
) comment '任务关联表';

alter table points_task_basics
add column done_time datetime null comment '任务完成时间' after sort;

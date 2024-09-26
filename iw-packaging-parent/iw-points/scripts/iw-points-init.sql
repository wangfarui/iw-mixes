create table points_total
(
    id             int unsigned auto_increment comment 'id',
    points_balance int                                  not null comment '积分余额',
    create_time    datetime   default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time    datetime   default CURRENT_TIMESTAMP not null comment '更新时间',
    user_id        int unsigned                         not null comment '用户id',
    primary key (id),
    unique key udx_user_id (user_id)
) comment '积分合计表';

create table points_records
(
    id               int unsigned auto_increment comment 'id',
    transaction_type tinyint(1)   default 1                 not null comment '积分变动类型(1表示增加, 2表示扣减)',
    points           int                                    not null comment '积分变动数量(可以是正数或负数)',
    source           varchar(255)                           not null comment '积分来源',
    remark           varchar(255) default ''                not null comment '积分变动备注',
    deleted          tinyint(1)   default 0                 not null comment '是否删除(true表示已删除, 默认false表示未删除',
    create_time      datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time      datetime     default CURRENT_TIMESTAMP not null comment '更新时间',
    user_id          int unsigned                           not null comment '用户id',
    primary key (id),
    key idx_user_id (user_id)
) comment '积分记录表';
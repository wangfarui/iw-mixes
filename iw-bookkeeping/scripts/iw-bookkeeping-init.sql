drop table if exists bookkeeping_expenses;
create table bookkeeping_expenses
(
    id             int unsigned                           not null auto_increment comment 'id',
    expense_date   datetime                               not null comment '支出时间',
    expense_item   varchar(255)                           not null comment '支出项目',
    expense_amount decimal(8, 2)                          not null comment '支出金额',
    remark         varchar(255) default ''                not null comment '备注',
    deleted        tinyint(1)   default 0                 not null comment '是否删除(true表示已删除, 默认false表示未删除',
    create_time    datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time    datetime     default CURRENT_TIMESTAMP not null comment '更新时间',
    primary key (id)
) comment '支出表';

drop table if exists bookkeeping_incomes;
create table bookkeeping_incomes
(
    id            int unsigned                           not null auto_increment comment 'id',
    income_date   datetime                               not null comment '收入时间',
    income_source varchar(64)                            not null comment '收入来源',
    income_amount decimal(8, 2)                          not null comment '收入金额',
    remark        varchar(255) default ''                not null comment '备注',
    deleted       tinyint(1)   default 0                 not null comment '是否删除(true表示已删除, 默认false表示未删除',
    create_time   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time   datetime     default CURRENT_TIMESTAMP not null comment '更新时间',
    primary key (id)
) comment '收入表';

drop table if exists bookkeeping_records;
create table bookkeeping_records
(
    id              int unsigned                           not null auto_increment comment 'id',
    record_date     date                                   not null comment '记录日期',
    record_time     datetime                               not null comment '记录时间',
    record_category tinyint                                not null comment '记录类型(1:支出, 2:收入)',
    record_source   varchar(64)                            not null default '' comment '记录来源',
    amount          decimal(8, 2)                          not null comment '金额',
    record_type     tinyint      default 0                 not null comment '记录分类',
    remark          varchar(255) default ''                not null comment '备注',
    deleted         tinyint(1)   default 0                 not null comment '是否删除(true表示已删除, 默认false表示未删除',
    create_time     datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time     datetime     default CURRENT_TIMESTAMP not null comment '更新时间',
    user_id         int unsigned                           not null comment '用户id',
    primary key (id)
) comment '记账记录表';
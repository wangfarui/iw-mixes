drop table if exists base_dict;
create table base_dict
(
    id          int unsigned auto_increment                 not null comment 'id',
    parent_id   int unsigned      default 0                 not null comment '父字典id',
    dict_type   smallint unsigned                           not null comment '字典类型(枚举)',
    dict_code   smallint unsigned default 0                 not null comment '字典code',
    dict_name   varchar(32)                                 not null comment '字典名称',
    dict_status tinyint           default 1                 not null comment '字典状态(0禁用 1启用)',
    sort        smallint unsigned default 1                 not null comment '排序',
    deleted     tinyint(1)        default 0                 not null comment '是否删除(true表示已删除, 默认false表示未删除)',
    create_time datetime          default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time datetime          default CURRENT_TIMESTAMP not null comment '更新时间',
    user_id     int unsigned      default 0                 not null comment '用户id',
    primary key (id),
    key idx_dict_type (dict_type)
) comment '字典表';

# 字典表的用户id应该是非空且有实际用户id的，当 user_id == 0 时，表示该字典值为模板字典
alter table base_dict
    modify column user_id int unsigned not null comment '用户id';

drop table if exists base_dict_business_relation;
create table base_dict_business_relation
(
    id            int unsigned auto_increment not null comment '主键id',
    business_type smallint unsigned           not null comment '业务类型(枚举code)',
    business_id   int unsigned                not null comment '业务id',
    dict_id       int unsigned                not null comment '字典id',
    primary key (id),
    key idx_business_column (business_type, business_id)
) comment '字典业务关联表';

## 初始字典模板数据
insert into base_dict (dict_type, dict_code, dict_name, sort, user_id)
values (3002, 0, '任意时间', 1, 0),
       (3002, 1, '早餐', 2, 0),
       (3002, 2, '午餐', 3, 0),
       (3002, 3, '晚餐', 4, 0),
       (3003, 1, '荤菜', 1, 0),
       (3003, 2, '素菜', 2, 0),
       (3003, 3, '荤素搭配', 3, 0),
       (3004, 1, '正常', 1, 0),
       (3004, 2, '禁用', 2, 0),
       (3004, 3, '售空', 3, 0),
       (4002, 1, '餐饮美食', 1, 0),
       (4002, 2, '日用百货', 2, 0),
       (4002, 3, '交通出行', 3, 0),
       (4002, 4, '充值缴费', 4, 0),
       (4002, 5, '生活服务', 5, 0),
       (4003, 1, '支出', 1, 0),
       (4003, 2, '收入', 2, 0)
;
insert into base_dict (dict_type, dict_name, sort, user_id)
values (4001, '买菜', 1, 0),
       (4001, '外卖', 2, 0),
       (4001, '朴朴', 3, 0),
       (4001, '聚餐', 4, 0),
       (4001, '固定支出', 5, 0),
       (4001, '旅游', 6, 0)
;

##  MQ消息消费记录表
drop table if exists base_mq_consume_records;
create table base_mq_consume_records
(
    id           bigint unsigned auto_increment comment '消息消费记录id',
    service_name varchar(32)                           not null comment '服务名称',
    message_id   varchar(64)                           not null comment '消息id',
    version      varchar(16)                           not null comment '消息版本',
    topic        varchar(64)                           not null comment '消息topic',
    tag          varchar(64) default ''                not null comment '消息tag',
    body         text                                  not null comment '消息体',
    status       tinyint                               not null comment '消费状态(0待消费, 1消费成功, 2消费失败)',
    create_time  datetime    default CURRENT_TIMESTAMP not null comment '创建时间',
    user_id      int unsigned                          null comment '用户id',
    primary key (id),
    key idx_message_id (message_id)
) comment 'MQ消息消费记录表';

##  MQ消息生产记录表
drop table if exists base_mq_produce_records;
create table base_mq_produce_records
(
    id           bigint unsigned auto_increment comment '消息生产记录id',
    service_name varchar(32)                           not null comment '服务名称',
    message_id   varchar(64)                           not null comment '消息id',
    version      varchar(16)                           not null comment '消息版本',
    topic        varchar(64)                           not null comment '消息topic',
    tag          varchar(64) default ''                not null comment '消息tag',
    body         text                                  not null comment '消息体',
    create_time  datetime    default CURRENT_TIMESTAMP not null comment '创建时间',
    user_id      int unsigned                          null comment '用户id',
    primary key (id)
) comment 'MQ消息生产记录表';

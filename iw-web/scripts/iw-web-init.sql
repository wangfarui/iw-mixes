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

drop table if exists base_dict_business_relation;
create table base_dict_business_relation
(
    id int unsigned not null  comment '业务id',
    dict_id int unsigned not null  comment '字典id',
    primary key (id, dict_id),
    key idx_dict_id (dict_id)
) comment '字典业务关联表';

## 初始字典数据
insert into base_dict (dict_type, dict_code, dict_name, sort)
values (3002, 0, '任意时间', 1),
       (3002, 1, '早餐', 2),
       (3002, 2, '午餐', 3),
       (3002, 3, '晚餐', 4),
       (3003, 1, '荤菜', 1),
       (3003, 2, '素菜', 2),
       (3003, 3, '荤素搭配', 3),
       (3004, 1, '正常', 1),
       (3004, 2, '禁用', 2),
       (3004, 3, '售空', 3),
       (4002, 1, '餐饮美食', 1),
       (4002, 2, '日用百货', 2),
       (4002, 3, '交通出行', 3),
       (4002, 4, '充值缴费', 4),
       (4002, 5, '生活服务', 5),
       (4003, 1, '支出', 1),
       (4003, 2, '收入', 2)
;
insert into base_dict (dict_type, dict_name, sort)
values (4001, '买菜', 1),
       (4001, '外卖', 2),
       (4001, '朴朴', 3),
       (4001, '聚餐', 4),
       (4001, '固定支出', 5),
       (4001, '旅游', 6)
;

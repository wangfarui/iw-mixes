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

drop table if exists external_sms_records;
create table external_sms_records
(
    id             int unsigned                         not null auto_increment comment 'id',
    phone_number   char(11)                             not null comment '电话号码',
    sign_name      varchar(32)                          not null comment '签名名称',
    template_code  varchar(16)                          not null comment '模板CODE',
    template_param varchar(64)                          not null default '' comment '模板参数',
    send_status    tinyint(1)                           not null default 0 comment '发送状态(0待发送, 1发送成功, 2发送失败)',
    deleted        tinyint(1) default 0                 not null comment '是否删除(true表示已删除, 默认false表示未删除',
    create_time    datetime   default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time    datetime   default CURRENT_TIMESTAMP not null comment '更新时间',
    primary key (id),
    key idx_phone_number (phone_number)
) comment '外部SMS短信记录表';

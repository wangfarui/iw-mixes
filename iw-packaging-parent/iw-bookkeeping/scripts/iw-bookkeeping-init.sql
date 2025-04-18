drop table if exists bookkeeping_records;
create table bookkeeping_records
(
    id                   int unsigned                           not null auto_increment comment 'id',
    record_date          date                                   not null comment '记录日期',
    record_time          datetime                               not null comment '记录时间',
    record_category      tinyint                                not null comment '记录类型(1:支出, 2:收入)',
    record_source        varchar(64)  default ''                not null comment '记录来源',
    amount               decimal(8, 2)                          not null comment '金额',
    record_type          tinyint      default 0                 not null comment '记录分类',
    remark               varchar(255) default ''                not null comment '备注',
    is_excitation_record tinyint(1)   default 0                 not null comment '是否为激励记录(0否, 1是)',
    deleted              tinyint(1)   default 0                 not null comment '是否删除(true表示已删除, 默认false表示未删除',
    create_time          datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time          datetime     default CURRENT_TIMESTAMP not null comment '更新时间',
    user_id              int unsigned                           not null comment '用户id',
    primary key (id)
) comment '记账记录表';

alter table bookkeeping_records
    add column is_statistics tinyint(1) default 1 not null comment '是否计入统计(0否, 1是)' after is_excitation_record;
alter table bookkeeping_records
    add column order_no varchar(32) default '' not null comment '订单号' after id;

drop table if exists bookkeeping_actions;
create table bookkeeping_actions
(
    id                   int unsigned                           not null auto_increment comment 'id',
    record_category      tinyint                                not null comment '记录类型(1:支出, 2:收入)',
    record_source        varchar(64)  default ''                not null comment '记录来源',
    record_type          tinyint      default 0                 not null comment '记录分类',
    record_icon			 varchar(255) default '' 				not null comment '记录图标',
    record_tags 		 varchar(255) default '' 				not null comment '记录标签(标签字典id逗号拼接)',
   	sort        		 DECIMAL(10,4)default 0                 not null comment '排序 0-默认排序',
    deleted              tinyint(1)   default 0                 not null comment '是否删除(true表示已删除, 默认false表示未删除',
    create_time          datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time          datetime     default CURRENT_TIMESTAMP not null comment '更新时间',
    user_id              int unsigned                           not null comment '用户id',
    primary key (id)
) comment '记账行为表';

insert into bookkeeping_actions(record_category, record_source, record_type, record_icon, sort, user_id)
values
(1, "餐饮", 1, "/icon/yinshi/canyin", 10, 0),
(1, "购物", 2, "/icon/gouwu/gouwu", 20, 0),
(1, "交通", 3, "/icon/jiaotong/jiaotong", 30, 0),
(1, "话费", 4, "/icon/shouru/chongzhi", 40, 0),
(1, "买药", 5, "/icon/shenghuo/yaowan", 50, 0),
(2, "工资", 5, "/icon/shouru/gongzi", 10, 0),
(2, "兼职", 5, "/icon/shouru/jianzhi", 20, 0);

alter table bookkeeping_records
add column record_icon varchar(255) default '' not null comment '记录图标' after record_source;

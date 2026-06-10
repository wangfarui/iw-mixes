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

create table bookkeeping_budget (
    id int unsigned auto_increment not null comment 'id',
    budget_type tinyint not null comment '预算类型',
    record_type tinyint default 0 not null comment '记录分类',
    budget_amount decimal(8, 2) not null comment '预算金额',
    deleted tinyint(1) default 0 not null comment '是否删除(true表示已删除, 默认false表示未删除)',
    create_time datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time datetime default CURRENT_TIMESTAMP not null comment '更新时间',
    user_id int unsigned default 0 not null comment '用户id',
    primary key (id),
    key idx_user_id (user_id)
) comment '记账预算表';

alter table bookkeeping_budget
add column budget_month date null comment '预算月份',
add column budget_year smallint unsigned null comment '预算年份';

create table bookkeeping_wallet (
    id int unsigned auto_increment not null comment 'id',
    wallet_balance decimal(10, 2) default 0 not null comment '余额',
    wallet_assets decimal(10, 2) default 0 not null comment '资产',
    deleted tinyint(1) default 0 not null comment '是否删除(true表示已删除, 默认false表示未删除)',
    create_time datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time datetime default CURRENT_TIMESTAMP not null comment '更新时间',
    user_id int unsigned default 0 not null comment '用户id',
    primary key (id),
    key idx_user_id (user_id)
) comment '用户钱包表';

create table bookkeeping_wallet_records (
    id int unsigned auto_increment not null comment 'id',
    change_type tinyint(1) not null comment '变动类型(1余额, 2资产)',
    change_amount decimal(10, 2) not null comment '变动金额',
    before_amount decimal(10, 2) not null comment '变动前金额',
    after_amount decimal(10, 2) not null comment '变动后金额',
    deleted tinyint(1) default 0 not null comment '是否删除(true表示已删除, 默认false表示未删除)',
    create_time datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time datetime default CURRENT_TIMESTAMP not null comment '更新时间',
    user_id int unsigned default 0 not null comment '用户id',
    primary key (id),
    key idx_user_id (user_id)
) comment '用户钱包记录表';

alter table bookkeeping_budget
add column `reward_points` tinyint NOT NULL DEFAULT '0' COMMENT '奖励积分',
add column `punish_points` tinyint NOT NULL DEFAULT '0' COMMENT '处罚积分';

create table bookkeeping_membership_subscription
(
    id              int unsigned auto_increment             not null comment 'id',
    membership_type tinyint       default 0                 not null comment '会员类型',
    membership_name varchar(64)   default ''                not null comment '会员名称',
    amount          decimal(8, 2) default 0                 not null comment '金额',
    billing_cycle   tinyint                                 not null comment '计费周期',
    cycle_num       int                                     null comment '自定义周期间隔数',
    cycle_unit      tinyint                                 null comment '自定义周期单位',
    start_date      date                                    not null comment '开始日期',
    end_date        date                                    null comment '结束日期',
    auto_renew      tinyint(1)    default 0                 not null comment '是否自动续费(true表示开启自动续费, 默认false表示未开启)',
    pay_way         varchar(64)   default ''                not null comment '支付方式',
    remind_days     tinyint                                 null comment '提前提醒天数',
    remark          varchar(255)  default ''                not null comment '备注',
    deleted         tinyint(1)    default 0                 not null comment '是否删除(true表示已删除, 默认false表示未删除)',
    create_time     datetime      default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time     datetime      default CURRENT_TIMESTAMP not null comment '更新时间',
    user_id         int unsigned  default '0'               not null comment '用户id',
    primary key (id),
    key idx_user_id (user_id)
) comment '会员订阅记录表';

alter table bookkeeping_records
    add column group_id int unsigned default 0 not null comment '家庭组ID (0-个人模式)' after user_id,
    add column share_state tinyint(1) default 0 not null comment '共享状态(0不共享 1共享中 2已离组)' after group_id;

alter table bookkeeping_records
    add key idx_group_id (group_id);

create table bookkeeping_voice_parse_log
(
    id               int unsigned auto_increment             not null comment 'id',
    parse_status     varchar(32)   default ''                not null comment '解析状态',
    confirm_status   varchar(32)   default 'UNCONFIRMED'     not null comment '确认状态',
    recognized_text  varchar(1024) default ''                not null comment '原始识别文本',
    audio_format     varchar(16)   default ''                not null comment '音频格式',
    audio_duration_ms int         default 0                  not null comment '音频时长(毫秒)',
    confidence       decimal(5, 2) default 0                 not null comment '解析置信度',
    matched_action_id int unsigned default null              null comment '匹配的记账行为ID',
    confirmed_record_id int unsigned default null            null comment '确认生成的记账记录ID',
    draft_json       text                                   null comment '解析结果草稿JSON',
    warning_json     text                                   null comment '解析警告JSON',
    confirmed_data_json text                                null comment '确认提交数据JSON',
    ai_raw_response  text                                   null comment 'AI原始响应',
    provider         varchar(64)   default ''                not null comment '服务提供方',
    deleted          tinyint(1)    default 0                 not null comment '是否删除(true表示已删除, 默认false表示未删除)',
    create_time      datetime      default CURRENT_TIMESTAMP not null comment '创建时间',
    confirmed_time   datetime                               null comment '确认时间',
    update_time      datetime      default CURRENT_TIMESTAMP not null comment '更新时间',
    user_id          int unsigned  default 0                 not null comment '用户id',
    primary key (id),
    key idx_user_id (user_id)
) comment '语音记账解析日志表';

drop table if exists eat_dishes;
create table eat_dishes
(
    id                int unsigned      not null auto_increment comment 'id',
    dishes_name       varchar(32)       not null comment '菜品名称',
    dishes_image      varchar(255)      not null default '' comment '菜品封面图片',
    dishes_type       tinyint           not null default 0 comment '菜品分类(0:无分类, 1:荤, 2:素, 3:荤素)',
    difficulty_factor tinyint unsigned  not null default 0 comment '难度系数(难度依次递增, 0表示未知难度)',
    use_time          tinyint unsigned  not null default 0 comment '用时(分钟, 0表示未知用时)',
    prices            smallint unsigned not null default 0 comment '价格(元, 0表示免费)',
    status            tinyint           not null default 1 comment '状态(1:启用, 2:禁用, 3:售空)',
    remark            varchar(255)      not null default '' comment '备注',
    deleted           bool              not null default false comment '是否删除(true表示已删除, 默认false表示未删除',
    create_time       datetime          not null default current_timestamp comment '创建时间',
    update_time       datetime          not null default current_timestamp comment '更新时间',
    primary key (id)
) comment '菜品表';


drop table if exists eat_dishes_material;
create table eat_dishes_material
(
    id              int unsigned not null auto_increment comment 'id',
    dishes_id       int unsigned not null comment '菜品id',
    material_name   varchar(16)  not null comment '食材名称',
    material_dosage varchar(16)  not null comment '食材用量',
    is_purchase     tinyint(1) default 0 comment '是否需要购买 0否 1是',
    primary key (id),
    key idx_dishes_id (dishes_id)
) comment '菜品用料表';

create table eat_dishes_creation_method
(
    id           int unsigned     not null auto_increment comment 'id',
    dishes_id    int unsigned     not null comment '菜品id',
    step         tinyint unsigned not null default 0 comment '制作步骤',
    step_image   varchar(255)     not null default '' comment '步骤图片',
    step_content text             null comment '步骤内容',
    primary key (id),
    key idx_dishes_id (dishes_id)
) comment '菜品制作方法表';

drop table if exists eat_meal;
create table eat_meal
(
    id          int unsigned     not null auto_increment comment 'id',
    meal_date   date             not null comment '用餐日期',
    meal_time   tinyint          not null default 0 comment '用餐时间(指规定的吃饭时间，通常包括1早餐、2午餐和3晚餐，0表示未规定用餐时间)',
    diners      tinyint unsigned not null default 0 comment '用餐人数(0表示不确定用餐人数)',
    remark      varchar(255)     not null default '' comment '备注',
    deleted     bool             not null default false comment '是否删除(true表示已删除, 默认false表示未删除',
    create_time datetime         not null default current_timestamp comment '创建时间',
    update_time datetime         not null default current_timestamp comment '更新时间',
    primary key (id),
    key idx_meal_date (meal_date)
) comment '用餐表';

drop table if exists eat_meal_menu;
create table eat_meal_menu
(
    id          int unsigned not null auto_increment comment 'id',
    meal_id     int unsigned not null comment '用餐id',
    dishes_id   int unsigned not null comment '菜品id',
    dishes_name varchar(32)  not null comment '菜品名称',
    primary key (id),
    key idx_meal_id (meal_id)
) comment '用餐菜单表';


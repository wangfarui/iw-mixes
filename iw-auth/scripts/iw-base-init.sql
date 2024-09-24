drop table if exists base_file_record;
create table base_file_record
(
    id          int unsigned not null auto_increment comment 'id',
    file_name   varchar(128) not null comment '文件名称(带后缀)',
    file_hash   binary(32)   not null comment '文件hash二进制值',
    file_uri    varchar(255) not null comment '文件路径',
    file_prefix varchar(128) not null comment '文件前缀',
    file_suffix varchar(16)  not null default '' comment '文件后缀',
    create_time datetime     not null default current_timestamp comment '创建时间',
    primary key (id),
    UNIQUE KEY (file_hash)
) comment '文件上传记录表';

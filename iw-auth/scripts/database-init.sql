create database iw_mixes CHARACTER SET utf8mb4 collate utf8mb4_0900_ai_ci;

CREATE USER 'iw_root'@'localhost' IDENTIFIED BY 'iw@2024';
GRANT ALL PRIVILEGES ON iw_mixes.* TO 'iw_root'@'localhost';
FLUSH PRIVILEGES;

CREATE TABLE iw_auth_user
(
    id              BIGINT                             NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '用户id',
    account         varchar(32)                        not null comment '账号',
    username        varchar(64)                        not null comment '用户名',
    password        varchar(64)                        not null comment '密码',
    create_time     datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    last_login_time datetime                           null comment '最后登录时间',
    key idx_account (account)
) comment '用户表';


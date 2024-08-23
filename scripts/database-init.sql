# 创建 iw_mixes 数据库
create database iw_mixes CHARACTER SET utf8mb4 collate utf8mb4_0900_ai_ci;

## 创建 iw_root 用户用于本地调试
CREATE USER 'iw_root'@'%' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON iw_mixes.* TO 'iw_root'@'%';
FLUSH PRIVILEGES;

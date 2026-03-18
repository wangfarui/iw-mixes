# 待同步脚本清单

## 维护规范
1. 每次后端开发后，只要有“需要人工执行”的脚本或基础设施变更，都必须登记到本文件。
2. 登记范围包括但不限于：数据库 SQL、Redis 初始化/清理、MQ topic/group、配置中心预置数据等。
3. 以后统一在本 Markdown 文件中维护，不再使用 `unsynchronized-scripts.txt`。
4. 本文件只记录“本次尚未同步执行”的内容；执行完成后，由执行人手动删除对应条目。
5. 记录时必须写清楚：日期、模块、类型、来源文件、执行内容/命令、执行说明。
6. 如果脚本文件是追加式初始化文件，不要笼统写“执行整个文件”，要明确写出本次新增的增量语句或命令。

## 2026-03-18

### [待执行][DB][iw-bookkeeping]
- 来源文件: `iw-packaging-parent/iw-bookkeeping/scripts/iw-bookkeeping-init.sql`
- 执行说明: 为记账记录接入家庭组共享数据能力，新增家庭组字段和索引。
- 执行 SQL:

```sql
alter table bookkeeping_records
    add column group_id int unsigned default 0 not null comment '家庭组ID (0-个人模式)' after user_id,
    add column share_state tinyint(1) default 0 not null comment '共享状态(0不共享 1共享中 2已离组)' after group_id;

alter table bookkeeping_records
    add key idx_group_id (group_id);
```

### [待执行][MQ][iw-rocketmq-starter]
- 来源文件: `iw-starter/iw-rocketmq-starter/scripts/topic-init.sh`
- 执行说明: 为家庭组离组/移除成员后的共享数据状态同步新增 topic。
- 执行命令:

```bash
sh bin/mqadmin updatetopic -n localhost:9876 -t family_group -c DefaultCluster
```

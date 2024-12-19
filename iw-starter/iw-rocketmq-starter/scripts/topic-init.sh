
# 创建"积分记录"topic
sh bin/mqadmin updatetopic -n localhost:9876 -t points-records -c DefaultCluster

# 创建"注册新用户"topic
sh bin/mqadmin updatetopic -n localhost:9876 -t register_new_user -c DefaultCluster


# 创建"积分记录"topic
sh bin/mqadmin updatetopic -n localhost:9876 -t points-records -c DefaultCluster

# 创建"注册新用户"topic
sh bin/mqadmin updatetopic -n localhost:9876 -t register_new_user -c DefaultCluster

# 创建"发送短信验证码"topic
sh bin/mqadmin updatetopic -n localhost:9876 -t send_verification_code -c DefaultCluster

# 创建"记账记录"topic
sh bin/mqadmin updatetopic -n localhost:9876 -t bookkeeping_records -c DefaultCluster
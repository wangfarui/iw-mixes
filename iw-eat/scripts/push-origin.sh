#!/bin/bash

# 1. 连接到远程服务器
ssh aliyun183 << 'EOF'
# 2. 检查连接是否成功，若失败则退出并报错
if [ $? -ne 0 ]; then
    echo "连接aliyun183服务器失败"
    exit 1
fi

# 3. 停止java服务并清空目录
cd /root/iw-mixes/iw-eat || exit 1
pid=$(jps -l | grep "iw-eat-0.0.1-SNAPSHOT.jar" | awk '{print $1}')
if [ -n "$pid" ]; then
    kill -9 "$pid"
fi
rm -f ./*

# 4. 退出远程服务器
exit
EOF

# 5. 切换到本地目录并拷贝jar文件到远程服务器
cd /Users/wangfarui/workspaces/wfr/iw-mixes/iw-eat || exit 1
scp ./target/iw-eat-0.0.1-SNAPSHOT.jar aliyun183:/root/iw-mixes/iw-eat/

# 6. 进入远程服务器并给文件授权
ssh aliyun183 << 'EOF'
cd /root/iw-mixes/iw-eat || exit 1
chmod +x iw-eat-0.0.1-SNAPSHOT.jar

# 7. 启动java服务并退出远程服务器
nohup java -jar iw-eat-0.0.1-SNAPSHOT.jar > iw-eat.log 2>&1 &
exit
EOF


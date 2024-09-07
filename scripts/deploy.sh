#!/bin/bash

### 使用方法示例：./deploy.sh iw-eat [version]
### 如果没有指定 version，则默认使用 0.0.1-SNAPSHOT

# 检查第一个参数是否存在
if [ $# -lt 1 ]; then
    echo "Usage: $0 <project-name> [version]"
    exit 1
fi

# 获取参数
PROJECT_NAME=$1
VERSION=${2:-0.0.1-SNAPSHOT}  # 如果没有指定第二个参数，则使用默认值

# 1. 连接到远程服务器
ssh aliyun183 << EOF
# 2. 检查连接是否成功，若失败则退出并报错
if [ $? -ne 0 ]; then
    echo "连接aliyun183服务器失败"
    exit 1
fi

# 3. 检查目录是否存在，不存在则创建
if [ ! -d "/root/iw-mixes/$PROJECT_NAME" ]; then
    mkdir -p /root/iw-mixes/$PROJECT_NAME
fi

# 4. 停止java服务并清空目录
cd /root/iw-mixes/$PROJECT_NAME || exit 1
pid=\$(jps -l | grep "$PROJECT_NAME-$VERSION.jar" | awk '{print \$1}')
if [ -n "\$pid" ]; then
    kill -9 "\$pid"
fi
rm -f ./*

# 5. 退出远程服务器
exit
EOF

# 6. 切换到本地目录并拷贝jar文件到远程服务器
cd /Users/wangfarui/workspaces/wfr/iw-mixes/$PROJECT_NAME || exit 1
scp ./target/$PROJECT_NAME-$VERSION.jar aliyun183:/root/iw-mixes/$PROJECT_NAME/

# 7. 进入远程服务器并给文件授权
ssh aliyun183 << EOF
cd /root/iw-mixes/$PROJECT_NAME || exit 1
chmod +x $PROJECT_NAME-$VERSION.jar

# 8. 启动java服务并退出远程服务器
nohup java -Xms64m -Xmx128m -jar $PROJECT_NAME-$VERSION.jar > $PROJECT_NAME.log 2>&1 &
exit
EOF

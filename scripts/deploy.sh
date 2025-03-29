#!/bin/bash

### 使用方法示例：./deploy.sh -p iw-eat [-v version] [-s server]

# 初始化变量
PROJECT_NAME=""
VERSION="0.1.0-SNAPSHOT"  # 默认版本号
SERVER_NAME="aliyun87" # 默认发布服务器
TARGET_DIR="/root/iw-mixes" # 目标服务器目录
SOURCE_DIR="/Users/wangfarui/workspaces/wfr/iw-mixes/iw-packaging-parent" # 源服务器目录

# 解析命令行参数
while getopts ":p:v:s:" opt; do
  case $opt in
    p)
      PROJECT_NAME=$OPTARG
      ;;
    v)
      VERSION=$OPTARG
      ;;
    s)
      SERVER_NAME=$OPTARG
      ;;
    \?)
      echo "Invalid option: -$OPTARG" >&2
      exit 1
      ;;
    :)
      echo "Option -$OPTARG requires an argument." >&2
      exit 1
      ;;
  esac
done

# 检查是否提供了项目名称
if [ -z "$PROJECT_NAME" ]; then
    echo "Usage: $0 -p <project-name> [-v version]"
    exit 1
fi

# 1. 连接到远程服务器
ssh $SERVER_NAME << EOF

# 2. 检查连接是否成功，若失败则退出并报错
if [ $? -ne 0 ]; then
    echo "连接$SERVER_NAME服务器失败"
    exit 1
fi

# 3. 检查目录是否存在，不存在则创建
if [ ! -d "$TARGET_DIR/$PROJECT_NAME" ]; then
    mkdir -p $TARGET_DIR/$PROJECT_NAME
fi

# 4. 清空目录下的内容
cd $TARGET_DIR/$PROJECT_NAME || exit 1
rm -rf ./*

# 5. 退出远程服务器
exit
EOF

# 6. 切换到本地目录并拷贝jar文件到远程服务器
cd $SOURCE_DIR/$PROJECT_NAME || exit 1
scp ./target/$PROJECT_NAME-$VERSION.jar $SERVER_NAME:$TARGET_DIR/$PROJECT_NAME/

# 7. 进入远程服务器并给文件授权
ssh $SERVER_NAME << EOF
cd $TARGET_DIR/$PROJECT_NAME || exit 1
chmod +x $PROJECT_NAME-$VERSION.jar

# 8. 停止java服务
pid=\$(jps -l | grep "$PROJECT_NAME-$VERSION.jar" | awk '{print \$1}')
if [ -n "\$pid" ]; then
    kill -9 "\$pid"
fi

# 9. 启动java服务并退出远程服务器
nohup java -Xms64m -Xmx128m -jar $PROJECT_NAME-$VERSION.jar > /dev/null 2>&1 &
exit
EOF

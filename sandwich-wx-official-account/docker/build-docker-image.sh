#!/bin/bash

JAR_FILE="sandwich-wx-official-account.jar"
IMAGE_NAME="wx-message"
IMAGE_TAG="1.0.0"

# 检查文件
if [ ! -f "$JAR_FILE" ]; then
  echo "错误: $JAR_FILE 不存在"
  exit 1
fi

# 自动清理旧镜像（仅针对当前项目的悬空镜像）
OLD_IMAGE=$(docker images --filter "dangling=true" --format "{{.ID}} {{.CreatedSince}}" \
           | grep "$IMAGE_NAME" | awk '{print $1}' | head -n 1)

if [ ! -z "$OLD_IMAGE" ]; then
  echo "清理旧镜像: $OLD_IMAGE"
  docker rmi $OLD_IMAGE 2>/dev/null || true
fi

# 构建镜像（强制删除中间容器）
echo "构建镜像: $IMAGE_NAME:$IMAGE_TAG"
docker build --rm -t "$IMAGE_NAME:$IMAGE_TAG" .

# 验证构建结果
if [ $? -eq 0 ]; then
  echo "构建成功"
else
  echo "构建失败"
  exit 1
fi


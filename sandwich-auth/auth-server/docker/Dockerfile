# 基础镜像
FROM openjdk:17-jdk-alpine
LABEL maintainer="维护者 <1344382941@qq.com>"
LABEL description="描述"
WORKDIR /app
COPY auth-server-1.0.0.jar /app/app.jar
COPY application.yml /app/application.yml
# 设置镜像源并安装必要的包（保留 tzdata！）
RUN echo "http://mirrors.aliyun.com/alpine/v3.14/main" > /etc/apk/repositories && \
    echo "http://mirrors.aliyun.com/alpine/v3.14/community" >> /etc/apk/repositories && \
    apk update && \
    apk add --no-cache bash tzdata && \
    # 设置时区
    cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && \
    echo "Asia/Shanghai" > /etc/timezone && \
    # 清理缓存（不要删除 tzdata！）
    rm -rf /var/cache/apk/*
# 开放端口9000
EXPOSE 9088
# 执行命令
ENTRYPOINT ["java","-jar","/app/app.jar"]


#构建镜像 最后面的 . 不要忽略
#docker build -t myapp:1.0.0 .
#创建容器运行   --restart=always 随docker启动而启动
#docker run --restart=always -d -it --name=myapp -p 8082:8081 -v /home/admin/app:/usr/data myapp:1.0.0

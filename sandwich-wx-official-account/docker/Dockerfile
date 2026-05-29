# 基础镜像
FROM openjdk:17-jdk-alpine
LABEL maintainer="维护者 <1344382941@qq.com>"
LABEL description="描述"
WORKDIR /app
COPY sandwich-wx-official-account.jar /app/app.jar
COPY application.yml /app/application.yml
RUN echo "http://mirrors.aliyun.com/alpine/v3.14/main" > /etc/apk/repositories && \
    echo "http://mirrors.aliyun.com/alpine/v3.14/community" >> /etc/apk/repositories && \
    apk update && \
    apk add --no-cache bash tzdata && \
    # 设置时区
    cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && \
    echo "Asia/Shanghai" > /etc/timezone && \
    # 清理缓存（不要删除 tzdata！）
    rm -rf /var/cache/apk/*
# 开放端口
EXPOSE 3012
# 执行命令
ENTRYPOINT ["java", "-Djasypt.encryptor.password=xxx", "-jar", "/app/app.jar"]

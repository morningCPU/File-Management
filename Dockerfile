# 使用官方Java运行时作为父镜像
FROM eclipse-temurin:17-jre-alpine

# 设置工作目录
WORKDIR /app

# 复制本地构建好的jar包到容器中
COPY target/readword-1.0-SNAPSHOT.jar app.jar

# 复制配置文件（可选，如果需要自定义配置）
COPY src/main/resources/application.properties .

# 设置时区
ENV TZ=Asia/Shanghai
RUN apk --no-cache add tzdata && \
    ln -sf /usr/share/zoneinfo/${TZ} /etc/localtime && \
    echo ${TZ} > /etc/timezone

# 暴露应用监听端口
EXPOSE 8080

# 启动应用
ENTRYPOINT ["java", "-jar", "app.jar"]
# 使用 Alpine 镜像（轻量但需处理时区）
FROM openjdk:17-jdk-alpine
# 设置时区（Alpine 专用）
ENV TZ=Asia/Shanghai
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo "Asia/Shanghai" > /etc/timezone

         # 创建目标目录并复制 JAR
RUN mkdir -p /ShoppingMall-Backend
COPY ./target/ShoppingMall-Backend-0.0.1-SNAPSHOT.jar /ShoppingMall-Backend/app.jar

         # 暴露端口
EXPOSE 8081

         # 启动命令（已修复时区参数）
ENTRYPOINT ["java", \
             "-Dfile.encoding=UTF-8", \
                 "-Dsun.jnu.encoding=UTF-8", \
                     "-Duser.timezone=Asia/Shanghai", \
                         "-jar", "/ShoppingMall-Backend/app.jar"]

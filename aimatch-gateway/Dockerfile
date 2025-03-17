FROM openjdk:8-jdk-alpine
###复制文件到容器app-springboot
ADD target/aimatch-gateway-1.0-SNAPSHOT.jar /aimatch-gateway.jar
###声明启动端口号
EXPOSE 9000
###配置容器启动后执行的命令
ENTRYPOINT ["java","-jar","/aimatch-gateway.jar"]
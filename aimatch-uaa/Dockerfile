FROM openjdk:8-jdk-alpine
###复制文件到容器app-springboot
ADD aimatch-uaa-1.0-SNAPSHOT.jar /aimatch-uaa.jar
###声明启动端口号
EXPOSE 9001
###配置容器启动后执行的命令
ENTRYPOINT ["java","-jar","/aimatch-uaa.jar"]
 # AI Match 微服务项目

## 项目结构
```
aimatch-cloud
├── aimatch-gateway -- 网关服务 [9000]
├── aimatch-uaa -- 认证和授权服务 [9001]
└── aimatch-business -- 业务服务 [9002]
```

## 技术栈
- 框架：Spring Boot 2.7.x
- 微服务：Spring Cloud 2021.0.x
- 注册中心：Nacos
- 数据库：MySQL 8.0
- 缓存：Redis
- ORM框架：MyBatis-Plus
- API文档：Swagger 3.0
- 项目管理：Maven
- 认证方式：JWT Token

## 环境要求
- JDK 1.8+
- Maven 3.6+
- MySQL 8.0+
- Redis 6.0+
- Nacos 2.0+

## 快速开始

### 1. 环境准备
- 安装并启动 MySQL
- 安装并启动 Redis
- 下载并启动 Nacos

### 2. 数据库初始化
创建数据库：
```sql
CREATE DATABASE aimatch_uaa DEFAULT CHARACTER SET utf8mb4;
CREATE DATABASE aimatch_business DEFAULT CHARACTER SET utf8mb4;
```

### 3. 修改配置
根据实际环境修改各服务的配置文件（application.yml）中的数据库、Redis和Nacos地址。

### 4. 编译打包
```bash
mvn clean package
```

### 5. 启动服务
按照以下顺序启动服务：
1. 启动Nacos
2. 启动aimatch-gateway
3. 启动aimatch-uaa
4. 启动aimatch-business

### 6. 访问服务
- 网关服务：http://localhost:9000
- UAA服务：http://localhost:9001
- 业务服务：http://localhost:9002
- Swagger文档：
  - UAA服务：http://localhost:9001/swagger-ui/
  - 业务服务：http://localhost:9002/swagger-ui/

## 项目维护
- 代码规范：遵循阿里巴巴Java开发规范
- 版本控制：使用Git进行版本管理
- 接口文档：使用Swagger自动生成API文档

## 注意事项
1. 首次运行需要确保Nacos、MySQL、Redis服务均已启动
2. 各服务配置文件中的连接信息需要根据实际环境进行修改
3. 建议使用IDEA等IDE工具进行开发
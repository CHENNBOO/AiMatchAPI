# AiMatchAPI

AiMatchAPI是一个基于Spring Cloud的微服务项目，提供AI匹配相关的API服务。

## 项目结构
```
aimatch-cloud
├── aimatch-gateway    # 网关服务
├── aimatch-uaa       # 认证授权服务
└── aimatch-business  # 业务服务
```

## 技术栈

- **基础框架**：
  - Spring Boot 2.7.17
  - Spring Cloud 2021.0.8
  - Spring Cloud Alibaba 2021.0.5.0

- **服务治理**：
  - Nacos 2.5.1 (服务注册与配置中心)

- **数据访问**：
  - MyBatis Plus 3.5.3.1
  - MySQL 5.1.47

- **安全认证**：
  - JWT 0.9.1

- **API文档**：
  - Swagger 3.0.0

- **开发环境**：
  - JDK 1.8
  - Maven 3.8.1

## 快速开始

1. 克隆项目
```bash
git clone https://github.com/CHENNBOO/AiMatchAPI.git
```

2. 配置环境
- 确保已安装JDK 1.8和Maven 3.8.1
- 配置Nacos服务
- 配置MySQL数据库

3. 编译运行
```bash
mvn clean package
```

## 项目说明

- `aimatch-gateway`: 网关服务，负责请求路由和过滤
- `aimatch-uaa`: 认证授权服务，负责用户认证和权限管理
- `aimatch-business`: 业务服务，提供核心业务功能

## 贡献指南

欢迎提交Issue和Pull Request来帮助改进项目。

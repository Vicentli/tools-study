# Tools Study - Spring Boot学习项目

这是一个Spring Boot学习项目，集成了多种常用技术和工具类，适合学习和实践。

## 🚀 技术栈

- **后端框架**: Spring Boot 3.2.0
- **数据库**: H2 (开发环境) / MySQL (生产环境)
- **缓存**: Redis
- **认证**: JWT (JSON Web Token)
- **安全**: Spring Security
- **ORM**: Spring Data JPA
- **工具库**: Hutool、Apache Commons
- **构建工具**: Maven
- **Java版本**: 21

## 📋 功能特性

- ✅ JWT用户认证和授权
- ✅ 用户注册和登录
- ✅ Redis缓存操作
- ✅ H2内存数据库
- ✅ 统一响应格式
- ✅ 跨域配置
- ✅ 安全配置

## 🛠️ 快速开始

### 环境要求

- JDK 21+
- Maven 3.8+
- Redis (可选，用于缓存功能)

### 1. 克隆项目

```bash
git clone https://github.com/你的用户名/tools-study.git
cd tools-study
```

### 2. 配置数据库

#### 开发环境 (使用H2内存数据库)
项目默认使用H2内存数据库，无需额外配置。

#### 生产环境 (使用MySQL)
1. 创建MySQL数据库
```sql
CREATE DATABASE tools_study CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. 修改 `application.yml` 中的数据库配置：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/tools_study?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
    username: your_username
    password: your_password
```

### 3. 配置Redis (可选)
如果本地没有Redis，可以在 `application-dev.yml` 中禁用Redis功能。

### 4. 运行项目

```bash
# 使用Maven运行
mvn spring-boot:run

# 或者打包后运行
mvn clean package
java -jar target/tools-study-1.0.0.jar
```

### 5. 访问应用

- 应用地址: http://localhost:8080/api
- H2控制台: http://localhost:8080/api/h2-console (开发环境)

## 📚 API文档

### 认证相关接口

#### 用户注册
```http
POST /api/auth/register
Content-Type: application/json

{
  "username": "testuser",
  "password": "123456",
  "email": "test@example.com",
  "nickname": "测试用户"
}
```

#### 用户登录
```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "testuser",
  "password": "123456"
}
```

#### 获取用户信息
```http
GET /api/auth/profile
Authorization: Bearer your_jwt_token
```

#### 退出登录
```http
POST /api/auth/logout
Authorization: Bearer your_jwt_token
```

### 测试接口

#### Redis测试
```http
GET /api/redis/test
```

#### 通用测试
```http
GET /api/test/hello
```

## 🔧 配置说明

### JWT配置
在 `application.yml` 中配置JWT相关参数：
```yaml
app:
  jwt:
    secret: your-secret-key-here  # JWT密钥
    expiration: 86400000          # Token过期时间(毫秒)
```

### Redis配置
```yaml
spring:
  data:
    redis:
      host: localhost
      port: 6379
      password: 
      database: 0
```

## 📁 项目结构

```
src/main/java/com/study/toolsstudy/
├── common/          # 通用类
│   └── Result.java  # 统一响应格式
├── config/          # 配置类
│   ├── RedisConfig.java
│   └── SecurityConfig.java
├── controller/      # 控制器
│   ├── AuthController.java
│   ├── RedisTestController.java
│   └── TestController.java
├── dto/            # 数据传输对象
│   ├── AuthResponse.java
│   ├── LoginRequest.java
│   └── RegisterRequest.java
├── entity/         # 实体类
│   └── User.java
├── repository/     # 数据访问层
│   └── UserRepository.java
├── service/        # 业务逻辑层
│   └── AuthService.java
├── utils/          # 工具类
│   ├── JwtUtils.java
│   └── RedisUtils.java
└── ToolsStudyApplication.java
```

## 🎯 学习要点

1. **Spring Boot基础**: 项目配置、自动配置、依赖注入
2. **Spring Security**: 安全配置、JWT认证、权限控制
3. **Spring Data JPA**: 实体映射、数据访问、事务管理
4. **Redis集成**: 缓存配置、数据操作
5. **JWT实现**: Token生成、验证、解析
6. **统一响应**: 异常处理、响应格式标准化

## 🤝 贡献

欢迎提交Issue和Pull Request来改进这个项目！

## 📄 许可证

MIT License

## 📞 联系方式

如有问题，请通过以下方式联系：
- 邮箱: li.gen334@iwhalecloud.com
- GitHub: [你的GitHub用户名] 
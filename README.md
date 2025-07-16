# Tools Study - Spring Boot 学习项目

这是一个基于 Spring Boot 3.2.0 的学习项目，包含了各种常用的工具类和功能模块。

## 项目结构

```
src/
├── main/
│   ├── java/
│   │   └── com/study/toolsstudy/
│   │       ├── ToolsStudyApplication.java    # 主启动类
│   │       ├── config/                       # 配置类
│   │       │   └── RedisConfig.java         # Redis配置
│   │       ├── controller/                   # 控制器
│   │       │   └── RedisTestController.java # Redis测试控制器
│   │       ├── service/                      # 服务层
│   │       ├── repository/                   # 数据访问层
│   │       ├── entity/                       # 实体类
│   │       ├── utils/                        # 工具类
│   │       │   └── RedisUtils.java          # Redis工具类
│   │       └── common/                       # 通用类
│   │           └── Result.java              # 统一响应结果
│   └── resources/
│       ├── application.yml                   # 主配置文件
│       └── application-dev.yml              # 开发环境配置
└── test/                                     # 测试目录
```

## 技术栈

- **Spring Boot**: 3.2.0
- **Java**: 17
- **Maven**: 项目构建工具
- **Redis**: 缓存数据库
- **MySQL**: 关系型数据库
- **JPA**: 数据持久化
- **Lombok**: 简化代码
- **Hutool**: 工具包
- **JWT**: 身份认证

## 主要功能

### 1. Redis 工具类 (RedisUtils)
提供了完整的 Redis 操作封装，包括：
- 字符串操作 (set, get, incr, decr)
- Hash 操作 (hset, hget, hmset, hmget)
- List 操作 (lSet, lGet, lRemove)
- Set 操作 (sSet, sGet, setRemove)
- 通用操作 (expire, hasKey, del)

### 2. 统一响应结果 (Result)
标准化的 API 响应格式，包含：
- 响应码
- 响应消息
- 响应数据
- 时间戳

### 3. Redis 测试接口
提供了完整的 Redis 操作测试接口：
- `POST /api/redis/set` - 设置缓存
- `GET /api/redis/get/{key}` - 获取缓存
- `DELETE /api/redis/del/{key}` - 删除缓存
- `GET /api/redis/exists/{key}` - 检查key是否存在
- `POST /api/redis/hset` - 设置Hash缓存
- `GET /api/redis/hget/{key}/{field}` - 获取Hash缓存
- `GET /api/redis/hgetall/{key}` - 获取所有Hash字段
- `POST /api/redis/incr/{key}` - 递增
- `POST /api/redis/decr/{key}` - 递减
- `POST /api/redis/expire/{key}` - 设置过期时间
- `GET /api/redis/ttl/{key}` - 获取过期时间

## 快速开始

### 1. 环境要求
- JDK 17+
- Maven 3.6+
- Redis 6.0+
- MySQL 8.0+

### 2. 配置数据库
修改 `src/main/resources/application.yml` 中的数据库配置：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/tools_study?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
    username: your_username
    password: your_password
```

### 3. 配置Redis
修改 Redis 配置：
```yaml
spring:
  data:
    redis:
      host: localhost
      port: 6379
      password: your_password
      database: 0
```

### 4. 启动项目
```bash
# 编译项目
mvn clean compile

# 运行项目
mvn spring-boot:run
```

### 5. 测试接口
项目启动后，访问 `http://localhost:8080/api/redis/` 下的各个接口进行测试。

## 开发计划

- [ ] JWT 工具类
- [ ] 文件上传工具类
- [ ] 邮件发送工具类
- [ ] 短信发送工具类
- [ ] 定时任务工具类
- [ ] 数据验证工具类
- [ ] 加密解密工具类
- [ ] Excel 导入导出工具类
- [ ] PDF 生成工具类
- [ ] 二维码生成工具类

## 贡献

欢迎提交 Issue 和 Pull Request 来完善这个项目。

## 许可证

MIT License 
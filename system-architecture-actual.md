# 阿尔茨海默病平台实际技术栈分析

## 1. 项目结构分析

根据pom文件分析，项目采用Maven多模块结构，包含以下模块：
- 父模块：AlzheimerPlatform
- 子模块：AlzheimerClient、untitled

## 2. 后端技术栈分析

### 2.1 核心框架
- **Spring Boot 3.1.3**: 项目基于Spring Boot 3.x构建，采用最新的稳定版本
- **Spring MVC**: 通过spring-boot-starter-web包提供Web开发支持

### 2.2 数据访问层
- **MySQL 8.0.33**: 关系型数据库
- **MyBatis Plus 3.5.3.1**: 增强的MyBatis ORM框架，提供CRUD操作简化
- **Druid 1.2.8**: 阿里巴巴开源的数据库连接池，提供监控功能
- **Spring JDBC**: 基础数据库操作支持
- **Redis**: 通过spring-boot-starter-data-redis提供缓存支持

### 2.3 安全与认证
- **Java JWT 4.4.0**: JWT(JSON Web Token)实现，用于身份验证和授权

### 2.4 工具类库
- **Lombok**: 简化Java代码编写，通过注解自动生成getter/setter等
- **Apache Commons Lang3 3.12.0**: 提供丰富的工具类
- **Commons IO 2.11.0**: 文件操作工具类
- **FastJSON2 2.0.43**: 阿里巴巴JSON处理库
- **HuTool 5.8.11**: 国产工具包，提供丰富的工具方法

### 2.5 API文档
- **Knife4j 4.0.0**: 基于Swagger的API文档生成工具，增强UI

### 2.6 数据校验
- **Jakarta Validation API**: Bean验证框架
- **Spring Boot Validation**: 参数校验支持

### 2.7 分页支持
- **PageHelper 1.4.6**: MyBatis分页插件

### 2.8 云服务
- **Aliyun OSS 3.15.1**: 阿里云对象存储服务，用于文件存储

### 2.9 AOP支持
- **AspectJ**: 面向切面编程支持，用于日志、权限等横切关注点

### 2.10 AI集成
- **科大讯飞语音听写SDK 3.0.0**: 提供语音识别功能

## 3. 实际系统架构

基于依赖分析，项目实际采用的是**单体应用架构**，而非微服务架构。主要特点：

1. **分层结构**:
   - 表现层 (Controller)
   - 业务逻辑层 (Service)
   - 数据访问层 (DAO/Mapper)
   - 实体层 (Entity/Domain)

2. **单一应用部署**:
   - 所有模块打包为一个单独的JAR/WAR文件
   - 不依赖服务注册与发现
   - 不依赖Spring Cloud组件

3. **数据存储**:
   - MySQL作为主数据库
   - Redis作为缓存
   - 阿里云OSS作为文件存储

## 4. 前端技术架构推测

根据之前的需求分析，前端可能采用:
- **Vue 3**: 前端框架
- **Element Plus**: UI组件库

但未找到前端项目的package.json文件，无法确认具体版本和依赖。

## 5. RESTful接口设计

从Controller分析，项目采用REST风格的API设计:
- 使用合适的HTTP方法 (GET, POST, PUT, PATCH, DELETE)
- 采用Result统一返回格式
- 区分DTO、VO和实体类
- 使用Knife4j提供API文档

## 6. 数据流程

```
+-------------------+       +-------------------+      +-------------------+
|                   |       |                   |      |                   |
|  前端应用(Vue3)   |------>|  后端应用(Spring) |----->|  数据库(MySQL)    |
|                   |       |                   |      |                   |
+-------------------+       +-------------------+      +-------------------+
                                    |
                                    v
                            +-------------------+
                            |                   |
                            |  缓存(Redis)      |
                            |                   |
                            +-------------------+
                                    |
                                    v
                            +-------------------+
                            |                   |
                            |  文件存储(阿里云) |
                            |                   |
                            +-------------------+
```

## 7. 安全架构

1. **身份认证**: 基于JWT实现无状态认证
2. **数据校验**: 使用Jakarta Validation进行参数验证
3. **安全传输**: 支持HTTPS配置(建议项目实际部署时配置)



## 8. 特色功能

1. **阿尔茨海默病患者管理**: 专门的患者信息管理和认证
2. **医生-患者绑定关系管理**: 支持医患关联
3. **机构管理**: 医疗机构分类和管理
4. **语音识别**: 集成科大讯飞SDK提供语音功能

## 9. 优化建议

1. **考虑引入缓存框架**: 如Spring Cache抽象，方便缓存管理
2. **统一异常处理**: 使用@ControllerAdvice全局异常处理
3. **日志系统**: 引入更完善的日志框架如Logback并考虑ELK整合
4. **安全加固**: 考虑引入Spring Security进行更全面的安全防护
5. **监控能力**: 引入Spring Boot Actuator提供健康检查和监控端点 
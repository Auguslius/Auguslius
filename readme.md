# 阿尔茨海默病平台API文档

## 文档概述

本文档提供了阿尔茨海默病平台的所有API接口说明，包括接口描述、请求参数、响应格式、错误码等信息。本文档旨在帮助开发人员快速理解和使用平台提供的各项功能。

![1747125309231](https://github.com/Auguslius/Auguslius/blob/main/%E5%9B%BE%E7%89%873.jpg)![1747125317943](https://github.com/Auguslius/Auguslius/blob/main/%E5%9B%BE%E7%89%875.jpg)

## 文档结构

本文档按功能模块及系统类型进行组织，分为客户端接口和后台管理系统接口两大类：

### 客户端接口（Client端）

1. [登录模块接口](./login-api.md) - 用户认证、验证码、令牌管理等接口
2. [患者认证模块](./patient-authentication-api.md) - 患者实名认证相关接口
3. [机构医生管理模块](./institution-doctor-api.md) - 机构医生关联及绑定接口
4. [MMSE答题模块](./mmse-answer-api.md) - 患者MMSE测试答题相关接口

### 后台管理系统接口（Admin端）

1. [管理员登录接口](./admin-login-api.md) - 管理员认证及权限控制接口
2. [用户管理模块](./user-api.md) - 用户注册、信息管理等接口
3. [患者管理模块](./patient-api.md) - 患者信息管理、统计分析等接口
4. [病历记录管理模块](./medical-record-api.md) - 病历记录的增删改查等接口
5. [医疗机构管理模块](./institution-api.md) - 医疗机构信息管理接口
6. [机构种类管理模块](./institution-category-api.md) - 医疗机构分类管理接口
7. [MMSE量表问题管理模块](./mmse-question-api.md) - MMSE量表问题管理接口
8. [语音转文本比对模块](./audio-text-comparison-api.md) - 音频转文字及文本相似度比对接口
9. [医生管理模块](./doctor-api.md) - 医生信息管理及审核接口
10. [患者审核模块](./patient-review-api.md) - 患者信息审核及管理接口
11. [系统配置模块](./system-config-api.md) - 系统参数配置及维护接口

### 通用接口

1. [媒体文件管理](./media-api.md) - 文件上传、下载、图片处理等接口
2. [通用数据接口](./common-api.md) - 字典数据、基础信息等通用接口

## 接口规范

### 请求格式

- 所有接口均采用RESTful风格设计
- 请求方法包括：GET、POST、PUT、DELETE、PATCH
- 请求参数支持：路径参数、查询参数、请求体参数
- 请求体格式：application/json、application/x-www-form-urlencoded、multipart/form-data

### 状态码说明

- 200: 成功
- 400: 请求参数错误
- 401: 未授权
- 403: 禁止访问
- 404: 资源不存在
- 500: 服务器内部错误

## 认证方式

除了登录、注册和密码重置相关接口外，所有接口都需要进行身份验证。认证方式为：

- 在请求头中添加 `Authorization` 字段，值为访问令牌
- 令牌格式：`Bearer [token]` 或直接为 token 字符串

## 系统架构

系统采用前后端分离架构：

- **前端**：基于Vue3开发，使用ElementPlus组件库
- **后端**：基于SpringBoot 3.1.3开发，采用RESTful API设计风格
- **数据库**：MySQL 8.0.33，配合MyBatis-Plus作为ORM框架
- **缓存**：Redis用于存储验证码、令牌等临时数据
- **认证**：JWT (JSON Web Token) 实现无状态认证

详细技术架构请参考[系统架构文档](./system-architecture-actual.md)。

## 数据模型说明

系统采用分层数据模型设计：

- **实体类(Entity)**：与数据库表结构一一对应
- **DTO类(Data Transfer Object)**：用于数据传输，通常用于接收前端请求数据
- **VO类(View Object)**：用于视图展示，通常用于返回给前端的数据

## 使用指南

1. 首先阅读[登录模块接口](./login-api.md)文档，了解如何获取验证码和登录系统
2. 根据业务需求，选择相应的功能模块文档进行查阅
3. 按照接口文档中的请求格式和参数要求进行开发
4. 处理接口返回的状态码和错误信息


## 联系方式

如有任何问题或建议，请联系：

- email：1909221500@qq.com


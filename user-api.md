# 用户管理模块接口文档

## 1. 用户认证接口

### 1.1 用户登录

#### 接口描述
用户登录系统，获取访问令牌。

#### 请求信息
- **URL**: `/user/login`
- **方法**: POST
- **Content-Type**: application/x-www-form-urlencoded

#### 请求参数
- **username**: 用户名（必填，5-16位非空字符）
- **password**: 密码（必填，5-16位非空字符）

#### 响应信息
```json
{
  "code": "integer",    // 状态码
  "message": "string",  // 响应消息
  "data": "string"      // 访问令牌
}
```

### 1.2 用户退出登录

#### 接口描述
用户退出登录，使当前令牌失效。

#### 请求信息
- **URL**: `/user/logout`
- **方法**: POST
- **Content-Type**: application/json

#### 请求参数
```json
{
  "token": "string"  // 访问令牌
}
```

#### 响应信息
```json
{
  "code": "integer",    // 状态码
  "message": "string",  // 响应消息
  "data": "string"      // 退出结果
}
```

### 1.3 用户注册

#### 接口描述
新用户注册系统账号。

#### 请求信息
- **URL**: `/user/register`
- **方法**: POST
- **Content-Type**: application/x-www-form-urlencoded

#### 请求参数
- **username**: 用户名（必填，5-16位非空字符）
- **password**: 密码（必填，5-16位非空字符）

#### 响应信息
```json
{
  "code": "integer",    // 状态码
  "message": "string",  // 响应消息
  "data": "boolean"     // 是否注册成功
}
```

### 1.4 获取当前用户信息

#### 接口描述
获取当前登录用户的详细信息。

#### 请求信息
- **URL**: `/user/userInfo`
- **方法**: GET
- **请求头**:
  - Authorization: 访问令牌

#### 响应信息
```json
{
  "code": "integer",    // 状态码
  "message": "string",  // 响应消息
  "data": {
    "id": "integer",           // 用户ID
    "username": "string",      // 用户名
    "nickname": "string",      // 昵称
    "avatar": "string",        // 头像URL
    "email": "string",         // 电子邮箱
    "phone": "string",         // 手机号码
    "role": "string",          // 用户角色
    "status": "integer",       // 用户状态
    "createTime": "string",    // 创建时间
    "updateTime": "string"     // 更新时间
  }
}
```

## 2. 用户管理接口

### 2.1 新增用户

#### 接口描述
管理员添加新用户。

#### 请求信息
- **URL**: `/user/save`
- **方法**: PUT
- **Content-Type**: application/json

#### 请求参数
```json
{
  "username": "string",      // 用户名
  "password": "string",      // 密码
  "nickname": "string",      // 昵称
  "email": "string",         // 电子邮箱
  "phone": "string",         // 手机号码
  "role": "string",          // 用户角色
  "status": "integer"        // 用户状态
}
```

#### 响应信息
```json
{
  "code": "integer",    // 状态码
  "message": "string",  // 响应消息
  "data": "boolean"     // 是否添加成功
}
```

### 2.2 更新用户信息

#### 接口描述
更新用户信息。

#### 请求信息
- **URL**: `/user/update`
- **方法**: PATCH
- **Content-Type**: application/json

#### 请求参数
```json
{
  "id": "integer",           // 用户ID
  "nickname": "string",      // 昵称
  "email": "string",         // 电子邮箱
  "phone": "string",         // 手机号码
  "role": "string",          // 用户角色
  "status": "integer"        // 用户状态
}
```

#### 响应信息
```json
{
  "code": "integer",    // 状态码
  "message": "string",  // 响应消息
  "data": "boolean"     // 是否更新成功
}
```

### 2.3 删除用户

#### 接口描述
根据用户ID删除用户。

#### 请求信息
- **URL**: `/user/{id}`
- **方法**: DELETE
- **参数**: 
  - id: 用户ID（路径参数）

#### 响应信息
```json
{
  "code": "integer",    // 状态码
  "message": "string",  // 响应消息
  "data": "null"        // 无返回数据
}
```

### 2.4 用户分页条件查询

#### 接口描述
根据条件分页查询用户信息。

#### 请求信息
- **URL**: `/user/page`
- **方法**: GET
- **参数**:
  - pageNum: 页码（默认1）
  - pageSize: 每页条数（默认10）
  - username: 用户名（可选）
  - nickname: 昵称（可选）
  - email: 电子邮箱（可选）
  - phone: 手机号码（可选）
  - role: 用户角色（可选）
  - status: 用户状态（可选）

#### 响应信息
```json
{
  "code": "integer",    // 状态码
  "message": "string",  // 响应消息
  "data": {
    "total": "integer",     // 总记录数
    "pages": "integer",     // 总页数
    "current": "integer",   // 当前页码
    "size": "integer",      // 每页条数
    "records": [           // 用户列表
      {
        "id": "integer",           // 用户ID
        "username": "string",      // 用户名
        "nickname": "string",      // 昵称
        "avatar": "string",        // 头像URL
        "email": "string",         // 电子邮箱
        "phone": "string",         // 手机号码
        "role": "string",          // 用户角色
        "status": "integer",       // 用户状态
        "createTime": "string",    // 创建时间
        "updateTime": "string"     // 更新时间
      }
    ]
  }
}
```

### 2.5 更新用户头像

#### 接口描述
更新当前用户的头像。

#### 请求信息
- **URL**: `/user/updateAvatar`
- **方法**: PATCH
- **Content-Type**: application/x-www-form-urlencoded
- **参数**:
  - avatarUrl: 头像URL（必填，有效的URL格式）

#### 响应信息
```json
{
  "code": "integer",    // 状态码
  "message": "string",  // 响应消息
  "data": "boolean"     // 是否更新成功
}
```

## 3. 密码重置接口

### 3.1 请求密码重置

#### 接口描述
用户请求重置密码，系统发送验证码。

#### 请求信息
- **URL**: `/password-reset/requestPwd`
- **方法**: POST
- **Content-Type**: application/json

#### 请求参数
```json
{
  "username": "string",  // 用户名
  "email": "string"      // 电子邮箱
}
```

#### 响应信息
```json
{
  "code": "integer",    // 状态码
  "message": "string",  // 响应消息
  "data": "string"      // 重置令牌
}
```

### 3.2 验证重置密码请求

#### 接口描述
验证密码重置请求的有效性。

#### 请求信息
- **URL**: `/password-reset/validate`
- **方法**: GET
- **参数**:
  - token: 重置令牌（必填）

#### 响应信息
```json
{
  "code": "integer",    // 状态码
  "message": "string",  // 响应消息
  "data": "boolean"     // 是否验证成功
}
```

### 3.3 重置密码

#### 接口描述
使用重置令牌设置新密码。

#### 请求信息
- **URL**: `/password-reset/reset`
- **方法**: POST
- **Content-Type**: application/json

#### 请求参数
```json
{
  "token": "string",     // 重置令牌
  "newPassword": "string" // 新密码
}
```

#### 响应信息
```json
{
  "code": "integer",    // 状态码
  "message": "string",  // 响应消息
  "data": "boolean"     // 是否重置成功
}
```

## 数据字典

### 状态码说明
- 200: 成功
- 400: 请求参数错误
- 401: 未授权
- 403: 禁止访问
- 404: 资源不存在
- 500: 服务器内部错误

### 用户角色枚举
- ADMIN: 管理员
- DOCTOR: 医生
- PATIENT: 患者
- GUARDIAN: 监护人

### 用户状态枚举
- 0: 禁用
- 1: 正常

## 注意事项
1. 所有接口都需要进行身份验证（除了登录、注册和密码重置相关接口）
2. 密码必须符合安全要求，建议使用强密码
3. 用户信息涉及隐私，请确保数据传输安全
4. 分页查询接口支持多条件组合查询
5. 密码重置令牌有效期为30分钟
6. 用户头像URL必须是有效的URL格式 
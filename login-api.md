# 登录模块接口文档

## 1. 获取图片验证码

### 接口描述
获取登录验证码图片和唯一标识。

### 请求信息
- **URL**: `/login/code`
- **方法**: GET

### 响应信息
```json
{
  "code": "integer",    // 状态码
  "message": "string",  // 响应消息
  "data": {
    "captchaId": "string",  // 验证码唯一标识
    "captchaImage": "string" // Base64编码的验证码图片
  }
}
```

### 错误码
- 获取成功：SUCCESS
- 获取失败：GET_CAPTCHA_FAIL

## 2. 用户名密码登录

### 接口描述
使用用户名和密码进行登录。

### 请求信息
- **URL**: `/login/password`
- **方法**: POST
- **Content-Type**: application/json

### 请求参数
```json
{
  "username": "string",     // 用户名（必填）
  "password": "string",     // 密码（必填）
  "captchaId": "string",    // 验证码唯一标识（必填）
  "captchaCode": "string"   // 验证码（必填）
}
```

### 响应信息
```json
{
  "code": "integer",    // 状态码
  "message": "string",  // 响应消息
  "data": {
    "token": "string",           // 访问令牌
    "userId": "string",          // 用户ID
    "username": "string",        // 用户名
    "nickname": "string",        // 昵称
    "avatar": "string",          // 头像URL
    "role": "string",            // 用户角色
    "permissions": ["string"],   // 权限列表
    "expireTime": "string"       // 令牌过期时间
  }
}
```

### 错误码
- 登录成功：SUCCESS
- 登录失败：LOGIN_FAIL
- 用户名或密码错误：USERNAME_PASSWORD_ERROR
- 验证码错误：CAPTCHA_ERROR
- 验证码已过期：CAPTCHA_EXPIRED
- 账号被禁用：ACCOUNT_DISABLED

## 3. 用户注册

### 接口描述
新用户注册系统账号。

### 请求信息
- **URL**: `/login/register`
- **方法**: POST
- **Content-Type**: application/json

### 请求参数
```json
{
  "username": "string",     // 用户名（必填，5-16位非空字符）
  "password": "string",     // 密码（必填，5-16位非空字符）
  "confirmPassword": "string", // 确认密码（必填，与密码一致）
  "nickname": "string",     // 昵称（可选）
  "email": "string",        // 电子邮箱（可选）
  "phone": "string",        // 手机号码（可选）
  "captchaId": "string",    // 验证码唯一标识（必填）
  "captchaCode": "string"   // 验证码（必填）
}
```

### 响应信息
```json
{
  "code": "integer",    // 状态码
  "message": "string",  // 响应消息
  "data": {
    "userId": "string",          // 用户ID
    "username": "string",        // 用户名
    "nickname": "string",        // 昵称
    "createTime": "string"       // 创建时间
  }
}
```

### 错误码
- 注册成功：SUCCESS
- 注册失败：REGISTER_FAIL
- 用户名已存在：USERNAME_EXISTS
- 密码不一致：PASSWORD_NOT_MATCH
- 验证码错误：CAPTCHA_ERROR
- 验证码已过期：CAPTCHA_EXPIRED
- 参数验证失败：PARAMETER_VALIDATION_FAILED

## 4. 获取当前登录用户

### 接口描述
获取当前登录用户的详细信息。

### 请求信息
- **URL**: `/login/current`
- **方法**: GET
- **请求头**:
  - Authorization: 访问令牌

### 响应信息
```json
{
  "code": "integer",    // 状态码
  "message": "string",  // 响应消息
  "data": {
    "userId": "string",          // 用户ID
    "username": "string",        // 用户名
    "nickname": "string",        // 昵称
    "avatar": "string",          // 头像URL
    "email": "string",           // 电子邮箱
    "phone": "string",           // 手机号码
    "role": "string",            // 用户角色
    "permissions": ["string"],   // 权限列表
    "status": "integer",         // 用户状态
    "lastLoginTime": "string",   // 最后登录时间
    "createTime": "string"       // 创建时间
  }
}
```

### 错误码
- 查询成功：SUCCESS
- 查询失败：GET_CURRENT_USER_FAIL
- 未登录：NOT_LOGIN
- 令牌已过期：TOKEN_EXPIRED
- 令牌无效：TOKEN_INVALID

## 5. 退出登录

### 接口描述
用户退出登录，使当前令牌失效。

### 请求信息
- **URL**: `/login/logout`
- **方法**: POST
- **请求头**:
  - Authorization: 访问令牌

### 响应信息
```json
{
  "code": "integer",    // 状态码
  "message": "string",  // 响应消息
  "data": null          // 无返回数据
}
```

### 错误码
- 退出成功：SUCCESS
- 退出失败：LOGOUT_FAIL
- 未登录：NOT_LOGIN
- 令牌已过期：TOKEN_EXPIRED
- 令牌无效：TOKEN_INVALID

## 数据字典

### 用户角色说明
- ADMIN: 管理员
- DOCTOR: 医生
- PATIENT: 患者
- GUARDIAN: 监护人

### 用户状态说明
- 0: 禁用
- 1: 正常

### 权限说明
- user:view: 查看用户信息
- user:add: 添加用户
- user:edit: 编辑用户
- user:delete: 删除用户
- patient:view: 查看患者信息
- patient:add: 添加患者
- patient:edit: 编辑患者
- patient:delete: 删除患者
- medical:record:view: 查看病历记录
- medical:record:add: 添加病历记录
- medical:record:edit: 编辑病历记录
- medical:record:delete: 删除病历记录
- mmse:test:view: 查看MMSE测试
- mmse:test:add: 添加MMSE测试
- mmse:test:edit: 编辑MMSE测试
- mmse:test:delete: 删除MMSE测试
- mmse:test:score: 批改MMSE测试

## 注意事项
1. 登录和注册接口需要验证码，验证码有效期为5分钟
2. 密码必须符合安全要求，建议使用强密码
3. 用户名不能重复，注册时会进行唯一性校验
4. 访问令牌有效期为24小时，过期后需要重新登录
5. 退出登录后，当前令牌将立即失效
6. 获取当前用户接口可用于前端判断用户登录状态和权限
7. 用户角色决定了用户可访问的功能和资源 
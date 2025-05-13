# 病人认证模块接口文档

## 1. 病人认证

### 接口描述
提交病人认证信息，进行身份验证。

### 请求信息
- **URL**: `/patients/authentication`
- **方法**: POST
- **Content-Type**: application/json

### 请求参数
```json
{
  "uuid": "string",           // 患者唯一标识（必填）
  "name": "string",           // 患者姓名（必填）
  "idCard": "string",         // 身份证号（必填）
  "gender": "string",         // 性别（必填，男/女）
  "birthDate": "string",      // 出生日期（必填，格式：yyyy-MM-dd）
  "phone": "string",          // 手机号码（必填）
  "address": "string",        // 居住地址（必填）
  "emergencyContact": "string", // 紧急联系人（必填）
  "emergencyPhone": "string",   // 紧急联系人电话（必填）
  "medicalHistory": "string",   // 病史（可选）
  "allergies": "string",        // 过敏史（可选）
  "medications": "string"       // 当前用药（可选）
}
```

### 响应信息
```json
{
  "code": "integer",    // 状态码
  "message": "string",  // 响应消息
  "data": {
    "uuid": "string",           // 患者唯一标识
    "name": "string",           // 患者姓名
    "idCard": "string",         // 身份证号
    "gender": "string",         // 性别
    "birthDate": "string",      // 出生日期
    "phone": "string",          // 手机号码
    "address": "string",        // 居住地址
    "emergencyContact": "string", // 紧急联系人
    "emergencyPhone": "string",   // 紧急联系人电话
    "medicalHistory": "string",   // 病史
    "allergies": "string",        // 过敏史
    "medications": "string",      // 当前用药
    "authenticationStatus": "integer", // 认证状态
    "authenticationTime": "string",    // 认证时间
    "createTime": "string"             // 创建时间
  }
}
```

### 错误码
- 认证成功：SUCCESS
- 认证失败：AUTHENTICATION_FAIL
- 参数验证失败：PARAMETER_VALIDATION_FAILED
- 身份证号已存在：ID_CARD_EXISTS
- 手机号已存在：PHONE_EXISTS
- 患者已认证：PATIENT_ALREADY_AUTHENTICATED

## 2. 查询认证状态

### 接口描述
查询患者的认证状态和认证信息。

### 请求信息
- **URL**: `/patients/isAuthentication/{uuid}`
- **方法**: GET
- **路径参数**:
  - uuid: 患者唯一标识（必填）

### 响应信息
```json
{
  "code": "integer",    // 状态码
  "message": "string",  // 响应消息
  "data": {
    "uuid": "string",           // 患者唯一标识
    "name": "string",           // 患者姓名
    "idCard": "string",         // 身份证号
    "gender": "string",         // 性别
    "birthDate": "string",      // 出生日期
    "phone": "string",          // 手机号码
    "address": "string",        // 居住地址
    "emergencyContact": "string", // 紧急联系人
    "emergencyPhone": "string",   // 紧急联系人电话
    "medicalHistory": "string",   // 病史
    "allergies": "string",        // 过敏史
    "medications": "string",      // 当前用药
    "authenticationStatus": "integer", // 认证状态
    "authenticationTime": "string",    // 认证时间
    "createTime": "string"             // 创建时间
  }
}
```

### 错误码
- 查询成功：SUCCESS
- 查询失败：GET_AUTHENTICATION_FAIL
- 患者不存在：PATIENT_NOT_FOUND
- 患者未认证：PATIENT_NOT_AUTHENTICATED

## 数据字典

### 认证状态说明
- 0: 未认证
- 1: 已认证
- 2: 认证中
- 3: 认证失败

### 性别说明
- 男
- 女

## 注意事项
1. 所有接口都需要进行身份验证
2. 病人认证接口需要验证身份证号和手机号的唯一性
3. 查询认证状态接口需要验证患者是否存在
4. 身份证号必须是有效的18位身份证号
5. 手机号必须是有效的11位手机号
6. 出生日期必须是有效的日期格式
7. 紧急联系人电话必须是有效的手机号或固定电话
8. 病史、过敏史和当前用药为可选字段，可以为空
9. 认证成功后，患者信息将被保存，可用于后续的医疗服务
10. 认证状态为"认证中"时，表示认证信息正在审核中，审核通过后状态将更新为"已认证" 
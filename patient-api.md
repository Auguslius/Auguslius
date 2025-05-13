# 患者管理模块接口文档

## 1. 患者基础管理接口

### 1.1 添加患者

#### 接口描述
添加新患者信息到系统。

#### 请求信息
- **URL**: `/patients`
- **方法**: POST
- **Content-Type**: application/json

#### 请求参数
```json
{
  "name": "string",           // 患者姓名
  "gender": "string",         // 性别
  "age": "integer",          // 年龄
  "phone": "string",         // 联系电话
  "address": "string",       // 地址
  "medicalHistory": "string", // 病史
  "guardianName": "string",   // 监护人姓名
  "guardianPhone": "string",  // 监护人电话
  "guardianRelation": "string" // 与监护人关系
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

### 1.2 更新患者信息

#### 接口描述
更新已存在患者的信息。

#### 请求信息
- **URL**: `/patients`
- **方法**: PUT
- **Content-Type**: application/json

#### 请求参数
```json
{
  "uuid": "string",          // 患者唯一标识
  "name": "string",          // 患者姓名
  "gender": "string",        // 性别
  "age": "integer",         // 年龄
  "phone": "string",        // 联系电话
  "address": "string",      // 地址
  "medicalHistory": "string", // 病史
  "guardianName": "string",   // 监护人姓名
  "guardianPhone": "string",  // 监护人电话
  "guardianRelation": "string" // 与监护人关系
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

### 1.3 删除患者

#### 接口描述
根据患者UUID删除患者信息。

#### 请求信息
- **URL**: `/patients/{uuid}`
- **方法**: DELETE
- **参数**: 
  - uuid: 患者唯一标识（路径参数）

#### 响应信息
```json
{
  "code": "integer",    // 状态码
  "message": "string",  // 响应消息
  "data": "boolean"     // 是否删除成功
}
```

### 1.4 获取患者详情

#### 接口描述
根据患者UUID获取患者详细信息。

#### 请求信息
- **URL**: `/patients/{uuid}`
- **方法**: GET
- **参数**: 
  - uuid: 患者唯一标识（路径参数）

#### 响应信息
```json
{
  "code": "integer",    // 状态码
  "message": "string",  // 响应消息
  "data": {
    "uuid": "string",           // 患者唯一标识
    "name": "string",           // 患者姓名
    "gender": "string",         // 性别
    "age": "integer",          // 年龄
    "phone": "string",         // 联系电话
    "address": "string",       // 地址
    "medicalHistory": "string", // 病史
    "guardianName": "string",   // 监护人姓名
    "guardianPhone": "string",  // 监护人电话
    "guardianRelation": "string", // 与监护人关系
    "createTime": "string",     // 创建时间
    "updateTime": "string"      // 更新时间
  }
}
```

### 1.5 获取所有患者列表

#### 接口描述
获取系统中所有患者的基本信息列表。

#### 请求信息
- **URL**: `/patients/list`
- **方法**: GET

#### 响应信息
```json
[
  {
    "uuid": "string",    // 患者唯一标识
    "name": "string",    // 患者姓名
    "gender": "string",  // 性别
    "age": "integer",   // 年龄
    "phone": "string"   // 联系电话
  }
]
```

## 2. 患者统计分析接口

### 2.1 患者分页条件查询

#### 接口描述
根据条件分页查询患者信息。

#### 请求信息
- **URL**: `/patients/page`
- **方法**: GET
- **参数**:
  - pageNum: 页码（默认1）
  - pageSize: 每页条数（默认10）
  - name: 患者姓名（可选）
  - gender: 性别（可选）
  - ageRange: 年龄范围（可选）
  - phone: 联系电话（可选）

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
    "records": [           // 患者列表
      {
        "uuid": "string",           // 患者唯一标识
        "name": "string",           // 患者姓名
        "gender": "string",         // 性别
        "age": "integer",          // 年龄
        "phone": "string",         // 联系电话
        "address": "string",       // 地址
        "medicalHistory": "string", // 病史
        "guardianName": "string",   // 监护人姓名
        "guardianPhone": "string",  // 监护人电话
        "guardianRelation": "string", // 与监护人关系
        "createTime": "string",     // 创建时间
        "updateTime": "string"      // 更新时间
      }
    ]
  }
}
```

### 2.2 查询患者性别分布

#### 接口描述
统计患者性别分布情况。

#### 请求信息
- **URL**: `/patients/PatientCount`
- **方法**: GET

#### 响应信息
```json
{
  "code": "integer",    // 状态码
  "message": "string",  // 响应消息
  "data": {
    "maleCount": "integer",    // 男性患者数量
    "femaleCount": "integer",  // 女性患者数量
    "totalCount": "integer"    // 总患者数量
  }
}
```

### 2.3 查询近五日新增患者

#### 接口描述
统计当前用户近五日新增患者数量。

#### 请求信息
- **URL**: `/patients/countNewPatients`
- **方法**: GET

#### 响应信息
```json
{
  "code": "integer",    // 状态码
  "message": "string",  // 响应消息
  "data": [
    {
      "date": "string",     // 日期（格式：yyyy-MM-dd）
      "count": "integer"    // 新增患者数量
    }
  ]
}
```

### 2.4 查询患者年龄分布

#### 接口描述
统计患者年龄分布情况。

#### 请求信息
- **URL**: `/patients/ageDistribution`
- **方法**: GET

#### 响应信息
```json
{
  "code": "integer",    // 状态码
  "message": "string",  // 响应消息
  "data": {
    "0-18": "integer",      // 0-18岁患者数量
    "19-30": "integer",     // 19-30岁患者数量
    "31-45": "integer",     // 31-45岁患者数量
    "46-60": "integer",     // 46-60岁患者数量
    "61-75": "integer",     // 61-75岁患者数量
    "76+": "integer"        // 76岁以上患者数量
  }
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

### 性别枚举
- MALE: 男
- FEMALE: 女

## 注意事项
1. 所有接口都需要进行身份验证
2. 分页查询接口支持多条件组合查询
3. 患者信息涉及隐私，请确保数据传输安全
4. 统计分析接口的数据可能会有一定延迟
5. 建议在查询大量数据时使用分页接口 
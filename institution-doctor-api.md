# 机构医生管理模块接口文档

## 1. 获取机构列表

### 接口描述
根据机构类别ID获取机构列表。

### 请求信息
- **URL**: `/institution-doctor/listInstitution/{institutionCategoryId}`
- **方法**: GET
- **路径参数**:
  - institutionCategoryId: 机构类别ID（必填）

### 响应信息
```json
{
  "code": "integer",    // 状态码
  "message": "string",  // 响应消息
  "data": [
    {
      "id": "integer",                // 机构ID
      "name": "string",               // 机构名称
      "address": "string",            // 机构地址
      "contactPhone": "string",       // 联系电话
      "description": "string",        // 机构描述
      "institutionCategoryId": "integer", // 机构类别ID
      "createTime": "string",         // 创建时间
      "updateTime": "string"          // 更新时间
    }
  ]
}
```

### 错误码
- 查询成功：SUCCESS
- 查询失败：GET_INSTITUTION_LIST_FAIL
- 机构类别不存在：INSTITUTION_CATEGORY_NOT_FOUND

## 2. 获取医生列表

### 接口描述
根据机构名称获取该机构下的医生列表。

### 请求信息
- **URL**: `/institution-doctor/listDoctor`
- **方法**: GET
- **请求参数**:
  - institution: 机构名称（必填）

### 响应信息
```json
{
  "code": "integer",    // 状态码
  "message": "string",  // 响应消息
  "data": [
    {
      "id": "integer",                // 用户ID
      "username": "string",           // 用户名
      "nickname": "string",           // 昵称
      "avatar": "string",             // 头像URL
      "email": "string",              // 电子邮箱
      "phone": "string",              // 手机号码
      "role": "string",               // 用户角色
      "status": "integer",            // 用户状态
      "doctorNumber": "integer",      // 医生编号
      "title": "string",              // 职称
      "department": "string",         // 科室
      "specialty": "string",          // 专长
      "introduction": "string",       // 个人简介
      "createTime": "string"          // 创建时间
    }
  ]
}
```

### 错误码
- 查询成功：SUCCESS
- 查询失败：GET_DOCTOR_LIST_FAIL
- 机构不存在：INSTITUTION_NOT_FOUND

## 3. 绑定医生

### 接口描述
将医生绑定到患者账号。

### 请求信息
- **URL**: `/institution-doctor/bindDoctorByDoctorNumber`
- **方法**: PATCH
- **Content-Type**: application/json

### 请求参数
```json
{
  "patientUuid": "string",     // 患者唯一标识（必填）
  "doctorNumber": "integer"    // 医生编号（必填）
}
```

### 响应信息
```json
{
  "code": "integer",    // 状态码
  "message": "string",  // 响应消息
  "data": "boolean"     // 绑定结果，true表示成功，false表示失败
}
```

### 错误码
- 绑定成功：SUCCESS
- 绑定失败：BIND_DOCTOR_FAIL
- 患者不存在：PATIENT_NOT_FOUND
- 医生不存在：DOCTOR_NOT_FOUND
- 医生已被绑定：DOCTOR_ALREADY_BOUND

## 4. 解绑医生

### 接口描述
解除患者与医生的绑定关系。

### 请求信息
- **URL**: `/institution-doctor/unbindDoctorByDoctorNumber`
- **方法**: PATCH
- **Content-Type**: application/json

### 请求参数
```json
{
  "patientUuid": "string",     // 患者唯一标识（必填）
  "doctorNumber": "integer"    // 医生编号（必填）
}
```

### 响应信息
```json
{
  "code": "integer",    // 状态码
  "message": "string",  // 响应消息
  "data": "boolean"     // 解绑结果，true表示成功，false表示失败
}
```

### 错误码
- 解绑成功：SUCCESS
- 解绑失败：UNBIND_DOCTOR_FAIL
- 患者不存在：PATIENT_NOT_FOUND
- 医生不存在：DOCTOR_NOT_FOUND
- 医生未绑定：DOCTOR_NOT_BOUND

## 5. 获取医生信息

### 接口描述
根据医生编号获取医生的详细信息。

### 请求信息
- **URL**: `/institution-doctor/getDoctorMsg/{doctorNumber}`
- **方法**: GET
- **路径参数**:
  - doctorNumber: 医生编号（必填）

### 响应信息
```json
{
  "code": "integer",    // 状态码
  "message": "string",  // 响应消息
  "data": {
    "id": "integer",                // 用户ID
    "username": "string",           // 用户名
    "nickname": "string",           // 昵称
    "avatar": "string",             // 头像URL
    "email": "string",              // 电子邮箱
    "phone": "string",              // 手机号码
    "role": "string",               // 用户角色
    "status": "integer",            // 用户状态
    "doctorNumber": "integer",      // 医生编号
    "title": "string",              // 职称
    "department": "string",         // 科室
    "specialty": "string",          // 专长
    "introduction": "string",       // 个人简介
    "institutionName": "string",    // 所属机构名称
    "createTime": "string"          // 创建时间
  }
}
```

### 错误码
- 查询成功：SUCCESS
- 查询失败：GET_DOCTOR_MSG_FAIL
- 医生不存在：DOCTOR_NOT_FOUND

## 数据字典

### 机构类别说明
- 1: 综合医院
- 2: 专科医院
- 3: 社区医院
- 4: 康复中心
- 5: 养老院

### 医生职称说明
- 主任医师
- 副主任医师
- 主治医师
- 住院医师
- 专科医师

### 科室说明
- 神经内科
- 精神科
- 老年医学科
- 康复医学科
- 心理科
- 全科医学科

## 注意事项
1. 所有接口都需要进行身份验证
2. 绑定医生接口需要验证患者和医生是否存在
3. 解绑医生接口需要验证患者和医生是否已绑定
4. 获取医生信息接口需要验证医生是否存在
5. 医生编号是医生的唯一标识，不可重复
6. 一个患者可以绑定多个医生，一个医生也可以被多个患者绑定
7. 医生信息包含个人简介和专长，可用于患者选择合适的医生 
# 医疗机构管理接口文档

## 1. 添加医疗机构

### 接口描述
添加一个新的医疗机构信息。

### 请求信息
- **URL**: `/institution`
- **方法**: POST
- **Content-Type**: application/json

### 请求参数
```json
{
  "uuid": "string",           // 机构ID（可选，系统自动生成）
  "institutionName": "string", // 机构名称（必填，最大255字符）
  "institutionPhone": "string", // 机构电话（必填，最大255字符，只能包含数字和可选的前缀'+'）
  "address": "string",        // 详细地址（可选，最大512字符）
  "institutionCategoryId": "integer", // 机构种类ID（必填，必须大于0）
  "institutionLevel": "integer", // 机构级别（必填，必须大于0且小于等于5）
  "status": "integer"         // 状态（必填，0-禁用，1-启用，2-审核中）
}
```

### 响应信息
```json
{
  "code": "integer",    // 状态码
  "message": "string",  // 响应消息
  "data": "boolean"     // 操作结果
}
```

### 错误码
- 添加成功：SUCCESS
- 添加失败：ADD_INSTITUTION_FAIL
- 机构名称重复：ADD_INSTITUTION_DUPLICATE_NAME
- 机构种类不存在：ADD_CATEGORY_NOT_EXIST
- 机构级别不存在：ADD_LEVEL_NOT_EXIST

## 2. 删除医疗机构

### 接口描述
根据机构ID删除医疗机构信息。

### 请求信息
- **URL**: `/institution/{uuid}`
- **方法**: DELETE
- **参数**: uuid - 机构ID（路径参数）

### 响应信息
```json
{
  "code": "integer",    // 状态码
  "message": "string",  // 响应消息
  "data": "boolean"     // 操作结果
}
```

### 错误码
- 删除成功：DELETE_INSTITUTION_SUCCESS
- 删除失败：DELETE_INSTITUTION_FAIL

## 3. 更新医疗机构

### 接口描述
更新医疗机构信息。

### 请求信息
- **URL**: `/institution`
- **方法**: PATCH
- **Content-Type**: application/json

### 请求参数
```json
{
  "uuid": "string",           // 机构ID（必填）
  "institutionName": "string", // 机构名称（必填，最大255字符）
  "institutionPhone": "string", // 机构电话（必填，最大255字符）
  "address": "string",        // 详细地址（可选，最大512字符）
  "institutionCategoryId": "integer", // 机构种类ID（必填，必须大于0）
  "institutionLevel": "integer", // 机构级别（必填，必须大于0且小于等于5）
  "status": "integer"         // 状态（必填，0-禁用，1-启用，2-审核中）
}
```

### 响应信息
```json
{
  "code": "integer",    // 状态码
  "message": "string",  // 响应消息
  "data": "boolean"     // 操作结果
}
```

### 错误码
- 更新成功：UPDATE_INSTITUTION_SUCCESS
- 更新失败：UPDATE_INSTITUTION_FAIL
- 机构不存在：UPDATE_INSTITUTION_NOT_EXIST
- 机构名称重复：UPDATE_INSTITUTION_DUPLICATE_NAME
- 机构种类不存在：UPDATE_INSTITUTION_CATEGORY_NOT_EXIST
- 机构级别不存在：UPDATE_INSTITUTION_LEVEL_NOT_EXIST

## 4. 分页查询医疗机构

### 接口描述
根据条件分页查询医疗机构信息。

### 请求信息
- **URL**: `/institution/page`
- **方法**: GET
- **参数**:
  - uuid: 机构ID（可选）
  - institutionName: 机构名称（可选）
  - institutionLevel: 机构级别（可选）
  - pageSize: 每页大小（默认5）

### 响应信息
```json
{
  "code": "integer",    // 状态码
  "message": "string",  // 响应消息
  "data": {
    "records": [{
      "uuid": "string",           // 机构ID
      "institutionName": "string", // 机构名称
      "institutionPhone": "string", // 机构电话
      "address": "string",        // 详细地址
      "institutionCategoryId": "integer", // 机构种类ID
      "institutionLevel": "integer", // 机构级别
      "status": "integer"         // 状态
    }],
    "total": "integer",          // 总记录数
    "size": "integer",           // 每页大小
    "current": "integer"         // 当前页码
  }
}
```

### 错误码
- 查询成功：QUERY_INSTITUTION_SUCCESS
- 查询失败：QUERY_INSTITUTION_FAIL

## 5. 根据ID查询医疗机构

### 接口描述
根据机构ID查询单个医疗机构信息。

### 请求信息
- **URL**: `/institution/{uuid}`
- **方法**: GET
- **参数**: uuid - 机构ID（路径参数）

### 响应信息
```json
{
  "code": "integer",    // 状态码
  "message": "string",  // 响应消息
  "data": {
    "uuid": "string",           // 机构ID
    "institutionName": "string", // 机构名称
    "institutionPhone": "string", // 机构电话
    "address": "string",        // 详细地址
    "institutionCategoryId": "integer", // 机构种类ID
    "institutionLevel": "integer", // 机构级别
    "status": "integer"         // 状态
  }
}
```

### 错误码
- 查询成功：QUERY_INSTITUTION_SUCCESS
- 查询失败：QUERY_INSTITUTION_FAIL

## 6. 查询所有医疗机构

### 接口描述
获取所有医疗机构信息。

### 请求信息
- **URL**: `/institution/getAll`
- **方法**: GET

### 响应信息
```json
{
  "code": "integer",    // 状态码
  "message": "string",  // 响应消息
  "data": [{
    "uuid": "string",           // 机构ID
    "institutionName": "string", // 机构名称
    "institutionPhone": "string", // 机构电话
    "address": "string",        // 详细地址
    "institutionCategoryId": "integer", // 机构种类ID
    "institutionLevel": "integer", // 机构级别
    "status": "integer"         // 状态
  }]
}
```

### 错误码
- 查询成功：QUERY_INSTITUTION_SUCCESS
- 查询失败：QUERY_INSTITUTION_FAIL

## 数据字典

### 状态码说明
- 0: 禁用
- 1: 启用
- 2: 审核中

### 字段说明
- uuid: 机构唯一标识
- institutionName: 机构名称
- institutionPhone: 机构联系电话
- address: 机构详细地址
- institutionCategoryId: 机构所属类别ID
- institutionLevel: 机构级别（1-5级）
- status: 机构状态
- createTime: 创建时间
- updateTime: 更新时间

## 注意事项
1. 所有接口都需要进行参数验证
2. 机构名称不能重复
3. 机构级别必须在1-5之间
4. 机构种类ID必须存在
5. 电话号码格式必须符合规范（只能包含数字和可选的前缀'+'） 
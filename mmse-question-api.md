# MMSE量表问题管理接口文档

## 1. 新增MMSE量表问题

### 接口描述
添加一个新的MMSE量表问题。

### 请求信息
- **URL**: `/mmseQuestions`
- **方法**: POST
- **Content-Type**: application/json

### 请求参数
```json
{
  "id": "integer",           // MMSE问题ID（可选，新增时可不传）
  "questionContent": "string", // 问题内容（必填）
  "questionType": "string",    // 问题类型（必填）
  "questionCategory": "string", // 问题类别（必填）
  "score": "integer",          // 分值（必填）
  "options": "string",         // 选项（可选）
  "answer": "string",          // 答案（必填）
  "createUser": "string",      // 创建人（必填）
  "remark": "string"           // 备注（可选）
}
```

### 响应信息
```json
{
  "code": "integer",    // 状态码
  "message": "string",  // 响应消息
  "data": "boolean"     // 操作结果，true表示成功
}
```

### 错误码
- 添加成功：SUCCESS
- 添加失败：FAIL

## 2. 更新MMSE量表问题

### 接口描述
更新现有的MMSE量表问题信息。

### 请求信息
- **URL**: `/mmseQuestions`
- **方法**: PUT
- **Content-Type**: application/json

### 请求参数
```json
{
  "id": "integer",           // MMSE问题ID（必填）
  "questionContent": "string", // 问题内容（必填）
  "questionType": "string",    // 问题类型（必填）
  "questionCategory": "string", // 问题类别（必填）
  "score": "integer",          // 分值（必填）
  "options": "string",         // 选项（可选）
  "answer": "string",          // 答案（必填）
  "createUser": "string",      // 创建人（必填）
  "remark": "string"           // 备注（可选）
}
```

### 响应信息
```json
{
  "code": "integer",    // 状态码
  "message": "string",  // 响应消息
  "data": "boolean"     // 操作结果，true表示成功
}
```

### 错误码
- 更新成功：SUCCESS
- 更新失败：FAIL

## 3. 删除MMSE量表问题

### 接口描述
根据ID删除MMSE量表问题。

### 请求信息
- **URL**: `/mmseQuestions/{id}`
- **方法**: DELETE
- **参数**: id - MMSE问题ID（路径参数）

### 响应信息
```json
{
  "code": "integer",    // 状态码
  "message": "string",  // 响应消息
  "data": "boolean"     // 操作结果，true表示成功
}
```

### 错误码
- 删除成功：SUCCESS
- 删除失败：FAIL

## 4. 根据ID获取MMSE量表问题

### 接口描述
根据ID查询单个MMSE量表问题信息。

### 请求信息
- **URL**: `/mmseQuestions/{id}`
- **方法**: GET
- **参数**: id - MMSE问题ID（路径参数）

### 响应信息
```json
{
  "code": "integer",    // 状态码
  "message": "string",  // 响应消息
  "data": {
    "id": "integer",           // MMSE问题ID
    "questionContent": "string", // 问题内容
    "questionType": "string",    // 问题类型
    "questionCategory": "string", // 问题类别
    "score": "integer",          // 分值
    "options": "string",         // 选项
    "answer": "string",          // 答案
    "createTime": "datetime",    // 创建时间
    "updateTime": "datetime",    // 更新时间
    "createUser": "string",      // 创建人
    "remark": "string"           // 备注
  }
}
```

### 错误码
- 查询成功：SUCCESS
- 查询失败：FAIL

## 5. 分页查询MMSE量表问题

### 接口描述
根据条件分页查询MMSE量表问题。

### 请求信息
- **URL**: `/mmseQuestions/mmseQuestionsPage`
- **方法**: GET
- **参数**: 
  - pageNum: 页码（必填）
  - pageSize: 每页数量（必填）
  - questionContent: 问题内容（可选，模糊查询）
  - questionType: 问题类型（可选）
  - questionCategory: 问题类别（可选）
  - startTime: 开始时间（可选）
  - endTime: 结束时间（可选）

### 响应信息
```json
{
  "code": "integer",    // 状态码
  "message": "string",  // 响应消息
  "data": {
    "total": "integer",  // 总记录数
    "pages": "integer",  // 总页数
    "pageNum": "integer", // 当前页码
    "pageSize": "integer", // 每页数量
    "list": [{
      "id": "integer",           // MMSE问题ID
      "questionContent": "string", // 问题内容
      "questionType": "string",    // 问题类型
      "questionCategory": "string", // 问题类别
      "score": "integer",          // 分值
      "options": "string",         // 选项
      "answer": "string",          // 答案
      "createTime": "datetime",    // 创建时间
      "updateTime": "datetime",    // 更新时间
      "createUser": "string",      // 创建人
      "remark": "string"           // 备注
    }]
  }
}
```

### 错误码
- 查询成功：SUCCESS
- 查询失败：FAIL

## 6. 获取所有MMSE量表问题

### 接口描述
获取所有MMSE量表问题列表，用于展示。

### 请求信息
- **URL**: `/mmseQuestions/listMMSEQuestions`
- **方法**: GET

### 响应信息
```json
{
  "code": "integer",    // 状态码
  "message": "string",  // 响应消息
  "data": [{
    "id": "integer",           // MMSE问题ID
    "questionContent": "string", // 问题内容
    "questionType": "string",    // 问题类型
    "questionCategory": "string", // 问题类别
    "score": "integer",          // 分值
    "options": "string",         // 选项
    "answer": "string",          // 答案
    "createTime": "datetime",    // 创建时间
    "updateTime": "datetime",    // 更新时间
    "createUser": "string",      // 创建人
    "remark": "string"           // 备注
  }]
}
```

### 错误码
- 查询成功：SUCCESS
- 查询失败：FAIL

## 7. 获取MMSE量表问题类别

### 接口描述
获取所有MMSE量表问题类别及其数量。

### 请求信息
- **URL**: `/mmseQuestions/listMMSEQuestionCategory`
- **方法**: GET

### 响应信息
```json
{
  "code": "integer",    // 状态码
  "message": "string",  // 响应消息
  "data": [{
    "category": "string",  // 问题类别
    "count": "integer"     // 该类别下的问题数量
  }]
}
```

### 错误码
- 查询成功：SUCCESS
- 查询失败：FAIL

## 数据字典

### MMSE量表问题字段说明
- id: MMSE问题唯一标识
- questionContent: 问题内容
- questionType: 问题类型（如选择题、填空题等）
- questionCategory: 问题类别（如定向力、记忆力等）
- score: 问题分值
- options: 问题选项（JSON格式，适用于选择题）
- answer: 问题答案
- createTime: 创建时间
- updateTime: 更新时间
- createUser: 创建人
- remark: 备注信息

## 注意事项
1. 所有接口都需要进行参数验证
2. 新增和更新问题时，必填字段不能为空
3. 删除问题时，需要确保该问题未被使用
4. 分页查询时，页码从1开始
5. 问题类别通常包括：定向力、记忆力、注意力和计算力、回忆力、语言能力等 
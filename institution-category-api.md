# 机构种类管理接口文档

## 1. 获取所有机构种类

### 接口描述
获取系统中所有的机构种类信息。

### 请求信息
- **URL**: `/institutionCategory`
- **方法**: GET

### 响应信息
```json
{
  "code": "integer",    // 状态码
  "message": "string",  // 响应消息
  "data": [{
    "id": "integer",           // 机构种类ID
    "categoryName": "string",  // 种类名称
    "categoryAlias": "string", // 种类别名
    "createTime": "datetime",  // 创建时间
    "updateTime": "datetime",  // 更新时间
    "level": "integer",        // 层级
    "levelName": "string",     // 层级名称
    "createUser": "string",    // 创建人
    "remark": "string"         // 备注
  }]
}
```

### 错误码
- 查询成功：GET_INSTITUTION_SUCCESS
- 查询失败：GET_INSTITUTION_FAIL

## 2. 新增机构种类

### 接口描述
添加一个新的机构种类。

### 请求信息
- **URL**: `/institutionCategory`
- **方法**: POST
- **Content-Type**: application/json

### 请求参数
```json
{
  "id": "integer",           // 机构种类ID（必填）
  "categoryName": "string",  // 种类名称（必填）
  "categoryAlias": "string", // 种类别名（可选）
  "createUser": "integer",   // 创建人（必填）
  "level": "integer",        // 层级（可选，默认5）
  "levelName": "string",     // 层级名称（可选，默认"其它"）
  "remark": "string"         // 备注（可选）
}
```

### 响应信息
```json
{
  "code": "integer",    // 状态码
  "message": "string",  // 响应消息
  "data": {
    "id": "integer",           // 机构种类ID
    "categoryName": "string",  // 种类名称
    "categoryAlias": "string", // 种类别名
    "createTime": "datetime",  // 创建时间
    "updateTime": "datetime",  // 更新时间
    "level": "integer",        // 层级
    "levelName": "string",     // 层级名称
    "createUser": "string",    // 创建人
    "remark": "string"         // 备注
  }
}
```

### 错误码
- 添加成功：ADD_INSTITUTION_SUCCESS
- 添加失败：ADD_INSTITUTION_FAIL
- 机构种类名称已存在：CATEGORY_NAME_EXISTS
- 机构种类别名已存在：CATEGORY_ALIAS_EXISTS

## 3. 删除机构种类

### 接口描述
根据ID删除机构种类。

### 请求信息
- **URL**: `/institutionCategory/{id}`
- **方法**: DELETE
- **参数**: id - 机构种类ID（路径参数）

### 响应信息
```json
{
  "code": "integer",    // 状态码
  "message": "string",  // 响应消息
  "data": null
}
```

### 错误码
- 删除成功：DELETE_INSTITUTION_SUCCESS
- 删除失败：DELETE_INSTITUTION_FAIL

## 4. 获取所有层级信息

### 接口描述
获取系统中所有的层级信息。

### 请求信息
- **URL**: `/institutionCategory/getLevelList`
- **方法**: GET

### 响应信息
```json
{
  "code": "integer",    // 状态码
  "message": "string",  // 响应消息
  "data": {
    "count": "integer",  // 层级数量
    "levelList": [{
      "level": "integer",     // 层级
      "levelName": "string"   // 层级名称
    }]
  }
}
```

### 错误码
- 查询成功：SUCCESS
- 查询失败：FAIL

## 5. 根据ID获取机构种类

### 接口描述
根据ID查询单个机构种类信息。

### 请求信息
- **URL**: `/institutionCategory/{id}`
- **方法**: GET
- **参数**: id - 机构种类ID（路径参数）

### 响应信息
```json
{
  "code": "integer",    // 状态码
  "message": "string",  // 响应消息
  "data": {
    "id": "integer",           // 机构种类ID
    "categoryName": "string",  // 种类名称
    "categoryAlias": "string", // 种类别名
    "createTime": "datetime",  // 创建时间
    "updateTime": "datetime",  // 更新时间
    "level": "integer",        // 层级
    "levelName": "string",     // 层级名称
    "createUser": "string",    // 创建人
    "remark": "string"         // 备注
  }
}
```

### 错误码
- 查询成功：GET_INSTITUTION_SUCCESS
- 查询失败：GET_INSTITUTION_FAIL

## 6. 更新机构种类

### 接口描述
更新机构种类信息。

### 请求信息
- **URL**: `/institutionCategory/{id}`
- **方法**: PUT
- **Content-Type**: application/json
- **参数**: id - 机构种类ID（路径参数）

### 请求参数
```json
{
  "id": "integer",           // 机构种类ID（必填）
  "categoryName": "string",  // 种类名称（必填）
  "categoryAlias": "string", // 种类别名（可选）
  "createUser": "integer",   // 创建人（必填）
  "level": "integer",        // 层级（可选）
  "levelName": "string",     // 层级名称（可选）
  "remark": "string"         // 备注（可选）
}
```

### 响应信息
```json
{
  "code": "integer",    // 状态码
  "message": "string",  // 响应消息
  "data": {
    "id": "integer",           // 机构种类ID
    "categoryName": "string",  // 种类名称
    "categoryAlias": "string", // 种类别名
    "createTime": "datetime",  // 创建时间
    "updateTime": "datetime",  // 更新时间
    "level": "integer",        // 层级
    "levelName": "string",     // 层级名称
    "createUser": "string",    // 创建人
    "remark": "string"         // 备注
  }
}
```

### 错误码
- 更新成功：UPDATE_INSTITUTION_SUCCESS
- 更新失败：UPDATE_INSTITUTION_FAIL
- 机构种类不存在：UPDATE_INSTITUTION_NOT_FOUND
- 机构种类名称已存在：CATEGORY_NAME_EXISTS
- 机构种类别名已存在：CATEGORY_ALIAS_EXISTS

## 7. 获取层级数量

### 接口描述
获取系统中层级的总数量。

### 请求信息
- **URL**: `/institutionCategory/getLevelCount`
- **方法**: GET

### 响应信息
```json
{
  "code": "integer",    // 状态码
  "message": "string",  // 响应消息
  "data": "integer"     // 层级数量
}
```

### 错误码
- 查询成功：SUCCESS
- 查询失败：FAIL

## 8. 获取机构种类树

### 接口描述
获取机构种类的树形结构数据。

### 请求信息
- **URL**: `/institutionCategory/getCategoryTree`
- **方法**: GET

### 响应信息
```json
{
  "code": "integer",    // 状态码
  "message": "string",  // 响应消息
  "data": [
    {
      "id": "integer",           // 机构种类ID
      "categoryName": "string",  // 种类名称
      "categoryAlias": "string", // 种类别名
      "level": "integer",        // 层级
      "levelName": "string",     // 层级名称
      "children": [              // 子节点
        {
          "id": "integer",
          "categoryName": "string",
          "categoryAlias": "string",
          "level": "integer",
          "levelName": "string"
        }
      ]
    }
  ]
}
```

### 错误码
- 查询成功：SUCCESS
- 查询失败：FAIL

## 数据字典

### 字段说明
- id: 机构种类唯一标识
- categoryName: 机构种类名称
- categoryAlias: 机构种类别名
- createUser: 创建人ID
- createTime: 创建时间
- updateTime: 更新时间
- level: 层级（数字）
- levelName: 层级名称
- remark: 备注信息

## 注意事项
1. 所有接口都需要进行参数验证
2. 机构种类名称不能重复
3. 机构种类别名不能重复
4. 创建机构种类时，如果没有指定层级和层级名称，将使用默认值（层级：5，层级名称："其它"）
5. 更新机构种类时，如果找不到对应的机构种类，将返回错误 
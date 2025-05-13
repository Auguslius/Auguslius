# 病历记录管理模块接口文档

## 1. 病历记录基础管理接口

### 1.1 新增病历记录

#### 接口描述
添加新的病历记录到系统。

#### 请求信息
- **URL**: `/medical-records`
- **方法**: POST
- **Content-Type**: application/json

#### 请求参数
```json
{
  "patientUuid": "string",      // 患者唯一标识
  "doctorUuid": "string",       // 医生唯一标识
  "visitDate": "string",        // 就诊日期（格式：yyyy-MM-dd）
  "diagnosis": "string",        // 诊断结果
  "treatment": "string",        // 治疗方案
  "prescription": "string",     // 处方信息
  "notes": "string",            // 备注信息
  "attachments": [              // 附件列表
    {
      "fileName": "string",     // 文件名
      "fileUrl": "string",      // 文件URL
      "fileType": "string"      // 文件类型
    }
  ]
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

### 1.2 更新病历记录

#### 接口描述
更新已存在的病历记录信息。

#### 请求信息
- **URL**: `/medical-records/{recordUuid}`
- **方法**: PUT
- **Content-Type**: application/json
- **参数**: 
  - recordUuid: 病历记录唯一标识（路径参数）

#### 请求参数
```json
{
  "patientUuid": "string",      // 患者唯一标识
  "doctorUuid": "string",       // 医生唯一标识
  "visitDate": "string",        // 就诊日期（格式：yyyy-MM-dd）
  "diagnosis": "string",        // 诊断结果
  "treatment": "string",        // 治疗方案
  "prescription": "string",     // 处方信息
  "notes": "string",            // 备注信息
  "attachments": [              // 附件列表
    {
      "fileName": "string",     // 文件名
      "fileUrl": "string",      // 文件URL
      "fileType": "string"      // 文件类型
    }
  ]
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

### 1.3 删除病历记录

#### 接口描述
根据病历记录UUID删除病历记录。

#### 请求信息
- **URL**: `/medical-records/{recordUuid}`
- **方法**: DELETE
- **参数**: 
  - recordUuid: 病历记录唯一标识（路径参数）

#### 响应信息
```json
{
  "code": "integer",    // 状态码
  "message": "string",  // 响应消息
  "data": "boolean"     // 是否删除成功
}
```

### 1.4 病历记录分页条件查询

#### 接口描述
根据条件分页查询病历记录信息。

#### 请求信息
- **URL**: `/medical-records/page`
- **方法**: GET
- **参数**:
  - pageNum: 页码（默认1）
  - pageSize: 每页条数（默认10）
  - patientUuid: 患者唯一标识（可选）
  - doctorUuid: 医生唯一标识（可选）
  - visitDateStart: 就诊日期起始（可选，格式：yyyy-MM-dd）
  - visitDateEnd: 就诊日期结束（可选，格式：yyyy-MM-dd）
  - diagnosis: 诊断结果关键字（可选）

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
    "records": [           // 病历记录列表
      {
        "recordUuid": "string",      // 病历记录唯一标识
        "patientUuid": "string",     // 患者唯一标识
        "patientName": "string",     // 患者姓名
        "doctorUuid": "string",      // 医生唯一标识
        "doctorName": "string",      // 医生姓名
        "visitDate": "string",       // 就诊日期
        "diagnosis": "string",       // 诊断结果
        "treatment": "string",       // 治疗方案
        "prescription": "string",    // 处方信息
        "notes": "string",           // 备注信息
        "attachments": [             // 附件列表
          {
            "fileName": "string",    // 文件名
            "fileUrl": "string",     // 文件URL
            "fileType": "string"     // 文件类型
          }
        ],
        "createTime": "string",      // 创建时间
        "updateTime": "string"       // 更新时间
      }
    ]
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

### 文件类型枚举
- IMAGE: 图片
- PDF: PDF文档
- DOC: Word文档
- XLS: Excel文档
- OTHER: 其他类型

## 注意事项
1. 所有接口都需要进行身份验证
2. 病历记录涉及患者隐私，请确保数据传输安全
3. 分页查询接口支持多条件组合查询
4. 附件上传大小限制为10MB
5. 建议在查询大量数据时使用分页接口
6. 病历记录创建后不可修改患者信息，如需修改请创建新记录 
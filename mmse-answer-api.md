# MMSE答案管理模块接口文档

## 1. 提交MMSE答案

### 接口描述
提交患者的MMSE测试答案，仅保存答案不进行批改。

### 请求信息
- **URL**: `/mmse-answers/submit`
- **方法**: POST
- **Content-Type**: application/json

### 请求参数
```json
{
  "patientUuid": "string",      // 患者唯一标识（必填）
  "answers": [                  // 答案列表（必填）
    {
      "questionId": "integer",  // 问题ID（必填）
      "answer": "string"        // 患者回答内容（必填）
    }
  ],
  "submitTime": "string"        // 提交时间（必填，格式：yyyy-MM-dd HH:mm:ss）
}
```

### 响应信息
```json
{
  "code": "integer",    // 状态码
  "message": "string",  // 响应消息
  "data": null          // 无返回数据
}
```

### 错误码
- 提交成功：SUCCESS
- 提交失败：SUBMIT_ANSWER_FAIL
- 参数验证失败：PARAMETER_VALIDATION_FAILED

## 2. 批改MMSE答案

### 接口描述
对已提交的MMSE答案进行批改，计算得分。

### 请求信息
- **URL**: `/mmse-answers/score`
- **方法**: POST
- **Content-Type**: application/json

### 请求参数
```json
{
  "patientUuid": "string",      // 患者唯一标识（必填）
  "answers": [                  // 批改后的答案列表（必填）
    {
      "questionId": "integer",  // 问题ID（必填）
      "patientAnswer": "string", // 患者回答内容（必填）
      "score": "integer",       // 得分（必填，0或问题满分）
      "comment": "string"       // 评语（可选）
    }
  ],
  "totalScore": "integer",      // 总分（必填）
  "evaluation": "string",       // 评估结果（必填）
  "scoreTime": "string"         // 批改时间（必填，格式：yyyy-MM-dd HH:mm:ss）
}
```

### 响应信息
```json
{
  "code": "integer",    // 状态码
  "message": "string",  // 响应消息
  "data": null          // 无返回数据
}
```

### 错误码
- 批改成功：SUCCESS
- 批改失败：SCORE_ANSWER_FAIL
- 参数验证失败：PARAMETER_VALIDATION_FAILED

## 3. 获取患者MMSE答案

### 接口描述
获取指定患者的MMSE测试问题和答案。

### 请求信息
- **URL**: `/mmse-answers/getAnswer/{patientUuid}`
- **方法**: GET
- **参数**: 
  - patientUuid: 患者唯一标识（路径参数）

### 响应信息
```json
{
  "code": "integer",    // 状态码
  "message": "string",  // 响应消息
  "data": {
    "questions": {      // 问题信息
      "1": {            // 问题ID作为键
        "id": "integer",           // 问题ID
        "content": "string",       // 问题内容
        "type": "string",          // 问题类型
        "category": "string",      // 问题类别
        "score": "integer",        // 问题分值
        "options": "string"        // 问题选项（如果有）
      }
    },
    "answers": {        // 答案信息
      "1": {            // 问题ID作为键
        "patientAnswer": "string", // 患者回答
        "score": "integer",        // 得分
        "comment": "string"        // 评语
      }
    },
    "totalScore": "integer",       // 总分
    "evaluation": "string",        // 评估结果
    "submitTime": "string",        // 提交时间
    "scoreTime": "string"          // 批改时间
  }
}
```

### 错误码
- 查询成功：SUCCESS
- 查询失败：GET_ANSWER_FAIL
- 患者不存在：PATIENT_NOT_FOUND

## 4. 获取所有患者MMSE答案

### 接口描述
获取系统中所有患者的MMSE测试问题和答案。

### 请求信息
- **URL**: `/mmse-answers/getAllAnswer`
- **方法**: GET

### 响应信息
```json
{
  "code": "integer",    // 状态码
  "message": "string",  // 响应消息
  "data": {
    "patientUuid1": {   // 患者唯一标识作为键
      "questions": {     // 问题信息
        "1": {           // 问题ID作为键
          "id": "integer",           // 问题ID
          "content": "string",       // 问题内容
          "type": "string",          // 问题类型
          "category": "string",      // 问题类别
          "score": "integer",        // 问题分值
          "options": "string"        // 问题选项（如果有）
        }
      },
      "answers": {       // 答案信息
        "1": {           // 问题ID作为键
          "patientAnswer": "string", // 患者回答
          "score": "integer",        // 得分
          "comment": "string"        // 评语
        }
      },
      "totalScore": "integer",       // 总分
      "evaluation": "string",        // 评估结果
      "submitTime": "string",        // 提交时间
      "scoreTime": "string"          // 批改时间
    },
    "patientUuid2": {
      // 同上
    }
  }
}
```

### 错误码
- 查询成功：SUCCESS
- 查询失败：GET_ALL_ANSWER_FAIL

## 5. 获取MMSE分数分布统计

### 接口描述
获取MMSE测试分数的分布统计信息。

### 请求信息
- **URL**: `/mmse-answers/scoreDistribution`
- **方法**: GET

### 响应信息
```json
{
  "code": "integer",    // 状态码
  "message": "string",  // 响应消息
  "data": {
    "0-9": "integer",    // 0-9分患者数量
    "10-19": "integer",  // 10-19分患者数量
    "20-29": "integer",  // 20-29分患者数量
    "30-39": "integer",  // 30-39分患者数量
    "40-49": "integer",  // 40-49分患者数量
    "50+": "integer"     // 50分及以上患者数量
  }
}
```

### 错误码
- 查询成功：SUCCESS
- 查询失败：GET_SCORE_DISTRIBUTION_FAIL

## 数据字典

### MMSE评估结果说明
- 正常：27-30分
- 轻度认知障碍：21-26分
- 中度认知障碍：10-20分
- 重度认知障碍：0-9分

### 问题类型说明
- SINGLE_CHOICE: 单选题
- MULTIPLE_CHOICE: 多选题
- FILL_BLANK: 填空题
- DRAWING: 绘图题
- WRITING: 书写题

### 问题类别说明
- ORIENTATION: 定向力
- MEMORY: 记忆力
- ATTENTION: 注意力和计算力
- RECALL: 回忆力
- LANGUAGE: 语言能力
- VISUAL_SPATIAL: 视空间能力

## 注意事项
1. 所有接口都需要进行身份验证
2. 提交答案和批改答案接口需要验证参数完整性
3. 批改答案时，每个问题的得分必须为0或该问题的满分值
4. 总分必须等于所有问题得分的总和
5. 评估结果必须根据总分进行判断
6. 获取答案接口返回的数据结构较为复杂，前端需要做好解析处理
7. 分数分布统计接口可用于生成统计图表 
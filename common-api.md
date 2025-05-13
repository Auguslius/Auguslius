# 通用接口文档

## 1. 音频转文字接口

### 1.1 通过OSS音频URL转换为文字

#### 接口描述
将OSS上的音频文件转换为文字，基于科大讯飞长语音转写服务。

#### 请求信息
- **URL**: `/audio/transform`
- **方法**: GET
- **参数**: 
  - ossUrl: 音频文件的OSS URL地址

#### 响应信息
```json
{
  "code": "integer",    // 状态码
  "message": "string",  // 响应消息
  "data": "string"      // 转换后的文字内容
}
```

#### 错误码
- 转换成功：SUCCESS
- 转换失败：FAIL
- URL格式无效：返回错误信息"无效的URL格式，请确保URL以http://或https://开头"
- 下载文件失败：返回错误信息"从OSS下载文件失败: [具体错误信息]"
- 转写失败：返回错误信息"音频转文字失败: [具体错误信息]"
- 查询超时：返回错误信息"查询转写结果超时，请稍后尝试查询结果"

#### 注意事项
1. 音频文件必须存储在可公开访问的OSS上
2. 支持的音频格式包括MP3、WAV等常见格式
3. 转写过程可能需要较长时间，取决于音频长度
4. 系统会自动下载音频文件到临时目录，处理完成后删除

## 2. 媒体文件管理接口

### 2.1 上传媒体文件

#### 接口描述
上传媒体文件到系统。

#### 请求信息
- **URL**: `/media/upload`
- **方法**: POST
- **Content-Type**: multipart/form-data
- **参数**: 
  - file: 媒体文件（MultipartFile）

#### 响应信息
```json
{
  "code": "integer",    // 状态码
  "message": "string",  // 响应消息
  "data": {
    "id": "integer",           // 媒体文件ID
    "fileName": "string",      // 文件名
    "fileType": "string",      // 文件类型
    "fileSize": "integer",     // 文件大小（字节）
    "fileUrl": "string",       // 文件URL
    "createTime": "datetime",  // 创建时间
    "updateTime": "datetime"   // 更新时间
  }
}
```

#### 错误码
- 上传成功：UPLOAD_SUCCESS
- 上传失败：UPLOAD_FAIL

### 2.2 获取所有媒体文件

#### 接口描述
获取系统中所有的媒体文件列表。

#### 请求信息
- **URL**: `/media/list`
- **方法**: GET

#### 响应信息
```json
{
  "code": "integer",    // 状态码
  "message": "string",  // 响应消息
  "data": [{
    "id": "integer",           // 媒体文件ID
    "fileName": "string",      // 文件名
    "fileType": "string",      // 文件类型
    "fileSize": "integer",     // 文件大小（字节）
    "fileUrl": "string",       // 文件URL
    "createTime": "datetime",  // 创建时间
    "updateTime": "datetime"   // 更新时间
  }]
}
```

#### 错误码
- 查询成功：GET_FILE_LIST_SUCCESS
- 查询失败：GET_FILE_LIST_FAIL

### 2.3 根据ID获取媒体文件

#### 接口描述
根据ID查询单个媒体文件信息。

#### 请求信息
- **URL**: `/media/{id}`
- **方法**: GET
- **参数**: id - 媒体文件ID（路径参数）

#### 响应信息
```json
{
  "code": "integer",    // 状态码
  "message": "string",  // 响应消息
  "data": {
    "id": "integer",           // 媒体文件ID
    "fileName": "string",      // 文件名
    "fileType": "string",      // 文件类型
    "fileSize": "integer",     // 文件大小（字节）
    "fileUrl": "string",       // 文件URL
    "createTime": "datetime",  // 创建时间
    "updateTime": "datetime"   // 更新时间
  }
}
```

#### 错误码
- 查询成功：GET_FILE_SUCCESS
- 查询失败：GET_FILE_FAIL

### 2.4 删除媒体文件

#### 接口描述
根据ID删除媒体文件。

#### 请求信息
- **URL**: `/media/{id}`
- **方法**: DELETE
- **参数**: id - 媒体文件ID（路径参数）

#### 响应信息
```json
{
  "code": "integer",    // 状态码
  "message": "string",  // 响应消息
  "data": null
}
```

#### 错误码
- 删除成功：DELETE_FILE_SUCCESS
- 删除失败：DELETE_FILE_FAIL

## 3. 老人基础信息接口

### 3.1 获取老人数据基础分布

#### 接口描述
获取老人数据的基础分布信息。

#### 请求信息
- **URL**: `/elderlyBasicInfo/list`
- **方法**: GET
- **请求头**: 
  - Authorization: JWT令牌

#### 响应信息
```json
{
  "code": "integer",    // 状态码
  "message": "string",  // 响应消息
  "data": "string"      // 老人数据基础分布信息
}
```

#### 错误码
- 查询成功：返回"老人数据基础分布"
- 查询失败：返回"token校验失败"，HTTP状态码401

#### 注意事项
1. 接口需要JWT令牌认证
2. 令牌无效或过期将返回401状态码
3. 令牌格式应为"Bearer [token]"或直接为token字符串

## 数据字典

### 媒体文件字段说明
- id: 媒体文件唯一标识
- fileName: 媒体文件名称
- fileType: 媒体文件类型（如mp3、wav、mp4等）
- fileSize: 媒体文件大小（字节）
- fileUrl: 媒体文件访问URL
- createTime: 创建时间
- updateTime: 更新时间

## 注意事项
1. 所有接口都需要进行参数验证
2. 音频转写接口需要确保OSS URL可访问
3. 媒体文件上传有大小限制，请参考系统配置
4. 老人基础信息接口需要有效的JWT令牌
5. 所有接口都支持跨域访问（CORS） 
# 语音转文本比对模块接口文档

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

## 2. 文本相似度比对接口

### 2.1 计算两段文本的相似度

#### 接口描述
使用多种算法计算两段文本的相似度，包括余弦相似度、Jaccard相似度、编辑距离相似度和汉明距离相似度，并计算平均相似度。

#### 请求信息
- **URL**: `/text/similarity`
- **方法**: GET
- **参数**: 
  - textA: 第一段文本（必填）
  - textB: 第二段文本（必填）

#### 响应信息
```json
{
  "code": "integer",    // 状态码
  "message": "string",  // 响应消息
  "data": {
    "cosine": "double",      // 余弦相似度（0-1之间）
    "jaccard": "double",     // Jaccard相似度（0-1之间）
    "levenshtein": "double", // 编辑距离相似度（0-1之间）
    "hamming": "double",     // 汉明距离相似度（0-1之间）
    "average": "double"      // 平均相似度（0-1之间）
  }
}
```

#### 错误码
- 计算成功：SUCCESS
- 计算失败：FAIL

#### 算法说明
1. **余弦相似度**：基于向量空间模型，计算两个文本向量之间的夹角余弦值
2. **Jaccard相似度**：计算两个集合的交集与并集的比值
3. **编辑距离相似度**：基于Levenshtein距离，计算将一个字符串转换为另一个字符串所需的最少操作次数
4. **汉明距离相似度**：计算两个等长字符串之间对应位置不同字符的个数
5. **平均相似度**：上述四种算法的平均值

## 3. 语音转文本比对流程

### 3.1 完整比对流程

#### 流程描述
1. 上传音频文件到OSS，获取音频URL
2. 调用音频转文字接口，将音频转换为文本A
3. 准备标准答案文本B
4. 调用文本相似度比对接口，计算文本A与文本B的相似度
5. 根据相似度结果评估答案的正确性

#### 示例代码
```javascript
// 前端示例代码
async function compareAudioWithText(audioUrl, standardText) {
  try {
    // 1. 调用音频转文字接口
    const audioResponse = await fetch(`/audio/transform?ossUrl=${encodeURIComponent(audioUrl)}`);
    const audioResult = await audioResponse.json();
    
    if (audioResult.code !== 200) {
      throw new Error(`音频转文字失败: ${audioResult.message}`);
    }
    
    const transcribedText = audioResult.data;
    
    // 2. 调用文本相似度比对接口
    const similarityResponse = await fetch(`/text/similarity?textA=${encodeURIComponent(transcribedText)}&textB=${encodeURIComponent(standardText)}`);
    const similarityResult = await similarityResponse.json();
    
    if (similarityResult.code !== 200) {
      throw new Error(`文本相似度计算失败: ${similarityResult.message}`);
    }
    
    return similarityResult.data;
  } catch (error) {
    console.error('比对过程出错:', error);
    throw error;
  }
}
```

## 数据字典

### 相似度指标说明
- cosine: 余弦相似度，范围0-1，越接近1表示越相似
- jaccard: Jaccard相似度，范围0-1，越接近1表示越相似
- levenshtein: 编辑距离相似度，范围0-1，越接近1表示越相似
- hamming: 汉明距离相似度，范围0-1，越接近1表示越相似
- average: 平均相似度，范围0-1，越接近1表示越相似

## 注意事项
1. 音频转文字接口需要确保OSS URL可访问
2. 文本相似度比对接口对文本长度没有严格限制，但过长的文本可能会影响性能
3. 不同相似度算法适用于不同场景，建议根据实际需求选择合适的算法
4. 平均相似度仅供参考，实际应用中可能需要根据具体场景调整权重
5. 对于中文文本，系统会自动进行分词处理
6. 所有接口都支持跨域访问（CORS） 
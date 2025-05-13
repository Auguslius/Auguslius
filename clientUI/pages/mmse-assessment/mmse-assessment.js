// pages/mmse-assessment/mmse-assessment.js
const { mmseApi } = require('../../api/index');
const router = require('../../utils/router');
const app = getApp(); // 获取应用实例
const config = require('../../config/index'); // 引入配置

// 所属大项选项
const sectionOptions = [
  { label: '定向力', value: 'Orientation' },
  { label: '记忆力', value: 'Memory' },
  { label: '计算力', value: 'Calculation' },
  { label: '回忆能力', value: 'Recall' },
  { label: '语言能力', value: 'Language' }
];

// 问题类型选项
const questionTypeOptions = [
  { label: '时间定向', value: 'TIME' },
  { label: '地点定向', value: 'LOCATION' },
  { label: '即刻记忆', value: 'IMMEDIATE_MEMORY' },
  { label: '连续计算', value: 'SERIAL_CALCULATION' },
  { label: '延迟记忆', value: 'DELAYED_MEMORY' },
  { label: '视觉命名', value: 'VISUAL_NAMING' },
  { label: '语句重复', value: 'REPETITION' },
  { label: '命令执行', value: 'COMMAND_EXECUTION' },
  { label: '书写能力', value: 'WRITING' },
  { label: '图形复制', value: 'FIGURE_COPYING' }
];

// 比对种类选项（原URL种类）
const urlCategoryOptions = [
  { label: '图片', value: 'IMAGE' },
  { label: '视频', value: 'VIDEO' },
  { label: '音频', value: 'AUDIO' },
  { label: '文本', value: 'TEXT' }
];

// 验证方法选项
const validationMethodOptions = [
  { label: '代码验证', value: 'BY_CODE' },
  { label: '数据验证', value: 'BY_DATA' }
];

Page({
  data: {
    mmseQuestions: [],
    loading: false,
    currentSectionIndex: -1,
    sections: [],
    answers: {},
    patientUuid: '', // 患者ID
    // 录音相关数据
    recordingInfo: {
      isRecording: false, // 是否正在录音
      position: null,     // 当前录音的题目位置
      duration: 0,        // 录音时长（秒）
      recorderManager: null, // 录音管理器
      timer: null         // 计时器
    }
  },

  onLoad: function() {
    // 从用户信息中获取uuid作为patientUuid
    if (app.globalData && app.globalData.userInfo && app.globalData.userInfo.uuid) {
      this.setData({
        patientUuid: app.globalData.userInfo.uuid
      });
    }
    this.fetchMMSEQuestions();
    
    // 初始化录音管理器
    this.initRecorderManager();
  },

  // 获取MMSE题目
  fetchMMSEQuestions: function() {
    this.setData({ loading: true });
    
    mmseApi.getMMSEQuestions().then(res => {
      if (res && (res.code === 0 || res.code === 200) && res.data) {
        const questions = res.data;
        
        // 美化字段，将代码值转换为标签文本，并确保每个问题都有position字段
        const formattedQuestions = questions.map((q, index) => {
          return {
            ...q,
            // 如果没有position字段，使用index+1作为position
            position: q.position || (index + 1),
            // 转换所属大项
            sectionLabel: this.getLabel(sectionOptions, q.section) || q.section,
            // 转换问题类型
            questionTypeLabel: this.getLabel(questionTypeOptions, q.questionType) || q.questionType,
            // 转换URL种类
            urlCategoryLabel: this.getLabel(urlCategoryOptions, q.urlCategory) || q.urlCategory,
            // 转换验证方法
            validationMethodLabel: this.getLabel(validationMethodOptions, q.validationMethod) || q.validationMethod
          };
        });
        
        // 按section分组
        const sectionMap = {};
        formattedQuestions.forEach(q => {
          const sectionKey = q.section; // 使用原始section值作为键
          if (!sectionMap[sectionKey]) {
            sectionMap[sectionKey] = [];
          }
          sectionMap[sectionKey].push(q);
        });
        console.log('sectionMap', sectionMap);
        
        // 转换为数组，使用格式化后的sectionLabel
        const sections = Object.keys(sectionMap).map(key => {
          // 查找第一个题目的sectionLabel作为分组名称
          const sectionLabel = sectionMap[key][0].sectionLabel;
          return {
            name: sectionLabel,
            key: key,
            questions: sectionMap[key]
          };
        });

        this.setData({
          mmseQuestions: formattedQuestions,
          sections: sections,
          currentSectionIndex: sections.length > 0 ? 0 : -1,
          loading: false
        });
      } else {
        wx.showToast({
          title: '获取题目失败',
          icon: 'none'
        });
        this.setData({ loading: false });
      }
    }).catch(err => {
      wx.showToast({
        title: '获取题目失败',
        icon: 'none'
      });
      this.setData({ loading: false });
    });
  },
  
  // 获取标签文本的辅助函数
  getLabel: function(options, value) {
    if (!value) return '';
    const option = options.find(opt => opt.value.toUpperCase() === value.toUpperCase());
    return option ? option.label : value;
  },
  
  // 处理答案输入
  onAnswerInput: function(e) {
    const value = e.detail.value;
    const position = e.currentTarget.dataset.position;
    
    console.log('使用position:', position, '提交答案:', value);
    
    // 直接更新答案，整合了submitAnswer的逻辑
    const answers = this.data.answers;
    answers[position] = {
      answer: value
    };
    
    this.setData({
      answers: answers
    });
  },
  
  // 切换到下一部分
  nextSection: function() {
    const nextIndex = this.data.currentSectionIndex + 1;
    if (nextIndex < this.data.sections.length) {
      // 先更新状态
      this.setData({
        currentSectionIndex: nextIndex
      }, () => {
        // 状态更新完成后，确保滚动到顶部
        setTimeout(() => {
          wx.pageScrollTo({
            scrollTop: 0,
            duration: 100
          });
        }, 50);
      });
    } else {
      // 已经是最后一部分，提交答案
      this.submitAnswers();
    }
  },
  
  // 切换到上一部分
  prevSection: function() {
    const prevIndex = this.data.currentSectionIndex - 1;
    if (prevIndex >= 0) {
      // 先更新状态
      this.setData({
        currentSectionIndex: prevIndex
      }, () => {
        // 状态更新完成后，确保滚动到顶部
        setTimeout(() => {
          wx.pageScrollTo({
            scrollTop: 0,
            duration: 100
          });
        }, 50);
      });
    }
  },
  
  // 初始化录音管理器
  initRecorderManager: function() {
    const recorderManager = wx.getRecorderManager();
    
    // 监听录音开始事件
    recorderManager.onStart(() => {
      console.log('录音开始');
      // 开始计时
      const timerInterval = setInterval(() => {
        const newDuration = this.data.recordingInfo.duration + 1;
        this.setData({
          'recordingInfo.duration': newDuration
        });
      }, 1000);
      
      this.setData({
        'recordingInfo.timer': timerInterval
      });
    });
    
    // 监听录音停止事件
    recorderManager.onStop((res) => {
      console.log('录音停止', res);
      // 停止计时
      clearInterval(this.data.recordingInfo.timer);
      
      // 获取录音文件路径
      const { tempFilePath } = res;
      const position = this.data.recordingInfo.position;
      
      if (position) {
        // 更新答案状态，标记为上传中
        const answers = this.data.answers;
        if (!answers[position]) {
          answers[position] = {};
        }
        answers[position].tempFilePath = tempFilePath;
        answers[position].isUploading = true;
        answers[position].uploadSuccess = false;
        
        this.setData({
          answers: answers,
          'recordingInfo.isRecording': false
        });
        
        // 上传音频文件
        this.uploadAudioFile(position, tempFilePath);
      }
    });
    
    // 监听录音错误事件
    recorderManager.onError((res) => {
      console.error('录音错误:', res);
      wx.showToast({
        title: '录音失败: ' + res.errMsg,
        icon: 'none'
      });
      
      // 清理录音状态
      clearInterval(this.data.recordingInfo.timer);
      this.setData({
        'recordingInfo.isRecording': false,
        'recordingInfo.duration': 0,
        'recordingInfo.timer': null
      });
    });
    
    this.setData({
      'recordingInfo.recorderManager': recorderManager
    });
  },
  
  // 开始录音
  startRecording: function(e) {
    const position = e.currentTarget.dataset.position;
    console.log('开始录音，题目位置:', position);
    
    // 请求录音权限
    wx.authorize({
      scope: 'scope.record',
      success: () => {
        // 如果已经在录音，先停止
        if (this.data.recordingInfo.isRecording) {
          this.stopRecording();
        }
        
        // 重置录音状态
        this.setData({
          'recordingInfo.isRecording': true,
          'recordingInfo.position': position,
          'recordingInfo.duration': 0
        });
        
        // 开始录音
        const recorderManager = this.data.recordingInfo.recorderManager;
        const options = {
          duration: 60000, // 最长60秒
          sampleRate: 16000,
          numberOfChannels: 1,
          encodeBitRate: 48000,
          format: 'mp3',
          frameSize: 50
        };
        recorderManager.start(options);
      },
      fail: (res) => {
        console.error('录音授权失败:', res);
        wx.showToast({
          title: '请授权录音权限',
          icon: 'none'
        });
      }
    });
  },
  
  // 停止录音
  stopRecording: function() {
    if (this.data.recordingInfo.isRecording) {
      const recorderManager = this.data.recordingInfo.recorderManager;
      recorderManager.stop();
    }
  },
  
  // 上传音频文件
  uploadAudioFile: function(position, filePath) {
    wx.showLoading({
      title: '上传中...',
      mask: true
    });
    
    // 使用API模块上传文件
    mmseApi.uploadMedia(filePath)
      .then(res => {
        // 上传成功
        console.log('音频上传成功:', res);
        
        // 更新答案状态
        const answers = this.data.answers;
        answers[position].isUploading = false;
        answers[position].uploadSuccess = true;
        answers[position].audioPath = res.data.url;
        answers[position].answer = res.data.url; // 将URL作为答案
        
        this.setData({
          answers: answers
        });
        
        wx.showToast({
          title: '上传成功',
          icon: 'success'
        });
      })
      .catch(err => {
        // 上传失败
        console.error('上传失败:', err);
        this.handleUploadFail(position, err.message || '上传失败');
      });
  },
  
  // 处理上传失败
  handleUploadFail: function(position, errorMsg) {
    const answers = this.data.answers;
    answers[position].isUploading = false;
    answers[position].uploadSuccess = false;
    
    this.setData({
      answers: answers
    });
    
    wx.showToast({
      title: '上传失败: ' + errorMsg,
      icon: 'none'
    });
  },
  
  // 提交答案
  submitAnswers: function() {
    // 检查是否有正在上传的音频
    let hasUploading = false;
    for (const position in this.data.answers) {
      if (this.data.answers[position].isUploading) {
        hasUploading = true;
        break;
      }
    }
    
    if (hasUploading) {
      wx.showModal({
        title: '提示',
        content: '有音频正在上传，请等待上传完成后再提交',
        showCancel: false
      });
      return;
    }
    
    // 构建答案数据
    const answersMap = {};
    const { answers, mmseQuestions } = this.data;
    
    // 确保所有题目都有对应条目，即使没有回答
    mmseQuestions.forEach(question => {
      const position = question.position;
      if (position) {
        // 查找是否已有答案
        const userAnswer = answers[position] ? (answers[position].answer || '') : '';
        
        // 创建答案对象
        answersMap[position] = {
          value: userAnswer,
          section: question.section || '',
          questionType: question.questionType || '',
          validationMethod: question.validationMethod || 'BY_CODE',
          expectedAnswer: question.expectedAnswer || '',
          timeSpent: 0 // 暂不收集时间
        };
        
        // 如果是音频类型的问题，且有上传成功的音频，添加音频URL
        if (question.urlCategory === 'AUDIO' && answers[position] && answers[position].audioPath) {
          answersMap[position].audioUrl = answers[position].audioPath;
        }
        
        // 如果是图片类型的问题，且有上传成功的图片，添加图片URL
        if (question.urlCategory === 'IMAGE' && answers[position] && answers[position].imagePath) {
          answersMap[position].imageUrl = answers[position].imagePath;
        }
      }
    });
    
    // 处理可能有的额外答案（用户回答但题目中不存在的）
    Object.keys(answers).forEach(position => {
      if (!answersMap[position]) {
        const answer = answers[position];
        answersMap[position] = {
          value: answer.answer || '',
          section: '',
          questionType: '',
          validationMethod: 'BY_CODE',
          expectedAnswer: '',
          timeSpent: 0
        };
        
        // 如果有音频URL也添加
        if (answer.audioPath) {
          answersMap[position].audioUrl = answer.audioPath;
        }
        
        // 如果有图片URL也添加
        if (answer.imagePath) {
          answersMap[position].imageUrl = answer.imagePath;
        }
      }
    });
    
    const submitData = {
      patientUuid: this.data.patientUuid || '',
      answersMap: answersMap
    };
    
    // 使用API模块提交答案
    mmseApi.submitMMSEAnswers(submitData)
      .then(res => {
        // 提交成功
        wx.showModal({
          title: '提交成功',
          content: 'MMSE评估答案已成功提交',
          showCancel: false,
          success: (modalRes) => {
            if (modalRes.confirm) {
              router.navigateBack();
            }
          }
        });
      })
      .catch(err => {
        // 提交失败
        wx.showModal({
          title: '提交失败',
          content: '答案提交失败：' + (err.message || '未知错误'),
          showCancel: false
        });
      });
  },
  
  // 预览图片
  previewImage: function(e) {
    const url = e.currentTarget.dataset.url;
    if (!url) return;
    
    wx.previewImage({
      current: url,
      urls: [url]
    });
  },

  // 预览已上传的图片
  previewUploadedImage: function(e) {
    const url = e.currentTarget.dataset.url;
    if (!url) return;
    
    wx.previewImage({
      current: url,
      urls: [url]
    });
  },
  
  // 选择图片
  chooseImage: function(e) {
    const position = e.currentTarget.dataset.position;
    console.log('选择图片，题目位置:', position);
    
    // 调用系统相册选择图片
    wx.chooseMedia({
      count: 1,
      mediaType: ['image'],
      sourceType: ['album', 'camera'],
      camera: 'back',
      success: (res) => {
        // 获取选中图片的临时路径
        const tempFilePath = res.tempFiles[0].tempFilePath;
        
        // 更新答案状态，标记为上传中
        const answers = this.data.answers;
        if (!answers[position]) {
          answers[position] = {};
        }
        answers[position].tempFilePath = tempFilePath;
        answers[position].isUploading = true;
        answers[position].uploadSuccess = false;
        answers[position].imagePath = tempFilePath; // 先显示本地临时路径
        
        this.setData({
          answers: answers
        });
        
        // 上传图片文件
        this.uploadImageFile(position, tempFilePath);
      },
      fail: (err) => {
        console.error('选择图片失败:', err);
        wx.showToast({
          title: '选择图片失败',
          icon: 'none'
        });
      }
    });
  },
  
  // 上传图片文件
  uploadImageFile: function(position, filePath) {
    wx.showLoading({
      title: '上传中...',
      mask: true
    });
    
    // 使用API模块上传文件
    mmseApi.uploadMedia(filePath)
      .then(res => {
        // 上传成功
        console.log('图片上传成功:', res);
        
        // 更新答案状态
        const answers = this.data.answers;
        answers[position].isUploading = false;
        answers[position].uploadSuccess = true;
        answers[position].imagePath = res.data.url;
        answers[position].answer = res.data.url; // 将URL作为答案
        
        this.setData({
          answers: answers
        });
        
        wx.showToast({
          title: '上传成功',
          icon: 'success'
        });
      })
      .catch(err => {
        // 上传失败
        console.error('上传失败:', err);
        this.handleUploadFail(position, err.message || '上传失败');
      });
  }
}); 
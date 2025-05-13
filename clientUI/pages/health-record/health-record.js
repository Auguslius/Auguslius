const { mmseApi } = require('../../api/index');
const app = getApp();

Page({
  data: {
    loading: false,
    userInfo: null,
    mmseAnswers: null,
    sectionMap: {
      'Orientation': '定向力',
      'Memory': '记忆力',
      'Calculation': '计算力',
      'Recall': '回忆能力',
      'Language': '语言能力'
    },
    questionTypeMap: {
      'TIME': '时间定向',
      'LOCATION': '地点定向',
      'IMMEDIATE_MEMORY': '即刻记忆',
      'SERIAL_CALCULATION': '连续计算',
      'DELAYED_MEMORY': '延迟记忆',
      'VISUAL_NAMING': '视觉命名',
      'REPETITION': '语句重复',
      'COMMAND_EXECUTION': '命令执行',
      'WRITING': '书写能力',
      'FIGURE_COPYING': '图形复制'
    },
    // 按部分分组的答案
    groupedAnswers: {}
  },

  onLoad: function(options) {
    // 获取用户信息
    this.getUserInfo();
  },
  
  onShow: function() {
    // 页面显示时刷新数据
    if (this.data.userInfo && this.data.userInfo.uuid) {
      this.loadMMSEAnswers(this.data.userInfo.uuid);
    }
  },
  
  // 获取用户信息
  getUserInfo: function() {
    const userInfo = app.globalData.userInfo || wx.getStorageSync('userInfo');
    
    if (userInfo) {
      this.setData({ 
        userInfo: userInfo 
      }, () => {
        // 加载MMSE答案
        this.loadMMSEAnswers(userInfo.uuid);
      });
    } else {
      wx.showToast({
        title: '请先登录',
        icon: 'none'
      });
    }
  },
  
  // 加载MMSE答案
  loadMMSEAnswers: function(patientUuid) {
    this.setData({ loading: true });
    
    mmseApi.getMMSEAnswers(patientUuid)
      .then(res => {
        if (res.code === 200 && res.data) {
          // 处理答案数据，按部分分组
          const answersMap = res.data;
          const groupedAnswers = this.groupAnswersBySection(answersMap);
          
          this.setData({
            mmseAnswers: answersMap,
            groupedAnswers: groupedAnswers,
            loading: false
          });
        } else {
          this.setData({ loading: false });
          wx.showToast({
            title: '加载MMSE答案失败',
            icon: 'none'
          });
        }
      })
      .catch(err => {
        console.error('加载MMSE答案出错:', err);
        this.setData({ loading: false });
        wx.showToast({
          title: err.message || '加载MMSE答案失败',
          icon: 'none'
        });
      });
  },
  
  // 按部分分组答案
  groupAnswersBySection: function(answersMap) {
    const groupedAnswers = {};
    
    // 遍历答案，按section分组
    Object.keys(answersMap).forEach(position => {
      const answer = answersMap[position];
      const section = answer.section;
      
      if (!groupedAnswers[section]) {
        groupedAnswers[section] = [];
      }
      
      // 确保图片和音频URL字段存在
      const enhancedAnswer = {
        position: position,
        ...answer,
        // 确保audioUrl和imageUrl字段存在
        audioUrl: answer.audioUrl || null,
        imageUrl: answer.imageUrl || null
      };
      
      groupedAnswers[section].push(enhancedAnswer);
    });
    
    // 对每个分组内的答案排序
    Object.keys(groupedAnswers).forEach(section => {
      groupedAnswers[section].sort((a, b) => a.position - b.position);
    });
    
    return groupedAnswers;
  },
  
  // 获取部分名称
  getSectionName: function(section) {
    return this.data.sectionMap[section] || section;
  },
  
  // 获取问题类型名称
  getQuestionTypeName: function(questionType) {
    return this.data.questionTypeMap[questionType] || questionType;
  },
  
  // 预览图片
  previewImage: function(e) {
    const url = e.currentTarget.dataset.url;
    if (!url) return;
    
    wx.previewImage({
      current: url,
      urls: [url]
    });
  }
}); 
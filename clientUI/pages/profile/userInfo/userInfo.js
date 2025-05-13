const { authenticationApi } = require('../../../api/index');
const errorUtil = require('../../../utils/error');

Page({
  data: {
    isAuthenticated: false,
    userInfo: null,
    authInfo: null,
    loadingAuth: false,
    showTopError: false,
    topErrorMsg: ''
  },

  onLoad(options) {
    // 获取是否已认证的参数
    const isAuthenticated = options.isAuthenticated === 'true';
    
    // 获取全局用户信息
    const app = getApp();
    const userInfo = app.globalData.userInfo || wx.getStorageSync('userInfo') || {};
    
    this.setData({
      isAuthenticated,
      userInfo
    });
    
    // 如果已认证，获取认证信息
    if (isAuthenticated && userInfo.uuid) {
      this.getAuthenticationInfo(userInfo.uuid);
    }
  },
  
  // 下拉刷新
  onPullDownRefresh() {
    // 获取全局用户信息
    const app = getApp();
    const userInfo = app.globalData.userInfo || wx.getStorageSync('userInfo') || {};
    
    if (this.data.isAuthenticated && userInfo.uuid) {
      this.getAuthenticationInfo(userInfo.uuid);
    } else {
      wx.stopPullDownRefresh();
    }
  },
  
  // 获取认证信息
  getAuthenticationInfo(uuid) {
    if (!uuid) {
      wx.stopPullDownRefresh();
      return;
    }
    
    this.setData({
      loadingAuth: true
    });
    
    if (!wx.getStorageSync('token')) {
      wx.stopPullDownRefresh();
      return;
    }
    
    wx.showLoading({
      title: '加载中',
      mask: true
    });
    
    // 使用更新后的API路径
    authenticationApi.isAuthentication(uuid).then(res => {
      wx.hideLoading();
      wx.stopPullDownRefresh();
      
      if (res.code === 200 && res.data) {
        // 处理性别显示
        let authInfo = {...res.data};
        if (authInfo.gender === 1) {
          authInfo.genderText = '男';
        } else if (authInfo.gender === 2) {
          authInfo.genderText = '女';
        } else {
          authInfo.genderText = '未知';
        }
        
        this.setData({
          authInfo,
          loadingAuth: false
        });
      } else {
        this.setData({
          loadingAuth: false
        });
        wx.showToast({
          title: '获取认证信息失败',
          icon: 'none'
        });
      }
    }).catch(error => {
      console.error('获取认证信息失败:', error);
      wx.hideLoading();
      wx.stopPullDownRefresh();
      this.setData({
        loadingAuth: false
      });
      errorUtil.handleError(this, '获取认证信息失败，请稍后重试');
    });
  },
  
  // 关闭错误提示
  closeTopError() {
    errorUtil.hideTopError(this);
  },
  
  // 前往认证页面
  goToAuthentication() {
    wx.navigateTo({
      url: '/pages/profile/authentication/authentication'
    });
  }
}); 
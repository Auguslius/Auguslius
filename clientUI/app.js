// app.js
const { loginApi } = require('./api/index');

App({
  isRedirectingToLogin: false, // 是否正在跳转到登录页的标记

  onLaunch() {
    // 展示本地存储能力
    const logs = wx.getStorageSync('logs') || []
    logs.unshift(Date.now())
    wx.setStorageSync('logs', logs)

    // 检查用户登录状态
    this.checkLoginStatus();
    
    // 从本地存储获取医生编号
    this.loadDoctorNumber();
  },

  // 检查用户登录状态
  checkLoginStatus() {
    try {
      const token = wx.getStorageSync('token');
      
      // 打印token用于调试
      console.log('app.js - token状态:', token ? '存在' : '不存在', token ? token.substring(0, 20) + '...' : '');
      
      if (token) {
        // 先从本地存储获取基本用户信息并设置全局变量
        const userInfo = wx.getStorageSync('userInfo');
        if (userInfo) {
          this.globalData.userInfo = userInfo;
          this.globalData.isLogin = true;
          console.log('app.js - 从本地存储读取用户信息成功');
        }
        
        // 再从服务器获取最新用户信息
        loginApi.getCurrentUser().then(res => {
          // 更新全局用户信息
          this.globalData.userInfo = res.data;
          this.globalData.isLogin = true;
          // 更新本地存储
          wx.setStorageSync('userInfo', res.data);
          console.log('app.js - 从服务器获取用户信息成功');
        }).catch((error) => {
          // 查询失败，可能是token过期
          console.error('app.js - 获取用户信息失败:', error);
          if (error.code === 401) {
            // token过期，清除登录状态
            this.clearLoginState();
          }
        });
      } else {
        // 没有token，未登录状态
        console.log('app.js - 无token');
        this.clearLoginState();
      }
    } catch (error) {
      console.error('检查登录状态失败', error);
      this.clearLoginState();
    }
  },
  
  // 清除登录状态
  clearLoginState() {
    console.log('清除全局登录状态');
    
    // 更新全局数据
    this.globalData.userInfo = null;
    this.globalData.isLogin = false;
    
    // 清除本地存储
    try {
      wx.removeStorageSync('token');
      wx.removeStorageSync('userInfo');
      console.log('已清除本地token和用户信息');
    } catch (error) {
      console.error('清除本地存储失败:', error);
    }
    
    // 通知页面更新状态
    this.updatePages();
  },

  // 通知页面更新状态
  updatePages() {
    // 获取当前所有页面
    const pages = getCurrentPages();
    if (pages.length > 0) {
      // 对每个页面调用checkLoginStatus方法(如果存在)
      pages.forEach(page => {
        if (page && page.checkLoginStatus) {
          console.log('更新页面状态:', page.route);
          page.checkLoginStatus();
        }
      });
    }
  },

  // 加载医生编号
  loadDoctorNumber() {
    try {
      const storedDoctorNumber = wx.getStorageSync('doctorNumber');
      if (storedDoctorNumber) {
        this.globalData.doctorNumber = storedDoctorNumber;
        console.log('app.js - 从本地存储读取医生编号:', storedDoctorNumber);
      } else {
        // 如果本地存储中没有，设置默认医生编号
        this.globalData.doctorNumber = 887375; // 默认医生工号
        console.log('app.js - 使用默认医生编号:', this.globalData.doctorNumber);
      }
    } catch (e) {
      console.error('app.js - 读取本地存储医生编号失败', e);
      // 设置默认值
      this.globalData.doctorNumber = 887375;
    }
  },

  globalData: {
    userInfo: null,
    isLogin: false,
    isAuthenticated: false, // 是否已认证为患者
    doctorNumber: 887375, // 默认医生工号
    appointmentTab: 'hospital' // 默认展示医院列表
  }
})

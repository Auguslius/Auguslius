// pages/profile/profile.js
const { loginApi, authenticationApi } = require('../../api/index');
const router = require('../../utils/router');
const errorUtil = require('../../utils/error');

Page({

  /**
   * 页面的初始数据
   */
  data: {
    isLogin: false,
    userInfo: null,
    showTopError: false, // 是否显示顶部错误提示
    topErrorMsg: '', // 顶部错误提示信息
    isAuthenticated: false, // 是否已完成认证
    authenticationInfo: null, // 认证信息
    items: [
      { icon: 'user-o', text: '个人资料', url: '/pages/profile/userInfo/userInfo' },
      { icon: 'checked', text: '患者认证', url: '/pages/profile/authentication/authentication' },
      { icon: 'friends-o', text: '我的医生', url: '/pages/profile/my-doctors/my-doctors' },
      { icon: 'records', text: '健康档案', url: '/pages/health-record/health-record' },
      { icon: 'like-o', text: '我的收藏', url: '' },
      { icon: 'comment-o', text: '意见反馈', url: '' }
    ]
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    this.checkLoginStatus();
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady() {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow() {
    this.checkLoginStatus();
  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide() {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload() {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh() {
    // 下拉刷新时重新检查登录和认证状态
    this.checkLoginStatus();
    wx.stopPullDownRefresh();
  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom() {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage() {

  },

  // 添加关闭顶部错误提示的方法
  closeTopError() {
    errorUtil.hideTopError(this);
  },

  // 检查登录状态
  checkLoginStatus() {
    try {
      const token = wx.getStorageSync('token');
      if (token) {
        const userInfo = wx.getStorageSync('userInfo') || {};
        this.setData({
          isLogin: true,
          userInfo: userInfo,
          showTopError: false
        });
        console.log('个人信息页面 - 用户已登录:', userInfo);
        
        // 检查用户认证状态
        this.checkAuthenticationStatus(userInfo.uuid);
      } else {
        this.setData({
          isLogin: false,
          userInfo: null,
          isAuthenticated: false,
          authenticationInfo: null,
          showTopError: false
        });
        console.log('个人信息页面 - 用户未登录');
      }
    } catch (error) {
      console.error('获取登录状态失败:', error);
      errorUtil.handleError(this, '获取用户信息失败');
    }
  },
  
  // 检查用户认证状态
  checkAuthenticationStatus(uuid) {
    if (!uuid) {
      console.log('无法检查认证状态: 缺少用户 UUID');
      return;
    }
    
    wx.showLoading({
      title: '加载中',
      mask: true
    });
    
    authenticationApi.isAuthentication(uuid).then(res => {
      wx.hideLoading();
      
      if (res.code === 200 && res.data) {
        // 更新状态，标记为已认证
        this.setData({
          isAuthenticated: true,
          authenticationInfo: res.data
        });
        
        // 更新全局用户信息
        const app = getApp();
        if (app.globalData.userInfo) {
          app.globalData.userInfo.isAuthenticated = true;
        }
        
        console.log('用户已完成认证:', res.data);
      } else {
        // 未认证或认证信息为空
        this.setData({
          isAuthenticated: false,
          authenticationInfo: null
        });
        console.log('用户未完成认证');
      }
    }).catch(error => {
      wx.hideLoading();
      console.error('检查认证状态失败:', error);
      
      // 设置为未认证
      this.setData({
        isAuthenticated: false,
        authenticationInfo: null
      });
    });
  },

  // 前往登录页
  goToLogin() {
    wx.navigateTo({
      url: '/pages/login/login'
    });
  },

  // 退出登录
  logout() {
    wx.showModal({
      title: '提示',
      content: '确定要退出登录吗？',
      success: (res) => {
        if (res.confirm) {
          // 显示加载中
          wx.showLoading({
            title: '退出中',
            mask: true
          });
          
          loginApi.logout().then(() => {
            // 清除本地存储的用户信息和token
            wx.removeStorageSync('token');
            wx.removeStorageSync('userInfo');
            
            // 更新全局状态
            const app = getApp();
            app.globalData.isLogin = false;
            app.globalData.userInfo = null;
            
            // 更新页面状态
            this.setData({
              isLogin: false,
              userInfo: null,
              isAuthenticated: false,
              authenticationInfo: null
            });
            
            wx.hideLoading();
            wx.showToast({
              title: '已退出登录',
              icon: 'success',
              duration: 1500,
              success: () => {
                // 切换到首页并触发onShow更新
                setTimeout(() => {
                  wx.switchTab({
                    url: '/pages/home/home',
                    success: () => {
                      console.log('跳转到首页成功');
                    }
                  });
                }, 500);
              }
            });
          }).catch(error => {
            wx.hideLoading();
            console.error('退出登录失败:', error);
            
            // 仍然清除本地状态
            wx.removeStorageSync('token');
            wx.removeStorageSync('userInfo');
            
            // 更新全局状态
            const app = getApp();
            app.globalData.isLogin = false;
            app.globalData.userInfo = null;
            
            // 更新页面状态
            this.setData({
              isLogin: false,
              userInfo: null,
              isAuthenticated: false,
              authenticationInfo: null
            });
            
            wx.showToast({
              title: '退出登录失败，请重试',
              icon: 'none'
            });
          });
        }
      }
    });
  },

  // 跳转到对应页面
  navigateTo(e) {
    const index = e.currentTarget.dataset.index;
    const item = this.data.items[index];
    
    // 检查登录状态
    if (!this.data.isLogin) {
      wx.showToast({
        title: '请先登录',
        icon: 'none'
      });
      return;
    }
    
    // 先检查URL是否存在
    if (!item.url) {
      wx.showToast({
        title: '功能开发中',
        icon: 'none'
      });
      return;
    }
    
    // 针对个人资料页面，传递认证信息
    if (index === 0) {
      try {
        const url = `${item.url}?isAuthenticated=${this.data.isAuthenticated}`;
        console.log('跳转到个人资料页面:', url);
        wx.navigateTo({
          url: url,
          fail: (err) => {
            console.error('跳转到个人资料页面失败:', err);
            wx.showToast({
              title: '页面跳转失败',
              icon: 'none'
            });
          }
        });
      } catch (error) {
        console.error('个人资料页面跳转错误:', error);
        wx.showToast({
          title: '页面跳转失败',
          icon: 'none'
        });
      }
      return;
    }
    
    // 针对患者认证页面，如果已认证则提示
    if (index === 1) {
      if (this.data.isAuthenticated) {
        wx.showToast({
          title: '您已完成认证',
          icon: 'success'
        });
        return;
      }
    }
    
    // 针对"我的医生"页面，传递医生工号
    if (index === 2) { // 我的医生是第三项，索引为2
      // 使用固定的默认医生工号
      const doctorNumber = 110110;
      const app = getApp();
      app.globalData.doctorNumber = doctorNumber;
      
      wx.navigateTo({
        url: item.url,
        fail: (err) => {
          console.error('跳转到我的医生页面失败:', err);
          wx.showToast({
            title: '页面跳转失败',
            icon: 'none'
          });
        }
      });
      return;
    }
    
    // 其他页面直接跳转
    wx.navigateTo({
      url: item.url,
      fail: (err) => {
        console.error('页面跳转失败:', err);
        wx.showToast({
          title: '页面跳转失败',
          icon: 'none'
        });
      }
    });
  }
})
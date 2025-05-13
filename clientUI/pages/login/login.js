const { loginApi } = require('../../api/index');
const config = require('../../config/index');
const router = require('../../utils/router');
const errorUtil = require('../../utils/error');

Page({
  data: {
    username: '', // 用户名
    password: '', // 密码
    code: '', // 验证码
    captchaId: '', // 验证码ID
    image: '', // 验证码图片
    countdown: 0, // 验证码倒计时
    codeExpired: false, // 验证码是否过期
    isLoading: false, // 是否正在登录中
    showTopError: false, // 是否显示顶部错误提示
    topErrorMsg: '' // 顶部错误提示信息
  },

  onLoad() {
    // 页面加载时获取验证码
    this.getCode();
    
    // 检查是否已有token
    this.checkToken();
  },

  // 检查是否已有token
  checkToken() {
    try {
      const token = wx.getStorageSync(config.TOKEN_KEY);
      console.log('当前token状态:', token ? '已存在' : '不存在', token ? token.substring(0, 20) + '...' : '');
      
      // 如果已有token，尝试验证是否有效
      if (token) {
        loginApi.getCurrentUser()
          .then(res => {
            console.log('token有效，自动跳转到首页');
            // 使用switchTab跳转到tabBar页面
            router.switchTab(router.pages.home);
          })
          .catch(err => {
            console.log('token无效，需要重新登录');
            // token无效，清除存储
            wx.removeStorageSync(config.TOKEN_KEY);
            wx.removeStorageSync(config.USER_INFO_KEY);
          });
      }
    } catch (error) {
      console.error('检查token出错:', error);
    }
  },

  // 获取验证码
  getCode() {
    this.setData({
      errorMessage: '' // 清除错误消息
    });
    
    wx.showLoading({
      title: '获取验证码中',
    });
    
    loginApi.getCode().then(res => {
      wx.hideLoading();
      
      if (res.data && res.data.captchaId) {
        this.setData({
          captchaId: res.data.captchaId,
          image: res.data.image,
          countdown: res.data.expire || 120,
          codeExpired: false
        });
        
        // 启动倒计时
        this.startCountdown();
      } else {
        errorUtil.handleError(this, res.message || '获取验证码失败');
      }
    }).catch(error => {
      wx.hideLoading();
      errorUtil.handleError(this, error);
    });
  },

  // 开始倒计时
  startCountdown() {
    // 清除旧的定时器
    if (this.countdownTimer) {
      clearInterval(this.countdownTimer);
    }
    
    this.countdownTimer = setInterval(() => {
      if (this.data.countdown > 0) {
        this.setData({
          countdown: this.data.countdown - 1
        });
      } else {
        // 验证码过期
        clearInterval(this.countdownTimer);
        this.setData({
          codeExpired: true
        });
      }
    }, 1000);
  },

  // 页面隐藏时清除定时器
  onHide() {
    if (this.countdownTimer) {
      clearInterval(this.countdownTimer);
    }
  },

  // 页面卸载时清除定时器
  onUnload() {
    if (this.countdownTimer) {
      clearInterval(this.countdownTimer);
    }
  },

  // 输入用户名
  onUsernameInput(e) {
    this.setData({
      username: e.detail.value,
      showTopError: false // 输入时隐藏错误提示
    });
  },

  // 输入密码
  onPasswordInput(e) {
    this.setData({
      password: e.detail.value,
      showTopError: false // 输入时隐藏错误提示
    });
  },

  // 输入验证码
  onCodeInput(e) {
    this.setData({
      code: e.detail.value,
      showTopError: false // 输入时隐藏错误提示
    });
  },

  // 点击刷新验证码
  refreshCode() {
    this.getCode();
  },

  // 登录
  login() {
    const { username, password, code, captchaId, codeExpired, isLoading } = this.data;
    
    // 如果正在登录中，不处理
    if (isLoading) {
      return;
    }
    
    // 验证码已过期，重新获取
    if (codeExpired) {
      errorUtil.handleError(this, '验证码已过期，请重新获取', {
        refreshCodeFunc: this.getCode.bind(this)
      });
      return;
    }
    
    // 参数校验
    if (!username) {
      errorUtil.handleError(this, '用户名不能为空');
      return;
    }
    
    if (!password) {
      errorUtil.handleError(this, '密码不能为空');
      return;
    }
    
    if (!code) {
      errorUtil.handleError(this, '验证码不能为空');
      return;
    }
    
    // 设置登录中状态
    this.setData({
      isLoading: true,
      errorMessage: '' // 清除错误消息
    });
    
    // 显示加载中
    wx.showLoading({
      title: '登录中',
      mask: true
    });
    
    // 调用登录接口
    loginApi.login({
      username,
      password,
      code,
      captchaId
    }).then(res => {
      wx.hideLoading();
      
      // 检查返回结果
      if (!res.data || !res.data.token) {
        errorUtil.handleError(this, res.message || '登录失败，返回结果异常', {
          refreshCodeFunc: this.getCode.bind(this)
        });
        return;
      }
      
      // 保存token和用户信息
      try {
        // 确保token没有额外空格或换行符
        const cleanToken = res.data.token.trim();
        wx.setStorageSync('token', cleanToken);
                
        // 保存用户信息
        const userInfo = {
          uuid: res.data.uuid,
          username: res.data.username
        };
        wx.setStorageSync('userInfo', userInfo);
        
        // 更新全局数据
        const app = getApp();
        app.globalData.isLogin = true;
        app.globalData.userInfo = userInfo;
        
        // 打印token信息
        console.log('登录成功，已保存token:', cleanToken.substring(0, 20) + '...');
      } catch (error) {
        console.error('保存token出错:', error);
      }
      
      // 登录成功提示
      wx.showToast({
        title: '登录成功',
        icon: 'success',
        duration: 1500,
        success: () => {
          // 清除定时器
          if (this.countdownTimer) {
            clearInterval(this.countdownTimer);
          }
          
          // 跳转到首页
          setTimeout(() => {
            this.setData({
              isLoading: false
            });
            
            // 使用switchTab跳转到tabBar页面
            wx.switchTab({
              url: '/pages/home/home',
              success: () => {
                console.log('跳转到首页成功');
              },
              fail: (error) => {
                console.error('switchTab失败:', error);
                // 如果switchTab失败，尝试重置导航栈
                wx.reLaunch({
                  url: '/pages/home/home',
                  fail: (relaunchError) => {
                    console.error('reLaunch也失败:', relaunchError);
                  }
                });
              }
            });
          }, 1500);
        }
      });
    }).catch((error) => {
      // 隐藏加载中
      wx.hideLoading();
      
      // 使用错误工具处理错误
      errorUtil.handleError(this, error, {
        refreshCodeFunc: this.getCode.bind(this)
      });
    });
  },
  
  // 跳转到注册页面
  goToRegister() {
    wx.navigateTo({
      url: '/pages/register/register'
    });
  },

  // 添加关闭顶部错误提示的方法
  closeTopError() {
    errorUtil.hideTopError(this);
  }
}); 
const { registerApi } = require('../../api/index');
const router = require('../../utils/router');
const errorUtil = require('../../utils/error');

Page({
  data: {
    username: '', // 用户名
    password: '', // 密码
    confirmPassword: '', // 确认密码
    code: '', // 验证码
    captchaId: '', // 验证码ID
    image: '', // 验证码图片
    countdown: 0, // 验证码倒计时
    codeExpired: false, // 验证码是否过期
    isLoading: false, // 是否正在注册中
    showTopError: false, // 是否显示顶部错误提示
    topErrorMsg: '' // 顶部错误提示信息
  },

  onLoad() {
    // 页面加载时获取验证码
    this.getCode();
  },

  // 获取验证码
  getCode() {
    this.setData({
      errorMessage: '' // 清除错误消息
    });
    
    wx.showLoading({
      title: '获取验证码中',
    });
    
    registerApi.getCode().then(res => {
      wx.hideLoading();
      
      if (res.data && res.data.captchaId) {
        this.setData({
          captchaId: res.data.captchaId,
          image: res.data.image,
          countdown: res.data.expire || 120, // 接口文档中验证码有效期为120秒
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

  // 输入确认密码
  onConfirmPasswordInput(e) {
    this.setData({
      confirmPassword: e.detail.value,
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

  // 注册
  register() {
    const { 
      username, 
      password, 
      confirmPassword, 
      code, 
      captchaId, 
      codeExpired, 
      isLoading 
    } = this.data;
    
    // 如果正在注册中，不处理
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
    
    if (!confirmPassword) {
      errorUtil.handleError(this, '确认密码不能为空');
      return;
    }
    
    if (password !== confirmPassword) {
      errorUtil.handleError(this, '两次密码输入不一致');
      return;
    }
    
    if (!code) {
      errorUtil.handleError(this, '验证码不能为空');
      return;
    }
    
    // 设置注册中状态
    this.setData({
      isLoading: true,
      errorMessage: '' // 清除错误消息
    });
    
    // 显示加载中
    wx.showLoading({
      title: '注册中',
      mask: true
    });
    
    // 准备请求参数
    const params = {
      username,
      password,
      confirmPassword,
      code,
      captchaId
    };
    
    // 调用注册接口
    registerApi.register(params).then(res => {
      wx.hideLoading();
      
      if (res.code === 200) {
        // 注册成功
        wx.showToast({
          title: '注册成功',
          icon: 'success',
          duration: 2000,
          success: () => {
            // 清除定时器
            if (this.countdownTimer) {
              clearInterval(this.countdownTimer);
            }
            
            // 跳转到登录页
            setTimeout(() => {
              this.setData({
                isLoading: false
              });
              
              wx.navigateBack({
                delta: 1
              });
            }, 2000);
          }
        });
      } else {
        // 注册失败
        errorUtil.handleError(this, res.message || '注册失败', {
          refreshCodeFunc: this.getCode.bind(this)
        });
      }
    }).catch(error => {
      // 隐藏加载中
      wx.hideLoading();
      
      // 统一处理错误
      errorUtil.handleError(this, error, {
        refreshCodeFunc: this.getCode.bind(this)
      });
    });
  },
  
  // 返回登录页
  backToLogin() {
    wx.navigateTo({
      url: '/pages/login/login'
    });
  },

  // 添加关闭顶部错误提示的方法
  closeTopError() {
    errorUtil.hideTopError(this);
  }
}); 
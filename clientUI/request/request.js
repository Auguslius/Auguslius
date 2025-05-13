// 导入配置
const config = require('../config/index');
const router = require('../utils/router');

/**
 * 统一请求封装
 * @param {String} url 请求路径
 * @param {Object} options 请求参数
 * @param {Boolean} showLoading 是否显示加载中
 * @returns {Promise} Promise对象
 */
function request(url, options = {}, showLoading = true) {
  if (showLoading) {
    wx.showLoading({
      title: '加载中',
      mask: true
    });
  }

  // 获取token
  const token = wx.getStorageSync(config.TOKEN_KEY) || '';
  
  // 设置请求头
  const header = {
    'Content-Type': 'application/json',
    ...options.header
  };
  
  // 如果有token，添加到请求头
  if (token) {
    // 确保token格式正确，不包含特殊字符或换行符
    const cleanToken = token.trim();
    header.Authorization = ` ${cleanToken}`;
    console.log('发送请求的Authorization头:', header.Authorization);
  }

  return new Promise((resolve, reject) => {
    wx.request({
      url: `${config.BASE_URL}${url}`,
      method: options.method || 'GET',
      data: options.data || {},
      header,
      timeout: config.API_TIMEOUT || 10000,
      success: (res) => {
        if (showLoading) {
          wx.hideLoading();
        }
        
        // 输出响应结果，方便调试
        console.log(`${options.method || 'GET'} ${url} 响应:`, res.data);

        // 处理HTTP状态码错误
        if (res.statusCode < 200 || res.statusCode >= 300) {
          handleError(res, reject);
          return;
        }
        
        // 处理业务状态码
        handleBusinessCode(res.data, resolve, reject);
      },
      fail: (err) => {
        if (showLoading) {
          wx.hideLoading();
        }
        // 处理网络错误
        handleNetworkError(err, reject);
      }
    });
  });
}

/**
 * 处理HTTP错误
 * @param {Object} res 响应对象
 * @param {Function} reject Promise的reject函数
 */
function handleError(res, reject) {
  const errMsg = res.data?.message || `请求失败(${res.statusCode})`;
  
  // 移除toast提示，由页面自行处理显示方式
  console.error('HTTP错误:', errMsg);
  
  reject({
    code: res.statusCode,
    message: errMsg,
    data: res.data
  });
}

/**
 * 处理网络错误
 * @param {Object} err 错误对象
 * @param {Function} reject Promise的reject函数
 */
function handleNetworkError(err, reject) {
  const errMsg = '网络异常，请稍后重试';
  
  // 移除toast提示，由页面自行处理显示方式
  console.error('网络请求失败:', err);
  
  reject({
    code: -1,
    message: errMsg,
    data: err
  });
}

/**
 * 处理业务状态码
 * @param {Object} data 响应数据
 * @param {Function} resolve Promise的resolve函数
 * @param {Function} reject Promise的reject函数
 */
function handleBusinessCode(data, resolve, reject) {
  // 如果状态码是401，表示未登录或token失效
  if (data.code === 401) {
    handleUnauthorized();
    reject(data);
    return;
  }
  
  // 如果状态码是0或200，表示成功（兼容两种API设计）
  if (data.code === 0 || data.code === 200) {
    resolve(data);
  } else {
    // 其他错误码，统一处理
    const errorMsg = data.message || '请求失败';
    
    // 移除toast提示，由页面自行处理显示方式
    console.log('业务错误:', errorMsg, data.code);
    
    // 返回标准格式的错误对象
    reject({
      code: data.code,
      message: errorMsg,
      data: data.data,
      originalResponse: data
    });
  }
}

function handleUnauthorized() {
  console.log('Token失效，清除存储并跳转登录页');
  wx.removeStorageSync(config.TOKEN_KEY);
  wx.removeStorageSync(config.USER_INFO_KEY);
  
  // 延迟处理，避免多次跳转
  if (!getApp().isRedirectingToLogin) {
    getApp().isRedirectingToLogin = true;
    setTimeout(() => {
      // 确保使用redirectTo而不是navigateTo
      wx.redirectTo({
        url: router.pages.login,
        success: () => {
          console.log('跳转到登录页成功');
        },
        fail: (error) => {
          console.error('跳转到登录页失败:', error);
          // 如果失败，尝试重置状态
          wx.reLaunch({
            url: router.pages.login
          });
        },
        complete: () => {
          getApp().isRedirectingToLogin = false;
        }
      });
    }, 100);
  }
}

module.exports = {
  request,
  BASE_URL: config.BASE_URL
}; 
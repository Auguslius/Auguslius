/**
 * 错误处理工具模块
 * 提供统一的错误信息处理和显示功能
 */

/**
 * 统一处理错误信息并显示
 * @param {Object} page - 页面实例，用于setData
 * @param {Object|String} error - 错误对象或错误字符串
 * @param {Object} options - 配置选项
 * @param {Boolean} options.needRefreshCode - 是否需要刷新验证码
 * @param {Function} options.refreshCodeFunc - 刷新验证码的函数
 * @param {Boolean} options.showToast - 是否显示toast提示
 * @returns {String} 提取的错误信息
 */
function handleError(page, error, options = {}) {
  // 默认配置
  const defaultOptions = {
    needRefreshCode: false,
    refreshCodeFunc: null,
    showToast: false
  };
  
  // 合并配置
  const finalOptions = { ...defaultOptions, ...options };
  
  // 提取错误信息
  let errorMsg = '操作失败，请重试';
  
  if (error) {
    if (typeof error === 'string') {
      errorMsg = error;
    } else if (error.message) {
      errorMsg = error.message;
    } else if (error.data && error.data.message) {
      errorMsg = error.data.message;
    }
  }
  
  // 输出错误日志
  console.error('错误信息:', errorMsg, error);
  
  // 如果提供了页面实例，显示顶部错误提示
  if (page && page.setData) {
    // 设置错误信息
    page.setData({
      topErrorMsg: errorMsg,
      isLoading: false,
      showTopError: true
    });
    
    // 5秒后自动隐藏错误提示
    setTimeout(() => {
      if (page && page.setData) {
        page.setData({
          showTopError: false
        });
      }
    }, 5000);
  }
  
  // 如果需要显示toast
  if (finalOptions.showToast) {
    wx.showToast({
      title: errorMsg,
      icon: 'none',
      duration: 2000
    });
  }
  
  // 如果需要刷新验证码
  const needRefresh = finalOptions.needRefreshCode || 
                     errorMsg.includes('验证码') || 
                     (error && error.code === 400);
                     
  if (needRefresh && typeof finalOptions.refreshCodeFunc === 'function') {
    finalOptions.refreshCodeFunc();
  }
  
  return errorMsg;
}

/**
 * 隐藏顶部错误提示
 * @param {Object} page - 页面实例
 */
function hideTopError(page) {
  if (page && page.setData) {
    page.setData({
      showTopError: false
    });
  }
}

/**
 * 校验表单字段并处理错误
 * @param {Object} page - 页面实例
 * @param {Object} fields - 需要校验的字段，格式为 {字段名: 错误信息}
 * @returns {Boolean} 是否校验通过
 */
function validateFields(page, fields) {
  for (const [field, errorMsg] of Object.entries(fields)) {
    if (!field || field === '') {
      handleError(page, errorMsg);
      return false;
    }
  }
  return true;
}

module.exports = {
  handleError,
  hideTopError,
  validateFields
}; 
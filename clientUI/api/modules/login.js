const api = require('../api');

/**
 * 登录模块API
 */
const loginApi = {
  /**
   * 获取验证码
   * @returns {Promise} Promise对象
   */
  getCode: () => api.get('/login/code'),
  
  /**
   * 用户名密码登录
   * @param {Object} data - 登录参数
   * @param {string} data.username - 用户名
   * @param {string} data.password - 密码
   * @param {string} data.code - 验证码
   * @param {string} data.captchaId - 验证码ID
   * @returns {Promise} Promise对象
   */
  login: (data) => api.post('/login/password', data),
  
  /**
   * 获取当前登录用户信息
   * @returns {Promise} Promise对象
   */
  getCurrentUser: () => api.get('/login/current'),
  
  /**
   * 退出登录
   * @returns {Promise} Promise对象
   */
  logout: () => api.post('/login/logout')
};

module.exports = loginApi; 
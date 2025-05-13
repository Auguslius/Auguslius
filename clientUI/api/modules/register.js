const api = require('../api');

/**
 * 注册模块API
 */
const registerApi = {
  /**
   * 获取注册验证码
   * @returns {Promise} Promise对象
   */
  getCode: () => api.get('/login/code'),
  
  /**
   * 用户注册
   * @param {Object} data - 注册参数
   * @param {string} data.username - 用户名
   * @param {string} data.password - 密码
   * @param {string} data.confirmPassword - 确认密码
   * @param {string} data.code - 验证码
   * @param {string} data.captchaId - 验证码ID
   * @param {string} [data.phone] - 手机号
   * @param {string} [data.email] - 邮箱
   * @param {string} [data.realName] - 真实姓名
   * @returns {Promise} Promise对象
   */
  register: (data) => api.post('/login/register', data),
  

};

module.exports = registerApi; 
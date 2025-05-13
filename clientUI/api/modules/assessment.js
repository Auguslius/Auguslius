const api = require('../api');

/**
 * 评估模块API
 */
const assessmentApi = {
  /**
   * 获取评估列表
   * @param {Object} params - 查询参数
   * @returns {Promise} Promise对象
   */
  getList: (params) => api.get('/assessment/list', params),
  
  /**
   * 获取评估详情
   * @param {string} id - 评估ID
   * @returns {Promise} Promise对象
   */
  getDetail: (id) => api.get(`/assessment/${id}`),
  
  /**
   * 提交评估
   * @param {Object} data - 评估数据
   * @returns {Promise} Promise对象
   */
  submit: (data) => api.post('/assessment/submit', data)
};

module.exports = assessmentApi; 
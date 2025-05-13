const api = require('../api');

/**
 * 首页模块API
 */
const homeApi = {
  /**
   * 获取首页数据
   * @returns {Promise} Promise对象
   */
  getHomeData: () => api.get('/home/data'),
  
  /**
   * 获取轮播图数据
   * @returns {Promise} Promise对象
   */
  getBanners: () => api.get('/home/banners'),
  
  /**
   * 获取推荐内容
   * @returns {Promise} Promise对象
   */
  getRecommends: () => api.get('/home/recommends'),

  /**
   * 获取医生列表
   * @param {Object} params 查询参数
   * @returns {Promise} Promise对象
   */
  getDoctors: (params) => api.get('/user/page', params)
};

module.exports = homeApi; 
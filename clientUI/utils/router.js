/**
 * 路由管理工具
 */
const router = {
  // 页面路径定义
  pages: {
    home: '/pages/home/home',
    login: '/pages/login/login',
    profile: '/pages/profile/profile',
    appointment: '/pages/appointment/appointment',
    logs: '/pages/logs/logs'
  },
  
  /**
   * 普通页面跳转
   * @param {string} path - 页面路径
   * @param {Object} params - 页面参数
   */
  navigateTo(path, params = {}) {
    const url = this._buildUrl(path, params);
    wx.navigateTo({ url });
  },
  
  /**
   * 重定向跳转
   * @param {string} path - 页面路径
   * @param {Object} params - 页面参数
   */
  redirectTo(path, params = {}) {
    const url = this._buildUrl(path, params);
    wx.redirectTo({ url });
  },
  
  /**
   * Tab页面跳转
   * @param {string} path - 页面路径
   */
  switchTab(path) {
    wx.switchTab({ url: path });
  },
  
  /**
   * 重启到指定页面
   * @param {string} path - 页面路径
   * @param {Object} params - 页面参数
   */
  reLaunch(path, params = {}) {
    const url = this._buildUrl(path, params);
    wx.reLaunch({ url });
  },
  
  /**
   * 返回上一页
   * @param {number} delta - 返回层级
   */
  navigateBack(delta = 1) {
    wx.navigateBack({ delta });
  },
  
  /**
   * 构建URL
   * @private
   * @param {string} path - 页面路径
   * @param {Object} params - 页面参数
   * @returns {string} 带参数的URL
   */
  _buildUrl(path, params) {
    // 处理参数拼接
    const query = Object.keys(params)
      .map(key => `${key}=${encodeURIComponent(params[key])}`)
      .join('&');
    
    return query ? `${path}?${query}` : path;
  }
};

module.exports = router; 
/**
 * 工具函数入口文件
 * 统一导出所有工具函数
 */

// 导入基础工具函数
const { formatTime, formatNumber } = require('./util');

// 导入错误处理
const errorHandler = require('./errorHandler');

// 导入缓存工具
const cache = require('./cache');

// 导入路由工具
const router = require('./router');

module.exports = {
  // 基础工具函数
  formatTime,
  formatNumber,
  
  // 错误处理
  errorHandler,
  
  // 缓存工具
  cache,
  
  // 路由工具
  router
}; 
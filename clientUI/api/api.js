const { request } = require('../request/request');

/**
 * API对象封装
 * 提供常用的HTTP请求方法
 */
const api = {
  get: (url, data, showLoading) => request(url, { method: 'GET', data }, showLoading),
  post: (url, data, showLoading) => request(url, { method: 'POST', data }, showLoading),
  put: (url, data, showLoading) => request(url, { method: 'PUT', data }, showLoading),
  delete: (url, data, showLoading) => request(url, { method: 'DELETE', data }, showLoading)
};

module.exports = api; 
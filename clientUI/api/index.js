/**
 * API入口文件
 * 统一导出API模块和请求函数
 */

// 导入请求函数
const { request } = require('../request/request');

// 导入API方法
const api = require('./api');

// 直接导入各个API模块
const loginApi = require('./modules/login');
const assessmentApi = require('./modules/assessment');
const appointmentApi = require('./modules/appointment');
const homeApi = require('./modules/home');
const registerApi = require('./modules/register');
const authenticationApi = require('./modules/authentication');
const mmseApi = require('./modules/mmse');

module.exports = {
  // 导出请求函数
  request,
  
  // 导出API方法
  api,
  
  // 导出API模块
  loginApi,
  assessmentApi,
  appointmentApi,
  homeApi,
  registerApi,
  authenticationApi,
  mmseApi
}; 
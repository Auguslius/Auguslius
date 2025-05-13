/**
 * MMSE评估API模块
 */
const { request } = require('../../request/request');
const api = require('../api');
const config = require('../../config/index');

// MMSE评估API
const mmseApi = {
  // 获取MMSE题目列表
  getMMSEQuestions: (params, showLoading = true) => {
    return new Promise((resolve, reject) => {
      // 获取token
      const token = wx.getStorageSync('token');
      
      if (showLoading) {
        wx.showLoading({
          title: '加载中',
          mask: true
        });
      }
      
      wx.request({
        url: `${config.HOST_URL}/mmseQuestions/listMMSEQuestions`,
        method: 'GET',
        data: params || {},
        header: {
          'Content-Type': 'application/json',
          'Authorization': token ? `Bearer ${token}` : ''
        },
        success: (res) => {
          if (showLoading) {
            wx.hideLoading();
          }
          
          console.log('获取MMSE题目列表结果:', res.data);
          resolve(res.data);
        },
        fail: (err) => {
          if (showLoading) {
            wx.hideLoading();
          }
          console.error('获取MMSE题目列表请求失败:', err);
          reject({
            code: -1,
            message: '网络请求失败',
            data: null
          });
        }
      });
    });
  },
  
  // 获取MMSE题目分类及数量
  getMMSECategories: (params, showLoading = true) => {
    return new Promise((resolve, reject) => {
      // 获取token
      const token = wx.getStorageSync('token');
      
      if (showLoading) {
        wx.showLoading({
          title: '加载中',
          mask: true
        });
      }
      
      wx.request({
        url: `${config.HOST_URL}/mmseQuestions/listMMSEQuestionCategory`,
        method: 'GET',
        data: params || {},
        header: {
          'Content-Type': 'application/json',
          'Authorization': token ? `Bearer ${token}` : ''
        },
        success: (res) => {
          if (showLoading) {
            wx.hideLoading();
          }
          
          console.log('获取MMSE题目分类结果:', res.data);
          resolve(res.data);
        },
        fail: (err) => {
          if (showLoading) {
            wx.hideLoading();
          }
          console.error('获取MMSE题目分类请求失败:', err);
          reject({
            code: -1,
            message: '网络请求失败',
            data: null
          });
        }
      });
    });
  },
  
  /**
   * 提交MMSE评估答案
   * @param {Object} data - 答案数据，包含patientUuid和answersMap
   * @param {boolean} showLoading - 是否显示加载提示
   * @returns {Promise} Promise对象，返回提交结果
   */
  submitMMSEAnswers: (data, showLoading = true) => {
    return new Promise((resolve, reject) => {
      // 获取token
      const token = wx.getStorageSync('token');
      
      // 参数校验
      if (!data || !data.patientUuid) {
        reject({
          code: 400,
          message: '患者ID不能为空',
          data: null
        });
        return;
      }
      
      if (!data.answersMap || Object.keys(data.answersMap).length === 0) {
        reject({
          code: 400,
          message: '答案不能为空',
          data: null
        });
        return;
      }
      
      if (showLoading) {
        wx.showLoading({
          title: '提交中',
          mask: true
        });
      }
      
      wx.request({
        url: `${config.BASE_URL}/mmse-answers/submit`,
        method: 'POST',
        data: data,
        header: {
          'Content-Type': 'application/json',
          'Authorization': token ? ` ${token}` : ''
        },
        success: (res) => {
          if (showLoading) {
            wx.hideLoading();
          }
          
          if (res.statusCode === 200 && res.data && (res.data.code === 0 || res.data.code === 200)) {
            console.log('提交MMSE答案成功:', res.data);
            resolve(res.data);
          } else {
            console.error('提交MMSE答案失败:', res);
            reject({
              code: res.data?.code || res.statusCode,
              message: res.data?.message || '提交MMSE答案失败',
              data: res.data?.data || null
            });
          }
        },
        fail: (err) => {
          if (showLoading) {
            wx.hideLoading();
          }
          console.error('提交MMSE答案请求失败:', err);
          reject({
            code: -1,
            message: '网络请求失败: ' + (err.errMsg || ''),
            data: null
          });
        }
      });
    });
  },
  
  /**
   * 获取MMSE评估答案
   * @param {string} patientUuid - 患者ID
   * @param {boolean} showLoading - 是否显示加载提示
   * @returns {Promise} Promise对象，返回答案数据
   */
  getMMSEAnswers: (patientUuid, showLoading = true) => {
    return new Promise((resolve, reject) => {
      // 获取token
      const token = wx.getStorageSync('token');
      
      // 参数校验
      if (!patientUuid) {
        reject({
          code: 400,
          message: '患者ID不能为空',
          data: null
        });
        return;
      }
      
      if (showLoading) {
        wx.showLoading({
          title: '加载中',
          mask: true
        });
      }
      
      wx.request({
        url: `${config.BASE_URL}/mmse-answers/getAnswer/${patientUuid}`,
        method: 'GET',
        header: {
          'Content-Type': 'application/json',
          'Authorization': token ? ` ${token}` : ''
        },
        success: (res) => {
          if (showLoading) {
            wx.hideLoading();
          }
          
          if (res.statusCode === 200 && res.data && (res.data.code === 0 || res.data.code === 200)) {
            console.log('获取MMSE答案成功:', res.data);
            resolve(res.data);
          } else {
            console.error('获取MMSE答案失败:', res);
            reject({
              code: res.data?.code || res.statusCode,
              message: res.data?.message || '获取MMSE答案失败',
              data: res.data?.data || null
            });
          }
        },
        fail: (err) => {
          if (showLoading) {
            wx.hideLoading();
          }
          console.error('获取MMSE答案请求失败:', err);
          reject({
            code: -1,
            message: '网络请求失败: ' + (err.errMsg || ''),
            data: null
          });
        }
      });
    });
  },
  
  /**
   * 上传媒体文件（音频、图片等）
   * @param {string} filePath - 文件路径
   * @param {boolean} showLoading - 是否显示加载提示
   * @returns {Promise} Promise对象，返回上传结果
   */
  uploadMedia: (filePath, showLoading = true) => {
    return new Promise((resolve, reject) => {
      // 参数校验
      if (!filePath) {
        reject({
          code: 400,
          message: '文件路径不能为空',
          data: null
        });
        return;
      }
      
      // 获取token
      const token = wx.getStorageSync(config.TOKEN_KEY);
      
      if (showLoading) {
        wx.showLoading({
          title: '上传中',
          mask: true
        });
      }
      
      wx.uploadFile({
        url: `${config.HOST_URL}/media/upload`,
        filePath: filePath,
        name: 'file',
        header: {
          'Content-Type': 'multipart/form-data',
          'Authorization': token ? `Bearer ${token}` : ''
        },
        success: (res) => {
          if (showLoading) {
            wx.hideLoading();
          }
          
          // 解析响应结果
          let response;
          try {
            response = JSON.parse(res.data);
          } catch (e) {
            console.error('解析上传响应失败:', e);
            reject({
              code: -1,
              message: '解析响应失败',
              data: null
            });
            return;
          }
          
          if (response && (response.code === 0 || response.code === 200) && response.data) {
            console.log('媒体文件上传成功:', response);
            resolve(response);
          } else {
            console.error('媒体文件上传失败:', response);
            reject({
              code: response?.code || -1,
              message: response?.message || '上传失败',
              data: response?.data || null
            });
          }
        },
        fail: (err) => {
          if (showLoading) {
            wx.hideLoading();
          }
          console.error('媒体文件上传请求失败:', err);
          reject({
            code: -1,
            message: '网络请求失败: ' + (err.errMsg || ''),
            data: null
          });
        }
      });
    });
  }
};

// 确保直接导出模块
module.exports = mmseApi; 
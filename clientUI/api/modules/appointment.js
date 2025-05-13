const api = require('../api');
const config = require('../../config/index');

/**
 * 预约模块API
 */
const appointmentApi = {
  /**
   * 获取预约详情
   * @param {string} id - 预约ID
   * @returns {Promise} Promise对象
   */
  getDetail: (id) => api.get(`/appointment/${id}`),
  
  /**
   * 创建预约
   * @param {Object} data - 预约数据
   * @returns {Promise} Promise对象
   */
  create: (data) => api.post('/appointment/create', data),
  
  /**
   * 取消预约
   * @param {string} id - 预约ID
   * @returns {Promise} Promise对象
   */
  cancel: (id) => api.post(`/appointment/cancel/${id}`),
  
  /**
   * 获取机构种类树
   * @returns {Promise} Promise对象，返回机构分类层级列表
   */
  getCategoryTree: () => {
    // 使用localhost:8080作为基础URL
    return new Promise((resolve, reject) => {
      // 获取token
      const token = wx.getStorageSync('token');
      
      wx.request({
        url: `${config.HOST_URL}/institutionCategory/getCategoryTree`,
        method: 'GET',
        header: {
          'Content-Type': 'application/json',
          'Authorization': token ? `Bearer ${token}` : ''
        },
        success: (res) => {
          if (res.statusCode >= 200 && res.statusCode < 300) {
            console.log('获取机构分类数据成功:', res.data);
            // 直接返回后端的响应结果，不做额外处理
            resolve(res.data);
          } else if (res.statusCode === 401) {
            console.error('获取机构分类数据失败: 未授权');
            // 未授权，可能是token过期
            wx.removeStorageSync('token');
            reject({
              code: 401,
              message: '登录已过期，请重新登录',
              data: null
            });
          } else {
            console.error('获取机构分类数据失败:', res);
            reject({
              code: res.statusCode,
              message: '获取机构分类数据失败',
              data: null
            });
          }
        },
        fail: (err) => {
          console.error('获取机构分类数据请求失败:', err);
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
   * 获取医疗机构列表
   * @param {number} institutionCategoryId - 机构分类ID
   * @returns {Promise} Promise对象，返回医疗机构列表
   */
  getInstitutionList: (institutionCategoryId) => {
    return new Promise((resolve, reject) => {
      // 获取token
      const token = wx.getStorageSync('token');
      
      // 检查institutionCategoryId是否有效
      if (!institutionCategoryId && institutionCategoryId !== 0) {
        reject({
          code: 400,
          message: '机构分类ID不能为空',
          data: null
        });
        return;
      }
      
      // 确保将institutionCategoryId转为整数类型，后端需要Long类型
      const categoryId = parseInt(institutionCategoryId);
      
      console.log('请求医疗机构列表，参数:', {
        categoryId,
        originalId: institutionCategoryId,
        token: token ? '已设置' : '未设置'
      });
      
      wx.request({
        url: `${config.BASE_URL}/institution-doctor/listInstitution/${categoryId}`,
        method: 'GET',
        header: {
          'Content-Type': 'application/json',
          'Authorization': token ? `${token}` : ''
        },
        success: (res) => {
          if (res.statusCode >= 200 && res.statusCode < 300) {
            console.log('获取医疗机构列表成功:', res.data);
            resolve(res.data);
          } else if (res.statusCode === 401) {
            wx.removeStorageSync('token');
            reject({
              code: 401,
              message: '登录已过期，请重新登录',
              data: null
            });
          } else {
            console.error('获取医疗机构列表失败:', res);
            reject({
              code: res.statusCode,
              message: '获取医疗机构列表失败: ' + (res.data ? res.data.message : ''),
              data: null
            });
          }
        },
        fail: (err) => {
          console.error('获取医疗机构列表请求失败:', err);
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
   * 获取医疗机构下的医生列表
   * @param {string} institution - 机构名称
   * @returns {Promise} Promise对象，返回医生列表
   */
  getDoctorList: (institution) => {
    return new Promise((resolve, reject) => {
      // 获取token
      const token = wx.getStorageSync('token');
      
      wx.request({
        url: `${config.BASE_URL}/institution-doctor/listDoctor`,
        method: 'GET',
        data: { institution },
        header: {
          'Content-Type': 'application/json',
          'Authorization': token ? `Bearer ${token}` : ''
        },
        success: (res) => {
          if (res.statusCode >= 200 && res.statusCode < 300) {
            console.log('获取医生列表成功:', res.data);
            resolve(res.data);
          } else if (res.statusCode === 401) {
            console.error('获取医生列表失败: 未授权');
            // 未授权，可能是token过期
            wx.removeStorageSync('token');
            reject({
              code: 401,
              message: '登录已过期，请重新登录',
              data: null
            });
          } else {
            console.error('获取医生列表失败:', res);
            reject({
              code: res.statusCode,
              message: '获取医生列表失败',
              data: null
            });
          }
        },
        fail: (err) => {
          console.error('获取医生列表请求失败:', err);
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
   * 获取医疗机构的医生列表
   * @param {string} institutionName - 医疗机构名称
   * @returns {Promise} Promise对象，返回医生列表
   */
  getDoctorsByInstitution(institutionName) {
    return new Promise((resolve, reject) => {
      // 参数校验
      if (!institutionName) {
        console.error('医疗机构名称不能为空');
        reject({
          code: 400,
          message: '医疗机构名称不能为空',
          data: null
        });
        return;
      }
      
      const token = wx.getStorageSync('token');
      
      // 发起请求
      wx.request({
        url: `${config.BASE_URL}/institution-doctor/listDoctor`,
        method: 'GET',
        data: {
          institution: institutionName
        },
        header: {
          'Content-Type': 'application/json',
          'Authorization': token ? `${token}` : ''
        },
        success: (res) => {
          if (res.statusCode >= 200 && res.statusCode < 300) {
            console.log('获取医疗机构医生列表成功:', res.data);
            resolve(res.data);
          } else if (res.statusCode === 401) {
            console.error('获取医疗机构医生列表失败: 未授权');
            // 未授权，可能是token过期
            wx.removeStorageSync('token');
            reject({
              code: 401,
              message: '登录已过期，请重新登录',
              data: null
            });
          } else {
            console.error('获取医疗机构医生列表失败:', res);
            reject({
              code: res.statusCode,
              message: '获取医疗机构医生列表失败: ' + (res.data ? res.data.message : ''),
              data: null
            });
          }
        },
        fail: (err) => {
          console.error('获取医疗机构医生列表请求失败:', err);
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
   * 绑定医生
   * @param {object} bindDoctorDto - 绑定医生的请求体数据
   * @returns {Promise} Promise对象，返回绑定结果
   */
  bindDoctor(bindDoctorDto) {
    return new Promise((resolve, reject) => {
      // 参数校验
      if (!bindDoctorDto || !bindDoctorDto.doctorNumber) {
        console.error('医生工号不能为空');
        reject({
          code: 400,
          message: '医生工号不能为空',
          data: null
        });
        return;
      }
      
      const token = wx.getStorageSync('token');
      
      // 发起请求
      wx.request({
        url: `${config.BASE_URL}/institution-doctor/bindDoctorByDoctorNumber`,
        method: 'PATCH',
        data: bindDoctorDto,
        header: {
          'Content-Type': 'application/json',
          'Authorization': token ? `${token}` : ''
        },
        success: (res) => {
          if (res.statusCode >= 200 && res.statusCode < 300) {
            console.log('绑定医生成功:', res.data);
            resolve(res.data);
          } else if (res.statusCode === 401) {
            console.error('绑定医生失败: 未授权');
            // 未授权，可能是token过期
            wx.removeStorageSync('token');
            reject({
              code: 401,
              message: '登录已过期，请重新登录',
              data: null
            });
          } else {
            console.error('绑定医生失败:', res);
            // 直接传递后端返回的错误信息
            reject({
              code: res.data?.code || res.statusCode,
              message: res.data?.message || '绑定医生失败',
              data: res.data?.data || null
            });
          }
        },
        fail: (err) => {
          console.error('绑定医生请求失败:', err);
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
   * 获取我绑定的医生列表
   * @param {number} doctorNumber - 医生工号
   * @returns {Promise} Promise对象，返回医生列表
   */
  getMyDoctors(doctorNumber) {
    return new Promise((resolve, reject) => {
      // 参数校验
      if (!doctorNumber) {
        console.error('医生工号不能为空');
        reject({
          code: 400,
          message: '医生工号不能为空',
          data: null
        });
        return;
      }
      
      const token = wx.getStorageSync('token');
      
      if (!token) {
        reject({
          code: 401,
          message: '未登录，请先登录',
          data: null
        });
        return;
      }
      
      // 转换为整数并确保是数字类型
      const docNumber = parseInt(doctorNumber);
      
      // 发起请求 - 将doctorNumber作为路径参数而不是查询参数
      wx.request({
        url: `${config.BASE_URL}/institution-doctor/getDoctorMsg/${docNumber}`,
        method: 'GET',
        header: {
          'Content-Type': 'application/json',
          'Authorization': token ? `${token}` : ''
        },
        success: (res) => {
          if (res.statusCode >= 200 && res.statusCode < 300) {
            console.log('获取绑定医生列表成功:', res.data);
            resolve(res.data);
          } else if (res.statusCode === 401) {
            console.error('获取绑定医生列表失败: 未授权');
            // 未授权，可能是token过期
            wx.removeStorageSync('token');
            reject({
              code: 401,
              message: '登录已过期，请重新登录',
              data: null
            });
          } else {
            console.error('获取绑定医生列表失败:', res);
            // 直接传递后端返回的错误信息
            reject({
              code: res.data?.code || res.statusCode,
              message: res.data?.message || '获取绑定医生列表失败',
              data: res.data?.data || null
            });
          }
        },
        fail: (err) => {
          console.error('获取绑定医生列表请求失败:', err);
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
   * 解绑医生
   * @param {number} doctorNumber - 医生工号
   * @returns {Promise} Promise对象，返回解绑结果
   */
  unbindDoctor(doctorNumber) {
    return new Promise((resolve, reject) => {
      // 参数校验
      if (!doctorNumber) {
        console.error('医生工号不能为空');
        reject({
          code: 400,
          message: '医生工号不能为空',
          data: null
        });
        return;
      }
      
      const token = wx.getStorageSync('token');
      const app = getApp();
      const userInfo = app.globalData.userInfo || {};
      const uuid = userInfo.uuid;
      
      if (!uuid) {
        console.error('用户UUID不存在');
        reject({
          code: 400,
          message: '用户信息不完整，无法解绑医生',
          data: null
        });
        return;
      }
      
      // 发起请求
      wx.request({
        url: `${config.BASE_URL}/institution-doctor/unbindDoctorByDoctorNumber`,
        method: 'PATCH',
        data: {
          doctorNumber: parseInt(doctorNumber),
          uuid: uuid
        },
        header: {
          'Content-Type': 'application/json',
          'Authorization': token ? `${token}` : ''
        },
        success: (res) => {
          if (res.statusCode >= 200 && res.statusCode < 300) {
            console.log('解绑医生成功:', res.data);
            resolve(res.data);
          } else if (res.statusCode === 401) {
            console.error('解绑医生失败: 未授权');
            // 未授权，可能是token过期
            wx.removeStorageSync('token');
            reject({
              code: 401,
              message: '登录已过期，请重新登录',
              data: null
            });
          } else {
            console.error('解绑医生失败:', res);
            // 直接传递后端返回的错误信息
            reject({
              code: res.data?.code || res.statusCode,
              message: res.data?.message || '解绑医生失败',
              data: res.data?.data || null
            });
          }
        },
        fail: (err) => {
          console.error('解绑医生请求失败:', err);
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
   * 获取医生详细信息
   * @param {number} doctorNumber - 医生工号
   * @returns {Promise} Promise对象，返回医生详情
   */
  getDoctorDetail(doctorNumber) {
    return new Promise((resolve, reject) => {
      // 参数校验
      if (!doctorNumber) {
        console.error('医生工号不能为空');
        reject({
          code: 400,
          message: '医生工号不能为空',
          data: null
        });
        return;
      }
      
      const token = wx.getStorageSync('token');
      
      // 转换为整数并确保是数字类型
      const docNumber = parseInt(doctorNumber);
      
      // 发起请求
      wx.request({
        url: `${config.BASE_URL}/institution-doctor/getDoctorMsg/${docNumber}`,
        method: 'GET',
        header: {
          'Content-Type': 'application/json',
          'Authorization': token ? `${token}` : ''
        },
        success: (res) => {
          if (res.statusCode >= 200 && res.statusCode < 300) {
            console.log('获取医生详情成功:', res.data);
            resolve(res.data);
          } else if (res.statusCode === 401) {
            console.error('获取医生详情失败: 未授权');
            // 未授权，可能是token过期
            wx.removeStorageSync('token');
            reject({
              code: 401,
              message: '登录已过期，请重新登录',
              data: null
            });
          } else {
            console.error('获取医生详情失败:', res);
            // 直接传递后端返回的错误信息
            reject({
              code: res.data?.code || res.statusCode,
              message: res.data?.message || '获取医生详情失败',
              data: res.data?.data || null
            });
          }
        },
        fail: (err) => {
          console.error('获取医生详情请求失败:', err);
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

module.exports = appointmentApi; 
const API = require('../api');

/**
 * 认证模块API
 */
const authenticationApi = {
  /**
   * 患者认证接口
   * @param {Object} params - 患者认证DTO参数
   * @param {String} params.uuid - 用户唯一标识
   * @param {String} params.name - 真实姓名
   * @param {String} params.phone - 联系方式
   * @param {String} params.email - 电子邮箱
   * @param {String} params.idCard - 身份证号
   * @param {Date|String} params.birthDate - 生日
   * @param {String} params.gender - 性别
   * @param {String} params.address - 住址
   * @param {String} params.remark - 既往病史
   * @return {Promise}
   */
  patientAuthentication: (params) => {
    // 创建请求数据的副本
    const requestData = {
      ...params
    };
    
    // 性别格式处理，将"男"转为1，"女"转为2
    if (requestData.gender) {
      if (requestData.gender === '男') {
        requestData.gender = 1;
      } else if (requestData.gender === '女') {
        requestData.gender = 2;
      }
      console.log('处理后的性别格式:', requestData.gender);
    }
    
    // 日期格式处理，确保是YYYY-MM-DD格式
    if (requestData.birthDate) {
      // 如果是Date对象或者是字符串，统一处理
      const dateObj = new Date(requestData.birthDate);
      
      // 格式化为YYYY-MM-DD
      const year = dateObj.getFullYear();
      const month = String(dateObj.getMonth() + 1).padStart(2, '0');
      const day = String(dateObj.getDate()).padStart(2, '0');
      
      requestData.birthDate = `${year}-${month}-${day}`;
      console.log('处理后的日期格式:', requestData.birthDate);
    }
    
    return API.post('/patients/authentication', requestData);
  },
  
  /**
   * 验证用户是否已完成患者认证
   * @param {String} uuid - 用户唯一标识
   * @returns {Promise} Promise对象，返回认证信息
   */
  isAuthentication: (uuid) => {
    return API.get(`/patients/isAuthentication/${uuid}`);
  }
};

module.exports = authenticationApi; 
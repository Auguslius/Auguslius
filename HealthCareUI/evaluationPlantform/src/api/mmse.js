import request from '../utils/request';
import mmseRequest from '../utils/mmseRequest';

// 新增MMSE问题
export function addMMSEQuestion(data) {
  return request({
    url: '/mmseQuestions',
    method: 'post',
    data
  });
}

// 更新MMSE问题
export function updateMMSEQuestion(data) {
  return request({
    url: '/mmseQuestions',
    method: 'put',
    data
  });
}

// 删除MMSE问题
export function deleteMMSEQuestion(id) {
  return request({
    url: `/mmseQuestions/${id}`,
    method: 'delete'
  });
}

// 根据ID查询MMSE问题
export function getMMSEQuestionById(id) {
  return request({
    url: `/mmseQuestions/${id}`,
    method: 'get'
  });
}

// 分页查询MMSE问题
export function getMMSEQuestionsPage(params) {
  return request({
    url: '/mmseQuestions/mmseQuestionsPage',
    method: 'get',
    params
  });
}

// 获取MMSE题目大项数量统计
export function getMMSEQuestionCategory() {
  return request({
    url: '/mmseQuestions/listMMSEQuestionCategory',
    method: 'get'
  });
}

// 获取所有MMSE问题列表
export function listAllMMSEQuestions() {
  return request({
    url: '/mmseQuestions/listMMSEQuestions',
    method: 'get'
  });
}

/**
 * 获取所有患者的MMSE答案数据
 * @returns {Promise} Promise对象
 */
export function getAllMMSEAnswers() {
  return mmseRequest({
    url: '/mmse-answers/getAllAnswer',
    method: 'get'
  });
}

/**
 * 获取单个患者的MMSE答案数据
 * @param {String} patientUuid 患者UUID
 * @returns {Promise} Promise对象
 */
export function getPatientMMSEAnswer(patientUuid) {
  return mmseRequest({
    url: `/mmse-answers/getAnswer/${patientUuid}`,
    method: 'get'
  });
}

/**
 * 提交MMSE答案
 * @param {Object} data 答案数据
 * @returns {Promise} Promise对象
 */
export function submitMMSEAnswer(data) {
  return mmseRequest({
    url: '/mmse-answers/submit',
    method: 'post',
    data
  });
}

/**
 * 批改MMSE答案
 * @param {Object} data 批改数据
 * @returns {Promise} Promise对象
 */
export function scoreMMSEAnswer(data) {
  return mmseRequest({
    url: '/mmse-answers/score',
    method: 'post',
    data
  });
}

/**
 * 获取MMSE得分分布情况
 * @returns {Promise} Promise对象
 */
export function getMMSEScoreDistribution() {
  return mmseRequest({
    url: '/mmse-answers/scoreDistribution',
    method: 'get'
  });
}

/**
 * 将音频转换为文字
 * @param {String} ossUrl OSS音频文件URL
 * @returns {Promise} Promise对象
 */
export function transformAudioToText(ossUrl) {
  return request({
    url: '/audio/transform',
    method: 'get',
    params: { ossUrl }
  });
}

/**
 * 比较两段文本的相似度
 * @param {String} textA 第一段文本
 * @param {String} textB 第二段文本
 * @returns {Promise} Promise对象
 */
export function compareTextSimilarity(textA, textB) {
  return request({
    url: '/text/similarity',
    method: 'get',
    params: { textA, textB }
  });
} 
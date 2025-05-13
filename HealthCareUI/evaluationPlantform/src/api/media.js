import request from '../utils/request';

/**
 * 上传媒体文件
 * @param {FormData} data 包含file字段的FormData对象
 * @returns {Promise} Promise对象
 */
export function uploadMedia(data) {
  return request({
    url: '/media/upload',
    method: 'post',
    data,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  });
}

/**
 * 获取所有媒体文件列表
 * @returns {Promise} Promise对象
 */
export function getMediaList() {
  return request({
    url: '/media/list',
    method: 'get'
  });
}

/**
 * 根据ID获取媒体文件
 * @param {number|string} id 媒体文件ID
 * @returns {Promise} Promise对象
 */
export function getMediaById(id) {
  return request({
    url: `/media/${id}`,
    method: 'get'
  });
}

/**
 * 根据类型获取媒体文件
 * @param {string} fileType 文件类型，可选值：AUDIO（音频）、VIDEO（视频）
 * @returns {Promise} Promise对象
 */
export function getMediaByType(fileType) {
  return request({
    url: `/media/type/${fileType}`,
    method: 'get'
  });
}

/**
 * 删除媒体文件
 * @param {number|string} id 媒体文件ID
 * @returns {Promise} Promise对象
 */
export function deleteMedia(id) {
  return request({
    url: `/media/${id}`,
    method: 'delete'
  });
}

/**
 * 分页查询媒体文件
 * @param {Object} params 查询参数，包含pageNo, pageSize, fileType等
 * @returns {Promise} Promise对象
 */
export function getMediaPage(params) {
  return request({
    url: '/media/page',
    method: 'get',
    params
  });
}

/**
 * 搜索媒体文件
 * @param {Object} params 搜索参数，包含keyword等
 * @returns {Promise} Promise对象
 */
export function searchMedia(params) {
  return request({
    url: '/media/search',
    method: 'get',
    params
  });
} 
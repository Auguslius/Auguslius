import request from '@/utils/request'

// 添加医疗机构
export function addInstitution(data) {
  return request({
    url: '/institution',
    method: 'post',
    data
  })
}

// 分页查询医疗机构
export function getInstitutionPage(params) {
  return request({
    url: '/institution/page',
    method: 'get',
    params
  })
}

// 获取机构类别树
export function getCategoryTree() {
  return request({
    url: '/institutionCategory/getCategoryTree',
    method: 'get'
  })
}

// 获取机构详情
export function getInstitutionDetail(id) {
  return request({
    url: `/institution/${id}`,
    method: 'get'
  })
}

// 更新机构
export function updateInstitution(data) {
  return request({
    url: '/institution',
    method: 'patch',
    data
  })
}

// 获取所有机构
export function getAllInstitutionsService() {
  return request({
    url: '/institution/getAll',
    method: 'get'
  })
}

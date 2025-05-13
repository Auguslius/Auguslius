import request from '@/utils/request.js'

export const institutionCategoryService = (institutionCategoryData) => {
  return request.post('/institutionCategory', institutionCategoryData)
}

export const institutionCategoryLevelCountService = () => {
  return request.get('/institutionCategory/getLevelList')
}

export const institutionCategoryAllService = () => {
  return request.get('/institutionCategory')
}

export const deleteInstitutionCategoryService = (id) => {
  return request.delete(`/institutionCategory/${id}`)
}

export const updateInstitutionCategoryService = (id, institutionCategoryData) => {
  return request.put(`/institutionCategory/${id}`, institutionCategoryData)
}
import request from '@/utils/request'

// 获取病历分页数据
export function getMedicalRecordsPage(params) {
    return request({
        url: '/medical-records/page',
        method: 'get',
        params
    })
}

// 添加病历
export function addMedicalRecord(data) {
    return request({
        url: '/medical-records',
        method: 'post',
        data
    })
}

// 更新病历
export function updateMedicalRecord(recordUuid, data) {
    return request({
        url: `/medical-records/${recordUuid}`,
        method: 'put',
        data
    })
}

// 删除病历
export function deleteMedicalRecord(recordUuid) {
    return request({
        url: `/medical-records/${recordUuid}`,
        method: 'delete'
    })
}

// 文件上传
export function uploadFile(file) {
    const formData = new FormData()
    formData.append('file', file)
    return request({
        url: '/upload',
        method: 'post',
        data: formData,
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    })
} 
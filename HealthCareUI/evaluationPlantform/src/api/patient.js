import request from '@/utils/request'

export function getPatientPage(params) {
    return request({
        url: '/patients/page',
        method: 'get',
        params
    })
}

// 获取患者统计数据（包含总数和性别分布）
export function getPatientCount() {
    return request({
        url: '/patients/PatientCount',
        method: 'get'
    })
}

export function updatePatient(data) {
    return request({
        url: '/patients',
        method: 'put',
        data
    })
}

export function deletePatient(uuid) {
    return request({
        url: `/patients/${uuid}`,
        method: 'delete'
    })
}

export function addPatient(data) {
    return request({
        url: '/patients',
        method: 'post',
        data
    })
}

// 获取近五日新增患者分布
export function getNewPatientDistribution() {
    return request({
        url: '/patients/countNewPatients',
        method: 'get'
    })
}

// 获取患者性别分布
export function getPatientGenderDistribution() {
    return request({
        url: '/patients/genderDistribution',
        method: 'get'
    })
}

// 获取患者统计数据（包含性别分布和总数）
export function getPatientStats() {
    return request({
        url: '/patients/stats',
        method: 'get'
    })
}

/**
 * 获取患者年龄分布统计
 * @returns {Promise} 返回包含年龄分布数据的Promise对象
 */
export function getPatientAgeDistribution() {
    return request({
        url: '/patients/ageDistribution',
        method: 'get'
    })
}
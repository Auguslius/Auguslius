<template>
    <div class="patient-case-container">
        <el-container>
            <el-header class="page-header">
                <el-page-header>
                    <template #content>
                        <span class="text-large font-600 mr-3">患者病例</span>
                    </template>
                </el-page-header>
                <div class="header-actions">
                    <el-button type="primary" @click="openAddDialog">
                        <el-icon><Plus /></el-icon> 新增病例
                    </el-button>
                </div>
            </el-header>

            <el-main class="main-content">
                <el-card class="box-card">
                    <!-- 查询条件 -->
                    <div class="search-form">
                        <el-form :model="queryParams" inline size="small">
                            <el-form-item label="患者姓名">
                                <el-input v-model="queryParams.patientName" placeholder="请输入患者姓名" clearable />
                            </el-form-item>
                            <el-form-item label="医生姓名">
                                <el-input v-model="queryParams.doctorName" placeholder="请输入医生姓名" clearable />
                            </el-form-item>
                            <el-form-item>
                                <el-button type="primary" @click="handleQuery">
                                    <el-icon><Search /></el-icon> 查询
                                </el-button>
                                <el-button @click="resetQuery">
                                    <el-icon><RefreshLeft /></el-icon> 重置
                                </el-button>
                            </el-form-item>
                        </el-form>
                    </div>

                    <!-- 病例卡片展示区域 -->
                    <div class="card-container">
                        <el-empty v-if="recordList.length === 0" description="暂无病例记录" />
                        <el-row :gutter="12" v-else>
                            <el-col :xs="24" :sm="12" :md="6" :lg="6" v-for="record in recordList" :key="record.recordUuid">
                                <el-card class="record-card" shadow="hover">
                                    <template #header>
                                        <div class="card-header">
                                            <span class="patient-name">{{ record.patientName }}</span>
                                            <div class="card-actions">
                                                <el-button type="primary" text size="small" @click="viewRecord(record)" class="action-btn">
                                                    <el-icon><View /></el-icon>
                                                </el-button>
                                                <el-button type="primary" text size="small" @click="editRecord(record)" class="action-btn">
                                                    <el-icon><Edit /></el-icon>
                                                </el-button>
                                                <el-popconfirm
                                                    title="确定删除该病例吗？"
                                                    @confirm="deleteRecord(record.recordUuid)"
                                                >
                                                    <template #reference>
                                                        <el-button type="danger" text size="small" class="action-btn">
                                                            <el-icon><Delete /></el-icon>
                                                        </el-button>
                                                    </template>
                                                </el-popconfirm>
                                            </div>
                                        </div>
                                    </template>
                                    <div class="card-body">
                                        <div class="card-image" v-if="record.diagnosisPic">
                                            <el-image 
                                                :src="record.diagnosisPic" 
                                                fit="cover"
                                                :preview-src-list="[record.diagnosisPic]"
                                            />
                                        </div>
                                        <div class="card-image placeholder" v-else>
                                            <el-icon><Picture /></el-icon>
                                            <span>暂无图片</span>
                                        </div>
                                        <div class="card-info">
                                            <p><span class="label">医生：</span>{{ record.doctorName }}</p>
                                            <p><span class="label">诊断：</span>{{ record.diagnosis ? (record.diagnosis.length > 15 ? record.diagnosis.slice(0, 15) + '...' : record.diagnosis) : '无' }}</p>
                                            <p><span class="label">时间：</span>{{ formatDate(record.createTime) }}</p>
                                        </div>
                                    </div>
                                </el-card>
                            </el-col>
                        </el-row>
                    </div>

                    <!-- 分页 -->
                    <div class="pagination-container">
                        <el-pagination
                            v-model:current-page="queryParams.pageNo"
                            v-model:page-size="queryParams.pageSize"
                            :page-sizes="[8, 16, 24, 32]"
                            :total="total"
                            layout="total, sizes, prev, pager, next, jumper"
                            @size-change="handleSizeChange"
                            @current-change="handleCurrentChange"
                        />
                    </div>
                </el-card>
            </el-main>
        </el-container>

        <!-- 查看病例对话框 -->
        <el-dialog
            v-model="viewDialog"
            title="查看病例详情"
            width="600px"
        >
            <div class="record-detail">
                <div class="detail-item">
                    <span class="detail-label">患者姓名：</span>
                    <span class="detail-value">{{ currentRecord.patientName }}</span>
                </div>
                <div class="detail-item">
                    <span class="detail-label">医生姓名：</span>
                    <span class="detail-value">{{ currentRecord.doctorName }}</span>
                </div>
                <div class="detail-item">
                    <span class="detail-label">诊断信息：</span>
                    <span class="detail-value">{{ currentRecord.diagnosis || '无' }}</span>
                </div>
                <div class="detail-item">
                    <span class="detail-label">治疗信息：</span>
                    <span class="detail-value">{{ currentRecord.treatment || '无' }}</span>
                </div>
                <div class="detail-item" v-if="currentRecord.diagnosisPic">
                    <span class="detail-label">诊断图片：</span>
                    <div class="detail-image">
                        <el-image 
                            :src="currentRecord.diagnosisPic" 
                            fit="contain"
                            :preview-src-list="[currentRecord.diagnosisPic]"
                        />
                    </div>
                </div>
                <div class="detail-item">
                    <span class="detail-label">创建时间：</span>
                    <span class="detail-value">{{ formatDate(currentRecord.createTime) }}</span>
                </div>
            </div>
        </el-dialog>

        <!-- 新增/编辑病例对话框 -->
        <el-dialog
            v-model="formDialog"
            :title="formType === 'add' ? '新增病例' : '编辑病例'"
            width="600px"
        >
            <el-form
                ref="recordFormRef"
                :model="recordForm"
                :rules="recordFormRules"
                label-width="100px"
            >
                <el-form-item label="患者" prop="patientUuid" v-if="formType === 'add'">
                    <el-select
                        v-model="recordForm.patientUuid"
                        filterable
                        placeholder="请选择患者"
                        style="width: 100%"
                    >
                        <el-option
                            v-for="patient in patientOptions"
                            :key="patient.uuid"
                            :label="patient.name"
                            :value="patient.uuid"
                        />
                    </el-select>
                </el-form-item>
                <el-form-item label="诊断信息" prop="diagnosis">
                    <el-input
                        v-model="recordForm.diagnosis"
                        type="textarea"
                        :rows="3"
                        placeholder="请输入诊断信息"
                    />
                </el-form-item>
                <el-form-item label="治疗信息" prop="treatment">
                    <el-input
                        v-model="recordForm.treatment"
                        type="textarea"
                        :rows="3"
                        placeholder="请输入治疗信息"
                    />
                </el-form-item>
                <el-form-item label="诊断图片">
                    <el-upload
                        class="record-upload"
                        :show-file-list="false"
                        :before-upload="beforeUpload"
                        :http-request="customUpload"
                    >
                        <div v-if="recordForm.diagnosisPic" class="upload-preview">
                            <el-image :src="recordForm.diagnosisPic" fit="cover" />
                            <div class="upload-actions">
                                <el-icon class="upload-icon"><Edit /></el-icon>
                            </div>
                        </div>
                        <el-button v-else type="primary">
                            <el-icon><Upload /></el-icon> 上传图片
                        </el-button>
                        <template #tip>
                            <div class="el-upload__tip">
                                支持JPG、PNG格式，文件大小不超过2MB
                            </div>
                        </template>
                    </el-upload>
                </el-form-item>
            </el-form>
            <template #footer>
                <div class="dialog-footer">
                    <el-button @click="formDialog = false">取消</el-button>
                    <el-button type="primary" @click="submitForm">确定</el-button>
                </div>
            </template>
        </el-dialog>
    </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { 
    Plus, 
    Edit, 
    Delete, 
    View, 
    Search, 
    RefreshLeft, 
    Upload, 
    Picture 
} from '@element-plus/icons-vue'
import { 
    getMedicalRecordsPage, 
    addMedicalRecord, 
    updateMedicalRecord, 
    deleteMedicalRecord,
    uploadFile
} from '@/api/medicalRecord.js'
import { getPatientPage } from '@/api/patient.js'

// 查询参数
const queryParams = reactive({
    pageNo: 1,
    pageSize: 8,
    patientName: '',
    doctorName: ''
})

// 病例列表和分页
const recordList = ref([])
const total = ref(0)

// 患者选项
const patientOptions = ref([])

// 对话框控制
const viewDialog = ref(false)
const formDialog = ref(false)
const formType = ref('add') // 'add' 或 'edit'
const currentRecord = ref({})
const recordFormRef = ref(null)

// 表单数据
const recordForm = reactive({
    recordUuid: '',
    patientUuid: '',
    diagnosis: '',
    treatment: '',
    diagnosisPic: ''
})

// 表单验证规则
const recordFormRules = reactive({
    patientUuid: [
        { required: true, message: '请选择患者', trigger: 'change' }
    ],
    diagnosis: [
        { required: true, message: '请输入诊断信息', trigger: 'blur' }
    ],
    treatment: [
        { required: true, message: '请输入治疗信息', trigger: 'blur' }
    ]
})

// 查询病例列表
const getRecordList = async () => {
    try {
        const response = await getMedicalRecordsPage(queryParams)
        if (response.code === 0) {
            recordList.value = response.data.list
            total.value = response.data.total
        } else {
            ElMessage.error('获取病例列表失败')
        }
    } catch (error) {
        console.error('获取病例列表错误:', error)
        ElMessage.error('获取病例列表出错，请稍后重试')
    }
}

// 获取患者列表
const getPatientList = async () => {
    try {
        const response = await getPatientPage({ pageNo: 1, pageSize: 1000 })
        if (response.code === 0) {
            patientOptions.value = response.data.list
        } else {
            ElMessage.error('获取患者列表失败')
        }
    } catch (error) {
        console.error('获取患者列表错误:', error)
        ElMessage.error('获取患者列表出错，请稍后重试')
    }
}

// 查询按钮点击事件
const handleQuery = () => {
    queryParams.pageNo = 1
    getRecordList()
}

// 重置查询条件
const resetQuery = () => {
    queryParams.patientName = ''
    queryParams.doctorName = ''
    handleQuery()
}

// 每页大小变化
const handleSizeChange = (size) => {
    queryParams.pageSize = size
    getRecordList()
}

// 页码变化
const handleCurrentChange = (page) => {
    queryParams.pageNo = page
    getRecordList()
}

// 查看病例详情
const viewRecord = (record) => {
    currentRecord.value = { ...record }
    viewDialog.value = true
}

// 打开新增病例对话框
const openAddDialog = () => {
    formType.value = 'add'
    recordForm.recordUuid = ''
    recordForm.patientUuid = ''
    recordForm.diagnosis = ''
    recordForm.treatment = ''
    recordForm.diagnosisPic = ''
    formDialog.value = true
}

// 打开编辑病例对话框
const editRecord = (record) => {
    formType.value = 'edit'
    recordForm.recordUuid = record.recordUuid
    recordForm.diagnosis = record.diagnosis
    recordForm.treatment = record.treatment
    recordForm.diagnosisPic = record.diagnosisPic
    formDialog.value = true
}

// 删除病例
const deleteRecord = async (recordUuid) => {
    try {
        const response = await deleteMedicalRecord(recordUuid)
        if (response.code === 0) {
            ElMessage.success('删除病例成功')
            getRecordList()
        } else {
            ElMessage.error(response.message || '删除病例失败')
        }
    } catch (error) {
        console.error('删除病例错误:', error)
        ElMessage.error('删除病例出错，请稍后重试')
    }
}

// 上传前验证
const beforeUpload = (file) => {
    const isJPG = file.type === 'image/jpeg'
    const isPNG = file.type === 'image/png'
    const isLt2M = file.size / 1024 / 1024 < 2

    if (!isJPG && !isPNG) {
        ElMessage.error('上传图片只能是JPG或PNG格式!')
        return false
    }
    if (!isLt2M) {
        ElMessage.error('上传图片大小不能超过2MB!')
        return false
    }
    return true
}

// 自定义上传
const customUpload = async (options) => {
    try {
        const response = await uploadFile(options.file)
        if (response.code === 0) {
            recordForm.diagnosisPic = response.data
            ElMessage.success('上传成功')
        } else {
            ElMessage.error(response.message || '上传失败')
        }
    } catch (error) {
        console.error('上传错误:', error)
        ElMessage.error('上传出错，请稍后重试')
    }
}

// 提交表单
const submitForm = async () => {
    recordFormRef.value.validate(async (valid) => {
        if (valid) {
            try {
                let response
                if (formType.value === 'add') {
                    response = await addMedicalRecord(recordForm)
                } else {
                    response = await updateMedicalRecord(recordForm.recordUuid, {
                        diagnosis: recordForm.diagnosis,
                        treatment: recordForm.treatment,
                        diagnosisPic: recordForm.diagnosisPic
                    })
                }

                if (response.code === 0) {
                    ElMessage.success(formType.value === 'add' ? '添加病例成功' : '更新病例成功')
                    formDialog.value = false
                    getRecordList()
                } else {
                    ElMessage.error(response.message || (formType.value === 'add' ? '添加病例失败' : '更新病例失败'))
                }
            } catch (error) {
                console.error(formType.value === 'add' ? '添加病例错误:' : '更新病例错误:', error)
                ElMessage.error(formType.value === 'add' ? '添加病例出错，请稍后重试' : '更新病例出错，请稍后重试')
            }
        }
    })
}

// 格式化日期
const formatDate = (dateStr) => {
    if (!dateStr) return '未知'
    const date = new Date(dateStr)
    return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`
}

onMounted(() => {
    getRecordList()
    getPatientList()
})
</script>

<style lang="scss" scoped>
.patient-case-container {
    height: 100%;
    width: 100%;
    
    .page-header {
        background-color: #fff;
        border-bottom: 1px solid #dcdfe6;
        padding: 0 20px;
        display: flex;
        align-items: center;
        justify-content: space-between;
        height: 50px;
        box-shadow: 0 1px 4px rgba(0, 0, 0, 0.05);
    }

    .main-content {
        padding: 15px;
        height: calc(100% - 50px);
        overflow: auto;
    }

    .box-card {
        height: 100%;
        box-shadow: 0 1px 4px rgba(0, 0, 0, 0.05);
        border: 1px solid #ebeef5;
        border-radius: 4px;
    }

    .search-form {
        margin-bottom: 15px;
        padding: 15px;
        background-color: #f9fafc;
        border-radius: 4px;
        border: 1px solid #ebeef5;
    }

    .card-container {
        margin-bottom: 15px;
        min-height: 200px;
        padding: 5px;
    }

    .record-card {
        margin-bottom: 12px;
        transition: all 0.3s;
        height: 210px;
        border-radius: 6px;
        overflow: hidden;
        border: none;

        &:hover {
            transform: translateY(-3px);
            box-shadow: 0 6px 12px rgba(0, 0, 0, 0.1);
            
            .card-image {
                .el-image {
                    transform: scale(1.05);
                }
            }
        }

        :deep(.el-card__header) {
            padding: 0;
        }

        :deep(.el-card__body) {
            padding: 0;
        }

        .card-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 3px 8px;
            height: 24px;
            background-color: #f0f2f5;
            border-bottom: 1px solid #ebeef5;

            .patient-name {
                font-weight: bold;
                font-size: 12px;
                color: #303133;
                white-space: nowrap;
                overflow: hidden;
                text-overflow: ellipsis;
                max-width: 70%;
            }
            
            .card-actions {
                display: flex;
                align-items: center;
                
                .action-btn {
                    padding: 1px;
                    margin-left: 2px;
                    height: 18px;
                    width: 18px;
                    
                    .el-icon {
                        font-size: 11px;
                    }
                }
            }
        }

        .card-body {
            padding: 5px 8px;
            height: calc(100% - 24px);
            display: flex;
            flex-direction: column;
            
            .card-image {
                height: 95px;
                display: flex;
                justify-content: center;
                align-items: center;
                margin-bottom: 5px;
                background-color: #f5f7fa;
                border-radius: 4px;
                overflow: hidden;

                .el-image {
                    width: 100%;
                    height: 100%;
                    transition: all 0.3s;
                }

                &.placeholder {
                    flex-direction: column;
                    color: #909399;

                    .el-icon {
                        font-size: 24px;
                        margin-bottom: 4px;
                    }
                    
                    span {
                        font-size: 12px;
                    }
                }
            }

            .card-info {
                flex: 1;
                p {
                    margin: 2px 0;
                    font-size: 12px;
                    overflow: hidden;
                    text-overflow: ellipsis;
                    white-space: nowrap;
                    color: #606266;
                    line-height: 1.4;
                }

                .label {
                    font-weight: 600;
                    color: #303133;
                    display: inline-block;
                    width: 40px;
                }
            }
        }
    }

    .pagination-container {
        display: flex;
        justify-content: flex-end;
        margin-top: 20px;
    }

    .record-detail {
        .detail-item {
            margin-bottom: 15px;

            .detail-label {
                font-weight: 600;
                color: #606266;
                margin-right: 10px;
            }

            .detail-image {
                margin-top: 10px;
                
                .el-image {
                    max-width: 100%;
                    max-height: 300px;
                }
            }
        }
    }

    .record-upload {
        .upload-preview {
            position: relative;
            width: 148px;
            height: 148px;
            border: 1px dashed #d9d9d9;
            border-radius: 6px;
            overflow: hidden;

            .el-image {
                width: 100%;
                height: 100%;
            }

            .upload-actions {
                position: absolute;
                top: 0;
                left: 0;
                width: 100%;
                height: 100%;
                background-color: rgba(0, 0, 0, 0.5);
                display: flex;
                justify-content: center;
                align-items: center;
                opacity: 0;
                transition: opacity 0.3s;

                .upload-icon {
                    font-size: 24px;
                    color: #fff;
                }
            }

            &:hover .upload-actions {
                opacity: 1;
            }
        }
    }
}
</style>

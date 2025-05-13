<template>
    <div class="patient-info-container">
        <el-container>
            <el-header class="page-header">
                <span class="header-title">患者信息</span>
            </el-header>

            <el-main class="main-content">
                <el-row :gutter="20" class="content-row">
                    <el-col :span="24" class="top-section">
                        <el-row :gutter="10">
                            <el-col :span="6" :offset="0">
                                <el-card shadow="never" class="info-card">
                                    <PatientNumber :total-count="totalCount" />
                                </el-card>
                            </el-col>
                            <el-col :span="6" :offset="0">
                                <el-card shadow="never" class="info-card">
                                    <PatientIncrease />
                                </el-card>
                            </el-col>
                            <el-col :span="6" :offset="0">
                                <el-card shadow="never" class="info-card">
                                    <PatientGender :male-count="maleCount" :female-count="femaleCount" />
                                </el-card>
                            </el-col>
                            <el-col :span="6" :offset="0">
                                <el-card shadow="never" class="info-card">
                                    <PatientNewDistribution :distribution-data="newPatientDistribution" />
                                </el-card>
                            </el-col>
                        </el-row>
                    </el-col>
                    
                    <el-col :span="24" class="bottom-section">
                        <el-card class="box-card" shadow="never">
                            <template #header>
                                <div class="card-header">
                                    <span>详细信息</span>
                                    <el-form :inline="true" :model="searchForm" class="search-form">
                                        <el-form-item>
                                            <el-input v-model="searchForm.uuid" placeholder="患者编号" clearable />
                                        </el-form-item>
                                        <el-form-item>
                                            <el-input v-model="searchForm.name" placeholder="姓名" clearable />
                                        </el-form-item>
                                        <el-form-item>
                                            <el-input v-model="searchForm.idCard" placeholder="身份证号" clearable />
                                        </el-form-item>
                                        <el-form-item>
                                            <el-input v-model="searchForm.phone" placeholder="手机号" clearable />
                                        </el-form-item>
                                        <el-form-item>
                                            <el-button type="primary" @click="handleSearch">搜索</el-button>
                                            <el-button @click="resetSearch">重置</el-button>
                                            <el-button @click="handleAdd">新增患者</el-button>
                                        </el-form-item>
                                    </el-form>
                                </div>
                            </template>
                            <el-table
                                :data="tableData"
                                v-loading="loading"
                                style="width: 100%"
                                height="calc(100% - 60px)"
                                border
                                stripe
                                :header-cell-style="{
                                    background: '#f5f7fa',
                                    color: '#303133',
                                    fontWeight: 'bold'
                                }"
                            >
                                <el-table-column prop="uuid" label="患者编号" align="center"/>
                                <el-table-column prop="name" label="姓名" align="center"/>
                                <el-table-column prop="idCard" label="身份证号" align="center"/>
                                <el-table-column prop="gender" label="性别" align="center">
                                    <template #default="scope">
                                        {{ scope.row.gender === 1 ? '男' : '女' }}
                                    </template>
                                </el-table-column>
                                <el-table-column prop="age" label="年龄" align="center"/>
                                <el-table-column prop="birthDate" label="出生日期" align="center"/>
                                <el-table-column prop="phone" label="联系电话" align="center"/>
                                <el-table-column prop="address" label="住址" align="center"/>
                                <el-table-column prop="remark" label="备注" align="center"/>
                                <el-table-column prop="isDead" label="是否死亡" align="center">
                                    <template #default="scope">
                                        {{ scope.row.isDead === 1 ? '是' : '否' }}
                                    </template>
                                </el-table-column>
                                <el-table-column prop="doctorNumber" label="医生编号" align="center"/>
                                <el-table-column label="操作" width="200" align="center">
                                    <template #default="scope">
                                        <el-button type="primary" link @click="handleEdit(scope.row)">编辑</el-button>
                                        <el-button type="primary" link @click="handleViewRemark(scope.row)">查看备注</el-button>
                                        <el-button type="danger" link @click="handleDelete(scope.row)">删除</el-button>
                                    </template>
                                </el-table-column>
                            </el-table>
                        </el-card>
                    </el-col>
                </el-row>
            </el-main>
        </el-container>
    </div>
    <el-drawer
        v-model="drawer"
        :title="drawerTitle"
        direction="rtl"
        size="40%"
        :destroy-on-close="true"
        class="patient-drawer"
    >
        <el-form :model="editForm" label-width="100px" :rules="rules" ref="formRef" class="patient-form">
            <el-divider content-position="left">基本信息</el-divider>
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="患者编号" prop="uuid" v-if="!isAdd">
                        <el-input v-model="editForm.uuid" disabled />
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="姓名" prop="name">
                        <el-input v-model="editForm.name" placeholder="请输入患者姓名" />
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="身份证号" prop="idCard">
                        <el-input v-model="editForm.idCard" :disabled="!isAdd" placeholder="请输入身份证号"/>
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="性别" prop="gender">
                        <el-radio-group v-model="editForm.gender">
                            <el-radio :label="1">男</el-radio>
                            <el-radio :label="2">女</el-radio>
                        </el-radio-group>
                    </el-form-item>
                </el-col>
            </el-row>
            
            <el-divider content-position="left">详细信息</el-divider>
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="出生日期" prop="birthDate">
                        <el-date-picker
                            v-model="editForm.birthDate"
                            type="date"
                            placeholder="选择日期"
                            format="YYYY-MM-DD"
                            value-format="YYYY-MM-DD"
                            @change="calculateAge"
                            style="width: 100%"
                        />
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="年龄" prop="age">
                        <el-input v-model="editForm.age" disabled />
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="手机号" prop="phone">
                        <el-input v-model="editForm.phone" placeholder="请输入手机号" />
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="医生编号" prop="doctorNumber">
                        <el-input v-model="editForm.doctorNumber" type="number" placeholder="请输入医生编号">
                            <template #append>
                                <el-tooltip
                                    content="不填写将默认归类给管理员。可前往个人主页查找个人编号。"
                                    placement="top"
                                    effect="light"
                                >
                                    <el-icon><QuestionFilled /></el-icon>
                                </el-tooltip>
                            </template>
                        </el-input>
                    </el-form-item>
                </el-col>
            </el-row>
            
            <el-divider content-position="left">其他信息</el-divider>
            <el-form-item label="住址" prop="address">
                <el-input v-model="editForm.address" type="textarea" rows="2" placeholder="请输入住址" />
            </el-form-item>
            <el-form-item label="备注" prop="remark">
                <el-input v-model="editForm.remark" type="textarea" rows="3" placeholder="请输入备注信息" />
            </el-form-item>
            
            <el-form-item class="form-footer">
                <el-button type="primary" @click="submitForm" :loading="submitting">确认</el-button>
                <el-button @click="cancelForm">取消</el-button>
            </el-form-item>
        </el-form>
    </el-drawer>
</template>

<script setup>
import { ElMessage, ElMessageBox } from 'element-plus'
import { ref, onMounted, computed } from 'vue'
import { getPatientPage, getPatientCount, updatePatient, deletePatient, addPatient, getNewPatientDistribution } from '@/api/patient'
import PatientNumber from './components/PatientNumber.vue'
import PatientIncrease from './components/PatientIncrease.vue'
import PatientGender from './components/PatientGender.vue'
import PatientNewDistribution from './components/PatientNewDistribution.vue'
import { QuestionFilled } from '@element-plus/icons-vue'

const loading = ref(false)
const submitting = ref(false)
const tableData = ref([])
const total = ref(0)
const pageNo = ref(1)
const pageSize = ref(10)
const searchForm = ref({
    uuid: '',
    name: '',
    idCard: '',
    phone: ''
})

const maleCount = ref(0)
const femaleCount = ref(0)
const totalCount = ref(0)
const newPatientDistribution = ref([])

const drawer = ref(false)
const editForm = ref({
    uuid: '',
    name: '',
    idCard: '',
    gender: 1,
    birthDate: '',
    phone: '',
    address: '',
    remark: '',
    doctorNumber: ''
})

const rules = {
    name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
    idCard: [
        { required: true, message: '请输入身份证号', trigger: 'blur' },
    ],
    gender: [{ required: true, message: '请选择性别', trigger: 'change' }],
    age: [{ required: true, message: '请输入年龄', trigger: 'blur' }],
    birthDate: [{ required: true, message: '请选择出生日期', trigger: 'change' }],
    phone: [{ pattern: /^1[3-9]\d{9}$/, message: '手机号格式不正确', trigger: 'blur' }],
    doctorNumber: [{ required: false, message: '请输入医生编号', trigger: 'blur' }]
}

const formRef = ref(null)
const isAdd = ref(false)
const drawerTitle = computed(() => isAdd.value ? '新增患者' : '编辑患者信息')

const getList = async () => {
    loading.value = true
    try {
        const res = await getPatientPage({
            pageNo: pageNo.value,
            pageSize: pageSize.value,
            ...searchForm.value
        })
        if (res.code === 0) { 
            tableData.value = res.data.list || []
            total.value = res.data.total || 0
        } else {
            ElMessage.error(res.message || '获取数据失败')
        }
    } catch (error) {
        console.error(error)
        ElMessage.error('获取数据失败')
    } finally {
        loading.value = false
    }
}

const fetchPatientCount = async () => {
    try {
        const res = await getPatientCount()
        if (res.code === 0) {
            maleCount.value = res.data.maleCount
            femaleCount.value = res.data.femaleCount
            totalCount.value = res.data.totalCount
        } else {
            ElMessage.error(res.message || '获取统计数据失败')
        }
    } catch (error) {
        console.error(error)
        ElMessage.error('获取统计数据失败')
    }
}

const fetchNewPatientDistribution = async () => {
    try {
        const res = await getNewPatientDistribution()
        if (res.code === 0) {
            newPatientDistribution.value = res.data || []
        } else {
            ElMessage.error(res.message || '获取新增患者分布数据失败')
        }
    } catch (error) {
        console.error(error)
        ElMessage.error('获取新增患者分布数据失败')
    }
}

const handleAdd = () => {
    isAdd.value = true
    editForm.value = {
        name: '',
        idCard: '',
        gender: 1,
        age: 0,
        birthDate: '',
        phone: '',
        address: '',
        remark: '',
        doctorNumber: ''
    }
    drawer.value = true
}

const handleSearch = () => {
    pageNo.value = 1
    getList()
}

const resetSearch = () => {
    pageNo.value = 1
    pageSize.value = 10
    searchForm.value = {
        uuid: '',
        name: '',
        idCard: '',
        phone: ''
    }
    handleSearch()
}

const handleEdit = (row) => {
    isAdd.value = false
    editForm.value = { ...row }
    drawer.value = true
}

const submitForm = async () => {
    if (!formRef.value) return
    
    await formRef.value.validate(async (valid) => {
        if (valid) {
            try {
                submitting.value = true
                const api = isAdd.value ? addPatient : updatePatient
                const res = await api(editForm.value)
                if (res.code === 0) {
                    ElMessage.success(isAdd.value ? '新增成功' : '更新成功')
                    drawer.value = false
                    getList()
                    fetchPatientCount()
                } else {
                    ElMessage.error(res.message || (isAdd.value ? '新增失败' : '更新失败'))
                }
            } catch (error) {
                console.error(error)
                ElMessage.error(isAdd.value ? '新增失败' : '更新失败')
            } finally {
                submitting.value = false
            }
        }
    })
}

const cancelForm = () => {
    formRef.value?.resetFields()
    drawer.value = false
}

const handleViewRemark = (row) => {
    ElMessageBox.alert(row.remark || '暂无备注', '备注信息', {
        confirmButtonText: '确定'
    })
}

const handleDelete = (row) => {
    ElMessageBox.confirm(
        '确定要删除该患者信息吗？',
        '警告',
        {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning',
        }
    )
    .then(async () => {
        try {
            const res = await deletePatient(row.uuid)
            if (res.code === 0) {
                ElMessage.success('删除成功')
                getList()
                fetchPatientCount()
            } else {
                ElMessage.error(res.message || '删除失败')
            }
        } catch (error) {
            console.error(error)
            ElMessage.error('删除失败')
        }
    })
    .catch(() => {
        // 取消删除，不做处理
    })
}

const calculateAge = (birthDate) => {
    if (birthDate) {
        const today = new Date()
        const birth = new Date(birthDate)
        let age = today.getFullYear() - birth.getFullYear()
        const monthDiff = today.getMonth() - birth.getMonth()
        
        if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birth.getDate())) {
            age--
        }
        
        editForm.value.age = age
    } else {
        editForm.value.age = 0
    }
}

onMounted(() => {
    fetchPatientCount()
    getList()
    fetchNewPatientDistribution()
})
</script>

<style lang="scss">
@import '@/assets/css/patient.scss';

.patient-drawer {
  .el-drawer__header {
    margin-bottom: 0;
    padding: 16px 20px;
    border-bottom: 1px solid #e4e7ed;
    
    .el-drawer__title {
      font-size: 18px;
      font-weight: 600;
      color: #303133;
    }
  }
  
  .patient-form {
    padding: 0 20px;
    
    .el-divider {
      margin: 16px 0;
      
      .el-divider__text {
        font-size: 15px;
        font-weight: 500;
        color: #409eff;
      }
    }
    
    .el-form-item__label {
      font-weight: 500;
    }
    
    .form-footer {
      margin-top: 30px;
      text-align: right;
    }
  }
}

.info-card {
  height: 170px;
  margin-bottom: 20px;
  
  .el-card__body {
    height: 100%;
    padding: 15px;
  }
}
</style>
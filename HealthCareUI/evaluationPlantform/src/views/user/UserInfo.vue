<template>
  <el-container class="info-container">
    <el-header class="page-header">
      <h1><el-icon><User /></el-icon> 个人信息管理</h1>
      <div class="description">完善您的个人信息，让系统更好地为您服务</div>
    </el-header>
    <el-main class="main-content">
      <el-card class="info-card">
        <el-row :gutter="30">
          <!-- 左侧表单 -->
          <el-col :xs="24" :sm="24" :md="16" :lg="16">
            <div class="form-section">
              <h2 class="section-title">基本资料</h2>
              <el-form 
                :model="form" 
                label-width="80px" 
                label-position="left" 
                class="info-form"
                :hide-required-asterisk="true"
              >
                <el-row :gutter="20">
                  <el-col :xs="24" :sm="12">
                    <el-form-item label="用户名">
                      <template #label>
                        <el-icon><User /></el-icon>
                        用户名
                      </template>
                      <el-input v-model="form.username" placeholder="请输入用户名"></el-input>
                    </el-form-item>
                  </el-col>
                  <el-col :xs="24" :sm="12">
                    <el-form-item label="昵称">
                      <template #label>
                        <el-icon><UserFilled /></el-icon>
                        昵称
                      </template>
                      <el-input 
                        v-model="form.nickname" 
                        placeholder="请输入昵称" 
                        maxlength="20" 
                        show-word-limit>
                      </el-input>
                    </el-form-item>
                  </el-col>
                </el-row>

                <el-row :gutter="20">
                  <el-col :xs="24" :sm="12">
                    <el-form-item label="年龄">
                      <template #label>
                        <el-icon><Avatar /></el-icon>
                        年龄
                      </template>
                      <el-input-number v-model="form.age" :min="0" :max="150" placeholder="请输入年龄" style="width: 100%"></el-input-number>
                    </el-form-item>
                  </el-col>
                  <el-col :xs="24" :sm="12">
                    <el-form-item label="编号">
                      <template #label>
                        <el-icon><DocumentCopy /></el-icon>
                        编号
                      </template>
                      <el-input v-model="form.number" disabled>
                        <template #append>
                          <el-tooltip content="系统自动生成的唯一编号" placement="top">
                            <el-icon><InfoFilled /></el-icon>
                          </el-tooltip>
                        </template>
                      </el-input>
                    </el-form-item>
                  </el-col>
                </el-row>

                <el-row :gutter="20">
                  <el-col :xs="24" :sm="12">
                    <el-form-item label="邮箱">
                      <template #label>
                        <el-icon><Message /></el-icon>
                        邮箱
                      </template>
                      <el-input v-model="form.email" placeholder="请输入邮箱">
                        <template #prefix>
                          <span style="color: #909399;">@</span>
                        </template>
                      </el-input>
                    </el-form-item>
                  </el-col>
                  <el-col :xs="24" :sm="12">
                    <el-form-item label="电话">
                      <template #label>
                        <el-icon><Iphone /></el-icon>
                        电话
                      </template>
                      <el-input v-model="form.phone" placeholder="请输入电话"></el-input>
                    </el-form-item>
                  </el-col>
                </el-row>

                <el-form-item label="地址">
                  <template #label>
                    <el-icon><Location /></el-icon>
                    地址
                  </template>
                  <el-input 
                    v-model="form.address" 
                    type="textarea" 
                    placeholder="请输入地址"
                    :rows="2">
                  </el-input>
                </el-form-item>

                <h2 class="section-title">职业信息</h2>
                
                <el-row :gutter="20">
                  <el-col :xs="24" :sm="12">
                    <el-form-item label="机构">
                      <template #label>
                        <el-icon><OfficeBuilding /></el-icon>
                        机构
                      </template>
                      <el-select
                        v-model="form.institution"
                        filterable
                        placeholder="请选择机构"
                        style="width: 100%"
                      >
                        <el-option
                          v-for="item in institutionOptions"
                          :key="item.uuid"
                          :label="item.institutionName"
                          :value="item.institutionName"
                        />
                      </el-select>
                    </el-form-item>
                  </el-col>
                  <el-col :xs="24" :sm="12">
                    <el-form-item label="职位">
                      <template #label>
                        <el-icon><User /></el-icon>
                        职位
                      </template>
                      <el-select
                        v-model="form.position"
                        filterable
                        allow-create
                        default-first-option
                        :reserve-keyword="false"
                        placeholder="请选择或输入职位"
                        style="width: 100%"
                      >
                        <el-option
                          v-for="item in presetPositions"
                          :key="item"
                          :label="item"
                          :value="item"
                        />
                      </el-select>
                    </el-form-item>
                  </el-col>
                </el-row>

                <el-form-item label="科室">
                  <template #label>
                    <el-icon><School /></el-icon>
                    科室
                  </template>
                  <el-input v-model="form.room" placeholder="请输入科室"></el-input>
                </el-form-item>

                <el-form-item class="action-buttons">
                  <el-button type="primary" @click="submitForm" size="large">
                    <el-icon><Check /></el-icon> 保存信息
                  </el-button>
                  <el-button @click="resetForm" size="large">
                    <el-icon><RefreshRight /></el-icon> 重置
                  </el-button>
                </el-form-item>
              </el-form>
            </div>
          </el-col>

          <!-- 右侧头像和提示 -->
          <el-col :xs="24" :sm="24" :md="8" :lg="8">
            <div class="avatar-section">
              <h2 class="section-title">个人头像</h2>
              <div class="avatar-container">
                <div class="avatar-wrapper">
                  <el-avatar 
                    :size="150" 
                    :src="imgUrl" 
                    class="user-avatar" 
                    fit="cover" />
                  <div class="avatar-overlay">
                    <el-icon><EditPen /></el-icon>
                    <span>点击更换头像</span>
                  </div>
                </div>
                <el-upload 
                  ref="uploadRef"
                  class="avatar-uploader" 
                  :show-file-list="false"
                  :auto-upload="true"
                  action="/api/upload"
                  name="file"
                  :headers="{'Authorization':tokenStore.token}"
                  :on-success="uploadSuccess"
                  style="display:none">
                </el-upload>
                <div class="avatar-buttons">
                  <el-button @click="uploadRef.$el.querySelector('input').click()">
                    <el-icon><Upload /></el-icon> 选择图片
                  </el-button>
                  <el-button type="success" @click="changeAvatar" plain>
                    <el-icon><Check /></el-icon> 保存头像
                  </el-button>
                </div>
              </div>

              <div class="tips-section">
                <el-alert
                  title="个人信息提示"
                  type="info"
                  description="请确保您填写的信息准确无误，这将有助于系统为您提供更精准的服务。"
                  show-icon
                  :closable="false"
                />
                <el-alert
                  class="mt-3"
                  title="头像上传说明"
                  type="warning"
                  description="请上传清晰的正面照片，文件大小不超过2MB，支持JPG、PNG格式。"
                  show-icon
                  :closable="false"
                />
              </div>
            </div>
          </el-col>
        </el-row>
      </el-card>
    </el-main>
  </el-container>
  <UserForm :user="form" @close="fetchUserInfo" />
</template>

<script setup>
import { ref, onMounted, onActivated } from 'vue';
import { userInfoService, userAvatarUpdateService, userInfoUpdateService } from '@/api/user.js';
import { getAllInstitutionsService } from '@/api/institution.js';
import { ElMessage } from 'element-plus';
import { useUserInfoStore } from '@/stores/userInfo'; // 确保使用命名导入
import { useTokenStore } from '@/stores/token';
import { 
  User, 
  Iphone, 
  Message, 
  Location, 
  DocumentCopy, 
  Avatar, 
  School, 
  UserFilled,
  OfficeBuilding,
  Plus,
  Upload,
  Check,
  RefreshRight,
  EditPen,
  InfoFilled
} from '@element-plus/icons-vue'

const userInfoStore = useUserInfoStore();  
const tokenStore = useTokenStore();  
const uploadRef = ref(null);  

const form = ref({
  username: '',
  nickname: '',
  age: '',
  number: '',
  institution: '',
  position: '', // 保持为字符串类型
  room: '',
  email: '',
  address: '',
  phone: '',
  avatar: ''
});

// 机构选项列表
const institutionOptions = ref([]);

// 获取所有机构
const fetchInstitutions = async () => {
  try {
    const response = await getAllInstitutionsService();
    if (response.code === 0 && response.data) {
      // 服务器返回的是包含institutionName的对象数组
      institutionOptions.value = response.data;
      console.log('获取到的机构列表:', institutionOptions.value);
    } else {
      ElMessage.warning('获取机构列表失败');
    }
  } catch (error) {
    console.error('获取机构列表错误:', error);
    ElMessage.error('获取机构列表出错，请稍后重试');
  }
};

//用户头像地址
const imgUrl= ref(userInfoStore.info.userPic)

//图片上传成功的回调函数
const uploadSuccess = (result)=>{
    imgUrl.value = result.data;
}
// 修改获取信息的方法，移除数组转换
const fetchUserInfo = async () => {
  try {
    const response = await userInfoService();
    form.value = response.data;
  } catch (error) {
    ElMessage.error('获取用户信息失败，请稍后重试');
  }
};

// 修改提交方法，直接提交表单
const submitForm = async () => {
  try {
    await userInfoUpdateService(form.value);
    ElMessage.success('表单提交成功');
  } catch (error) {
    ElMessage.error('提交失败');
  }
};

const resetForm = () => {
  Object.keys(form.value).forEach((key) => {
    form.value[key] = '';
  });
};

const changeAvatar = async () => {
  try {
    let result = await userAvatarUpdateService(imgUrl.value);
    if (result.code === 0) {
      ElMessage.success(result.msg ? result.msg : '修改成功')
      //修改pinia中的数据
      userInfoStore.info.userPic = imgUrl.value
    }
  } catch (error) {
    ElMessage.error(result.msg ? result.msg : '头像更新失败，请稍后重试');
  }
};

const presetPositions = ['医生', '专家', '实习生', '护理员', '主治医师', '住院医师'];

onMounted(() => {
  fetchUserInfo();
  fetchInstitutions();
});

// 当组件被keep-alive缓存后重新激活时，也会重新获取数据
onActivated(() => {
  fetchInstitutions();
});
</script>

<style scoped>
.info-container {
  height: 100%;
  background-color: #f9fafc;
  padding-top: 10px;
}

.page-header {
  padding: 28px 0;
  background: transparent;
  height: auto;
  margin-bottom: 10px;
}

.page-header h1 {
  font-size: 24px;
  font-weight: 500;
  color: #303133;
  margin: 0;
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 16px;
  position: relative;
  padding-bottom: 14px;
}

.page-header h1::after {
  content: '';
  position: absolute;
  left: 0;
  bottom: 0;
  width: 40px;
  height: 3px;
  background-color: #008E65;
  border-radius: 3px;
}

.description {
  margin-top: 16px;
  color: #909399;
  font-size: 14px;
  padding-bottom: 8px;
}

.main-content {
  padding: 0 20px 20px;
}

.info-card {
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
}

.section-title {
  font-size: 18px;
  font-weight: 500;
  color: #303133;
  margin: 20px 0 15px;
  padding-bottom: 10px;
  border-bottom: 1px solid #ebeef5;
}

.info-form {
  max-width: 100%;
}

.info-form :deep(.el-form-item__label) {
  display: flex;
  align-items: center;
  gap: 5px;
}

.avatar-section {
  padding: 0 10px;
}

.avatar-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 30px;
}

.avatar-wrapper {
  position: relative;
  cursor: pointer;
  margin-bottom: 20px;
  transition: all 0.3s;
}

.avatar-wrapper:hover .avatar-overlay {
  opacity: 1;
}

.user-avatar {
  border: 3px solid #fff;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.avatar-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  opacity: 0;
  transition: opacity 0.3s;
  border-radius: 50%;
  color: white;
}

.avatar-overlay .el-icon {
  font-size: 24px;
  margin-bottom: 5px;
}

.avatar-buttons {
  display: flex;
  gap: 10px;
  margin-top: 15px;
}

.action-buttons {
  margin-top: 30px;
  display: flex;
  justify-content: center;
  gap: 15px;
}

.tips-section {
  margin-top: 30px;
}

.mt-3 {
  margin-top: 15px;
}

@media (max-width: 768px) {
  .avatar-section {
    margin-top: 20px;
  }
  
  .action-buttons {
    flex-direction: column;
    gap: 10px;
  }
  
  .action-buttons .el-button {
    width: 100%;
  }
}
</style>
<script setup>
import { ref, onMounted } from 'vue';
import { ElMessage, ElMessageBox, ElCard, ElRow, ElCol, ElAvatar, ElDescriptions, ElDescriptionsItem, ElTag } from 'element-plus';
import useUserInfoStore from '@/stores/userInfo';
import UserForm from './UserForm.vue'; // 引入表单组件
import {
    Edit,
    Delete,
    Search,
    RefreshRight,
    OfficeBuilding,
    Ticket,
    Suitcase,
    User,
    Phone,
    Message,
    LocationInformation,
    Briefcase
} from '@element-plus/icons-vue'
const userInfoStore = useUserInfoStore();
const loading = ref(false);

//用户表声明
const userList = ref([]);
//条件查询声明
const searchForm = ref({
  username: '',
  number: '',
  institution: '',
  position: ''
});
// 新增代码开始
const dialogVisible = ref(false);
const currentUser = ref({});

//分页查找声明
const currentPage = ref(1);
const pageSize = ref(5);
const total = ref(0);
//用户信息声明
const userInfo = ref({});

//条件分页查询
const handleSearch = async () => {
  loading.value = true;
  try {
    const res = await userInfoStore.fetchUserList(currentPage.value, pageSize.value, searchForm.value);
    userList.value = res.data.list;
    total.value = res.data.total;
  } catch (error) {
    ElMessage.error('搜索用户列表失败');
  } finally {
    loading.value = false;
  }
};
//当前用户查询
const fetchUserInfo = async () => {
  try {
    console.log('fetchUserInfo1');
    await userInfoStore.fetchUserInfo();
    userInfo.value = userInfoStore.getInfo();
    console.log('userInfo.value', userInfo.value);
  } catch (error) {
    ElMessage.error('获取用户信息失败');
  }
};
//分页查询
const fetchUserList = async () => {
  loading.value = true;
  try {
    console.log('开始获取用户列表...');
    const res = await userInfoStore.fetchUserList(currentPage.value, pageSize.value);
    console.log('res', res);
    userList.value = res.data.list;
    total.value = res.data.total;
  } catch (error) {
    ElMessage.error('获取用户列表失败');
  } finally {
    loading.value = false;
  }
};

const handleDelete = (row) => {
    // 弹出确认框
    ElMessageBox.confirm(
        '你确认要删除该分类信息吗?',
        '温馨提示',
        {
            confirmButtonText: '确认',
            cancelButtonText: '取消',
            type: 'warning',
        }
    )
    .then(async () => {
        try {
            console.log('handleDelete - row.id:', row.id);
            if (!row.id) {
                throw new Error('无效的 ID');
            }
            const result = await userInfoStore.deleteUser(row.id);
            console.log('delete user response:', result);
            if (result.code === 0) {
                ElMessage({
                    type: 'success',
                    message: '删除成功',
                });
                // 刷新列表
                fetchUserList();
            } else {
                throw new Error(result.message || '删除失败');
            }
        } catch (error) {
            ElMessage({
                type: 'error',
                message: error.message || '删除失败',
            });
        }
    })
    .catch(() => {
        ElMessage({
            type: 'info',
            message: '用户取消了删除',
        });
    });
};


//换页处理
const handlePageChange = (page) => {
  currentPage.value = page;
  fetchUserList();
};

const showDialog = (row) => {
  currentUser.value = { ...row }; // 赋值编辑行数据
  dialogVisible.value = true;
};


onMounted(() => {
  fetchUserInfo();
  fetchUserList();
});
</script>

<template>
  <div style="padding: 20px;">
    <el-card shadow="never" style="margin-bottom: 20px;">
      <template #header>
        <div class="card-header">
          <span>医生查询</span>
        </div>
      </template>
      <el-form :model="searchForm" label-position="top">
        <el-row :gutter="20">
          <el-col :xs="24" :sm="12" :md="6">
            <el-form-item label="用户名">
              <el-input
                v-model="searchForm.username"
                placeholder="请输入用户名"
                :prefix-icon="Search"
                clearable>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="6">
            <el-form-item label="医生编号">
              <el-input
                v-model="searchForm.number"
                placeholder="请输入医生编号"
                :prefix-icon="Ticket"
                clearable>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="6">
            <el-form-item label="机构">
              <el-input
                v-model="searchForm.institution"
                placeholder="请输入机构"
                :prefix-icon="OfficeBuilding"
                clearable>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="6">
            <el-form-item label="职位">
              <el-input
                v-model="searchForm.position"
                placeholder="请输入职位"
                :prefix-icon="Suitcase"
                clearable>
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row justify="center" style="margin-top: 10px;">
          <el-button type="primary" @click="handleSearch" :icon="Search">查询</el-button>
          <el-button @click="searchForm = {}" :icon="RefreshRight">重置</el-button>
        </el-row>
      </el-form>
    </el-card>

    <el-card shadow="never" style="margin-bottom: 20px;">
       <el-row :gutter="20" align="middle">
         <el-col :xs="24" :sm="6" :md="4" style="text-align: center;">
            <el-avatar :size="100" :src="userInfo.userPic" style="margin-bottom: 10px; border: 2px solid #ebeef5;">
               <img src="https://cube.elemecdn.com/e/fd/0fc7d20532fdaf769a25683617711png.png"/>
            </el-avatar>
            <div class="welcome-message" style="font-size: 14px; color: #606266;">
              <span>欢迎回来, </span>
              <strong style="font-size: 16px; color: var(--el-color-primary); font-weight: 600;">{{ userInfo.nickname || '用户' }}</strong>
            </div>
         </el-col>
         <el-col :xs="24" :sm="18" :md="20">
           <el-row :gutter="20">
             <el-col :xs="24" :md="12">
                <el-descriptions title="个人信息" :column="1" border>
                  <el-descriptions-item label="姓名" label-align="right" align="center" label-class-name="my-label" class-name="my-content" width="100px">
                    <el-icon><User /></el-icon> {{ userInfo.username }}
                  </el-descriptions-item>
                  <el-descriptions-item label="科室" label-align="right" align="center">
                    <el-icon><LocationInformation /></el-icon> {{ userInfo.room }}
                  </el-descriptions-item>
                  <el-descriptions-item label="职位" label-align="right" align="center">
                     <el-icon><Briefcase /></el-icon> {{ userInfo.position }}
                  </el-descriptions-item>
                </el-descriptions>
             </el-col>
             <el-col :xs="24" :md="12">
                <el-descriptions title="机构信息" :column="1" border>
                  <el-descriptions-item label="机构" label-align="right" align="center" width="100px">
                    <el-icon><OfficeBuilding /></el-icon> {{ userInfo.institution }}
                  </el-descriptions-item>
                  <el-descriptions-item label="电话" label-align="right" align="center">
                    <el-icon><Phone /></el-icon> {{ userInfo.phone }}
                  </el-descriptions-item>
                  <el-descriptions-item label="邮箱" label-align="right" align="center">
                    <el-icon><Message /></el-icon> {{ userInfo.email }}
                  </el-descriptions-item>
                </el-descriptions>
             </el-col>
           </el-row>
         </el-col>
       </el-row>
    </el-card>

    <el-card shadow="never" :body-style="{ display: 'flex', flexDirection: 'column', height: 'calc(100vh - 480px)' }">
       <div style="flex: 1; overflow: auto; margin-bottom: 20px;">
        <el-table
          :data="userList"
          v-loading="loading"
          style="width: 100%"
          stripe
          border
          :header-cell-style="{ background: '#f5f7fa', color: '#606266', textAlign: 'center' }"
          :cell-style="{ textAlign: 'center', padding: '8px 0' }"
        >
          <el-table-column prop="username" label="用户名" min-width="120"></el-table-column>
          <el-table-column prop="room" label="科室" min-width="120"></el-table-column>
          <el-table-column prop="institution" label="机构" min-width="180"></el-table-column>
          <el-table-column prop="address" label="地址" min-width="180"></el-table-column>
          <el-table-column prop="position" label="职位" min-width="120"></el-table-column>
          <el-table-column prop="email" label="邮箱" min-width="180"></el-table-column>
          <el-table-column prop="phone" label="手机" min-width="120"></el-table-column>
          <el-table-column label="操作" width="180" fixed="right">
            <template #default="scope">
              <el-button :icon="Edit" type="primary" size="small" @click="showDialog(scope.row)">编辑</el-button>
              <el-button :icon="Delete" type="danger" size="small" @click="handleDelete(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <el-pagination
        style="display: flex; justify-content: center; padding-top: 10px; flex-shrink: 0;"
        @current-change="handlePageChange"
        :current-page="currentPage"
        :page-size="pageSize"
        layout="total, prev, pager, next, jumper"
        :total="total"
        background
      />
    </el-card>

    <UserForm
      v-if="dialogVisible"
      :user="currentUser"
      @close="dialogVisible=false"
      @fetchUserList="fetchUserList" />
  </div>
</template>

<style lang="scss" scoped>
/* 移除大部分自定义样式，只保留必要的微调 */

/* 可以为 Descriptions 的 label 添加一些样式 */
:deep(.el-descriptions__label.my-label) {
  // min-width: 80px; /* 如果需要固定标签宽度 */
}

/* 可以为 Descriptions 的 content 添加一些样式 */
:deep(.el-descriptions__content.my-content) {
  //
}

/* 微调按钮间距 */
.el-button + .el-button {
  margin-left: 10px;
}

/* 响应式调整 Descriptions 布局 */
@media (max-width: 768px) {
  .el-col:has(> .el-descriptions) {
      margin-bottom: 20px; /* 在小屏幕上为 Descriptions 添加底部间距 */
  }
  .el-col:last-child:has(> .el-descriptions) {
     margin-bottom: 0;
  }
  .el-card[shadow=never] :deep(.el-card__body) {
      height: auto !important; /* 在小屏幕上取消固定高度 */
  }
  .el-table :deep(th), .el-table :deep(td) {
      padding: 6px 0 !important; /* 减小单元格内边距 */
  }
  .el-table-column[fixed=right] {
      width: 150px !important; /* 调整操作列宽度 */
  }
}

/* 如果需要，保留头像的一些样式 */
.el-avatar > img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

/* 调整表格操作按钮间距 */
.el-table .el-button {
  margin: 0 4px;
}


</style>
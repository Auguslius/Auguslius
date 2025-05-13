<script setup>
import { ElInput, ElDropdown, ElAvatar, ElBadge, ElMessage } from 'element-plus';
import { Bell, Setting, User, Search } from '@element-plus/icons-vue';
import { ref, computed } from 'vue';
import { useUserInfoStore } from '@/stores/userInfo';
import { useRouter } from 'vue-router';

const userInfoStore = useUserInfoStore();
const userInfo = ref(userInfoStore.getInfo());
const userDisplayInfo = computed(() => userInfoStore.getUserDisplayInfo);
const router = useRouter();

const handleLogout = async () => {
  try {
    const currentUserInfo = userInfoStore.getInfo();
    if (!currentUserInfo || !currentUserInfo.number) {
      ElMessage.warning('用户信息不完整，正在为您重新登录');
      router.push('/login');
      return;
    }
    await userInfoStore.logout(currentUserInfo.number);
    ElMessage.success('退出登录成功');
  } catch (error) {
    ElMessage.error(error.message || '退出失败，请重试');
    console.error('Logout error:', error);
  }
};

const goToUserInfo = () => {
  router.push('/user/info');
};
</script>

<template>
  <el-header style="height: 60px; padding: 0;">
    <div class="header-content">
      <!-- 左侧搜索栏 -->
      <el-input
        class="search-input"
        placeholder="搜索"
        :prefix-icon="Search"
      />
      
      <!-- 右侧的通知、设置和个人头像 -->
      <div class="right-content">
        <!-- 通知 -->
        <el-dropdown class="icon-button" trigger="click">
          <span class="icon-wrapper">
            <el-badge class="badge" value="5" is-dot>
              <el-icon class="icon"><Bell /></el-icon>
            </el-badge>
          </span>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item>通知 1</el-dropdown-item>
            <el-dropdown-item>通知 2</el-dropdown-item>
            <el-dropdown-item>通知 3</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>

        <!-- 设置 -->
        <el-dropdown class="icon-button" trigger="click">
          <span class="icon-wrapper">
            <el-icon class="icon"><Setting /></el-icon>
          </span>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item>设置选项 1</el-dropdown-item>
            <el-dropdown-item>设置选项 2</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>

        <!-- 个人头像 -->
        <el-dropdown 
          class="icon-button" 
          trigger="click"
          :teleported="true"
          :fallback-placements="['bottom', 'top']"
        >
          <el-avatar 
            :src="userDisplayInfo.avatar || 'https://randomuser.me/api/portraits/men/1.jpg'" 
            size="small"
            tabindex="0"
          />
          <template #dropdown>
            <el-dropdown-menu>
              <div class="user-info" role="menuitem" tabindex="-1">
                <el-avatar :src="userDisplayInfo.avatar || 'https://randomuser.me/api/portraits/men/1.jpg'" :size="50" />
                <div class="user-details">
                  <span class="username">{{ userDisplayInfo.name }}</span>
                  <span class="role">{{ userDisplayInfo.role }}</span>
                </div>
              </div>
              <div class="divider" role="separator"></div>
              <el-dropdown-item @click="goToUserInfo">
                <el-icon><User /></el-icon>修改个人信息
              </el-dropdown-item>
              <el-dropdown-item @click="goToUserInfo">
                <el-icon><Setting /></el-icon>修改头像
              </el-dropdown-item>
              <div class="divider" role="separator"></div>
              <el-dropdown-item @click="handleLogout">
                <el-icon class="logout-icon">
                  <svg viewBox="0 0 1024 1024" xmlns="http://www.w3.org/2000/svg" width="16" height="16">
                    <path d="M868 732h-70.3c-4.8 0-9.3 2.1-12.3 5.8-7 8.5-14.5 16.7-22.4 24.5a353.84 353.84 0 0 1-112.7 75.9A352.8 352.8 0 0 1 512.4 866c-47.9 0-94.3-9.4-137.9-27.8a353.84 353.84 0 0 1-112.7-75.9 353.28 353.28 0 0 1-76-112.5C167.3 606.2 158 559.9 158 512s9.4-94.2 27.8-137.8c17.8-42.1 43.4-80 76-112.5s70.5-58.1 112.7-75.9c43.6-18.4 90-27.8 137.9-27.8 47.9 0 94.3 9.3 137.9 27.8 42.2 17.8 80.1 43.4 112.7 75.9 7.9 7.9 15.3 16.1 22.4 24.5 3 3.7 7.6 5.8 12.3 5.8H868c6.3 0 10.2-7 6.7-12.3C798 160.5 663.8 81.6 511.3 82 271.7 82.6 79.6 277.1 82 516.4 84.4 751.9 276.2 942 512.4 942c152.1 0 285.7-78.8 362.3-197.7 3.4-5.3-.4-12.3-6.7-12.3zm88.9-226.3L815 393.7c-5.3-4.2-13-.4-13 6.3v76H488c-4.4 0-8 3.6-8 8v56c0 4.4 3.6 8 8 8h314v76c0 6.7 7.8 10.5 13 6.3l141.9-112a8 8 0 0 0 0-12.6z" />
                  </svg>
                </el-icon>退出登录
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>
  </el-header>
</template>

<style scoped>
.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 24px;
  background-color: #ffffff;
  height: 60px;
  border-bottom: 1px solid #e6e6e6;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
  position: relative;
  z-index: 10;
}

.search-input {
  width: 300px;
  transition: all 0.3s ease;
}

@media (max-width: 768px) {
  .search-input {
    width: 180px;
  }
}

:deep(.search-input .el-input__wrapper) {
  background-color: #f5f7fa;
  border-radius: 20px;
  transition: all 0.3s ease;
  box-shadow: 0 0 0 1px #e0e0e0 inset;
}

:deep(.search-input .el-input__wrapper:hover) {
  box-shadow: 0 0 0 1px #c0c4cc inset;
}

:deep(.search-input .el-input__wrapper.is-focus) {
  box-shadow: 0 0 0 1px #409EFF inset;
}

:deep(.search-input .el-input__inner) {
  height: 36px;
}

:deep(.search-input .el-input__inner::placeholder) {
  color: #909399;
}

.right-content {
  display: flex;
  align-items: center;
  gap: 16px;
}

.icon-button {
  cursor: pointer;
  transition: all 0.2s ease;
}

.icon-button:hover {
  opacity: 0.8;
  transform: translateY(-2px);
}

.icon-wrapper {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 36px;
  height: 36px;
  border-radius: 50%;
  background-color: #f5f7fa;
  transition: all 0.3s ease;
}

.icon-wrapper:hover {
  background-color: #e6f7ff;
}

.icon {
  font-size: 18px;
  color: #606266;
}

.badge :deep(.el-badge__content) {
  box-shadow: 0 0 0 1px #fff;
}

.el-avatar {
  border: 2px solid transparent;
  transition: all 0.3s ease;
  cursor: pointer;
}

.el-avatar:hover {
  border: 2px solid #409EFF;
  transform: scale(1.05);
}

.user-info {
  padding: 16px;
  display: flex;
  align-items: center;
  min-width: 240px;
  background-color: #f9f9f9;
  border-radius: 8px 8px 0 0;
}

.user-details {
  margin-left: 12px;
  display: flex;
  flex-direction: column;
}

.username {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

.role {
  font-size: 12px;
  color: #909399;
  margin-top: 4px;
}

.divider {
  height: 1px;
  background-color: #EBEEF5;
  margin: 8px 0;
}

:deep(.el-dropdown-menu) {
  border-radius: 8px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
  padding: 0;
  overflow: hidden;
}

:deep(.el-dropdown-menu__item) {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 12px 16px;
  font-weight: 500;
  outline: none;
  transition: all 0.2s ease;
}

:deep(.el-dropdown-menu__item:focus) {
  background-color: #F5F7FA;
}

:deep(.el-dropdown-menu__item:not(.is-disabled):hover) {
  background-color: #F5F7FA;
  color: #409EFF;
}

:deep(.el-dropdown-menu__item i) {
  font-size: 16px;
  margin-right: 4px;
}

.logout-icon {
  color: #F56C6C;
}

:deep(.el-dropdown-menu__item:not(.is-disabled):hover .logout-icon) {
  color: #f78989;
}
</style>

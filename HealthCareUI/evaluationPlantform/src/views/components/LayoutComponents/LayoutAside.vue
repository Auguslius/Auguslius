<!-- src/components/LayoutAside.vue -->
<template>
    <el-aside :width="isCollapse ? '64px' : '240px'" class="layoutAside">
      <div class="AsideLogo" :class="{ 'collapsed': isCollapse }">
        <img src="@/assets/pic/logo.png" class="logo" />
        <span class="system-name" v-if="!isCollapse">老年平台管理系统</span>
        <div class="bottom-border"></div>
      </div>
      
      <div class="menu-container">
        <el-menu 
          active-text-color="#ffffff" 
          background-color="#008E65" 
          text-color="#f0f9f6" 
          router 
          :default-active="activeMenu" 
          :collapse="isCollapse"
          :unique-opened="true"
          class="el-menu-vertical">
          <!-- 各个菜单项 -->
          <el-menu-item index="/home">
            <el-icon>
              <House />
            </el-icon>
            <template #title>首页</template>
          </el-menu-item>
    
          <!-- 患者管理改为带子菜单的一级菜单 -->
          <el-sub-menu index="patient">
            <template #title>
              <el-icon>
                <User />
              </el-icon>
              <span>患者管理</span>
            </template>

            <el-menu-item index="/patient/info">
              <el-icon>
                <UserFilled />
              </el-icon>
              <span>患者信息</span>
            </el-menu-item>

            <el-menu-item index="/patient/case">
              <el-icon>
                <Document />
              </el-icon>
              <span>病例管理</span>
            </el-menu-item>
          </el-sub-menu>

          <el-sub-menu index="mmse-management">
            <template #title>
              <el-icon>
                <Files />
              </el-icon>
              <span>MMSE管理</span>
            </template>

            <el-menu-item index="/mmse-management/scale">
              <el-icon>
                <Document />
              </el-icon>
              <span>MMSE量表</span>
            </el-menu-item>

            <el-menu-item index="/mmse-management/review">
              <el-icon>
                <DocumentChecked />
              </el-icon>
              <span>量表审查</span>
            </el-menu-item>
          </el-sub-menu>

          <el-sub-menu index="institution">
            <template #title>
              <el-icon>
                <OfficeBuilding />
              </el-icon>
              <span>机构管理</span>
            </template>

            <el-menu-item index="/institutionManager/institutionManager">
              <el-icon>
                <OfficeBuilding />
              </el-icon>
              <span>机构信息</span>
            </el-menu-item>
            <el-menu-item index="/institutionManager/institutionCategory">
              <el-icon>
                <Location />
              </el-icon>
              <span>机构种类</span>
            </el-menu-item>
          </el-sub-menu>
  
          <el-menu-item index="/userManager" v-if="userRole === 1">
            <el-icon>
              <Service />
            </el-icon>
            <template #title>医生管理</template>
          </el-menu-item>
          <!-- 子菜单 -->
          <el-sub-menu index="userCenter">
            <template #title>
              <el-icon>
                <User />
              </el-icon>
              <span>个人中心</span>
            </template>
    
            <el-menu-item index="/user/Info">
              <el-icon>
                <DocumentChecked />
              </el-icon>
              <span>基本资料</span>
            </el-menu-item>
    
            <el-menu-item index="/user/avatar">
              <el-icon>
                <PictureRounded />
              </el-icon>
              <span>更换头像</span>
            </el-menu-item>
          </el-sub-menu>
        </el-menu>
      </div>
      
      <div class="collapse-btn" @click="toggleCollapse">
        <el-icon :size="16">
          <component :is="isCollapse ? 'Expand' : 'Fold'" />
        </el-icon>
      </div>
    </el-aside>
  </template>
  
  <script setup>
  import { House, DocumentChecked, User, UserFilled, OfficeBuilding, 
    Service, PictureRounded, Files, Document, DataLine, Fold, Expand, Location } from '@element-plus/icons-vue';
  import { useUserInfoStore } from '@/stores/userInfo';
  import { computed, ref, onMounted } from 'vue';
  import { userInfoService } from '@/api/user.js';

  const userInfoStore = useUserInfoStore();
  const userRole = ref(0);
  const isCollapse = ref(false);

  // 切换菜单折叠状态
  const toggleCollapse = () => {
    isCollapse.value = !isCollapse.value;
  };

  // 获取当前活动菜单
  const activeMenu = computed(() => {
    return window.location.hash.replace('#', '');
  });

  // 获取用户信息和角色
  const fetchUserInfo = async () => {
    try {
      const response = await userInfoService();
      userRole.value = response.data.role;
    } catch (error) {
      console.error('获取用户信息失败', error);
    }
  };

  onMounted(fetchUserInfo);
  </script>
  
  <style scoped>
  .layoutAside {
    background-color: #008E65;
    display: flex;
    flex-direction: column;
    height: 100vh;
    box-shadow: 4px 0 10px rgba(0, 0, 0, 0.2);
    margin: 0;
    padding: 0;
    transition: width 0.3s ease;
    position: relative;
  }
  
  .AsideLogo {
    display: flex;
    align-items: center;
    padding: 20px;
    position: relative;
    font-family: "PingFangSC-Regular", sans-serif;
    height: 60px;
    box-sizing: border-box;
    transition: all 0.3s ease;
    background-color: #007a56;
  }
  
  .AsideLogo.collapsed {
    justify-content: center;
    padding: 20px 0;
  }
  
  .bottom-border {
    position: absolute;
    bottom: 0;
    left: 50%;
    transform: translateX(-50%);
    width: 80%;
    height: a1px;
    background-color: rgba(255, 255, 255, 0.2);
  }
  
  .logo {
    width: 28px;
    height: 28px;
    margin-right: 10px;
    transition: margin 0.3s ease;
  }
  
  .AsideLogo.collapsed .logo {
    margin-right: 0;
  }
  
  .system-name {
    font-size: 18px;
    font-weight: bold;
    color: #ffffff;
    white-space: nowrap;
    overflow: hidden;
    transition: opacity 0.3s ease;
  }
  
  .menu-container {
    flex-grow: 1;
    overflow-y: auto;
    overflow-x: hidden;
    /* 自定义滚动条样式 */
    scrollbar-width: thin;
    scrollbar-color: rgba(255, 255, 255, 0.3) transparent;
  }
  
  .menu-container::-webkit-scrollbar {
    width: 6px;
  }
  
  .menu-container::-webkit-scrollbar-thumb {
    background-color: rgba(255, 255, 255, 0.3);
    border-radius: 3px;
  }
  
  .menu-container::-webkit-scrollbar-track {
    background: transparent;
  }
  
  .el-menu-vertical {
    border-right: none !important;
    padding-top: 10px;
    margin: 0;
  }
  
  .el-menu-vertical:not(.el-menu--collapse) {
    width: 240px;
  }
  
  .el-menu-item {
    font-family: "PingFangSC-Regular", sans-serif;
    font-weight: normal;
    height: 56px;
    line-height: 56px;
    margin: 4px 0;
  }
  
  .el-menu-item .el-icon, .el-sub-menu .el-icon {
    margin-right: 12px;
    font-size: 18px;
    vertical-align: middle;
    color: #ffffff;
  }
  
  .el-menu-item:hover {
    background: rgba(255, 255, 255, 0.1);
    color: #ffffff;
    transition: all 0.3s ease;
  }
  
  .el-menu-item.is-active {
    background: rgba(255, 255, 255, 0.2);
    color: #fff;
    font-weight: 500;
    transition: all 0.3s ease;
  }
  
  .el-sub-menu.is-opened .el-menu-item:hover {
    background: rgba(255, 255, 255, 0.1);
    color: #ffffff;
    transition: all 0.3s ease;
  }
  
  .el-sub-menu.is-active .el-menu-item.is-active {
    background: rgba(255, 255, 255, 0.2);
    color: #fff;
    font-weight: 500;
    transition: all 0.3s ease;
  }
  
  .el-sub-menu__title {
    font-family: "PingFangSC-Regular", sans-serif;
    height: 56px;
    line-height: 56px;
    color: #f0f9f6;
  }
  
  .el-sub-menu__title:hover {
    background: rgba(255, 255, 255, 0.1);
    color: #ffffff;
  }
  
  /* 调整子菜单背景色 */
  :deep(.el-menu--inline) {
    background-color: #007a56;
  }
  
  /* 调整图标颜色 */
  :deep(.el-menu-item .el-icon),
  :deep(.el-sub-menu__title .el-icon) {
    color: #f0f9f6;
  }
  
  .collapse-btn {
    position: absolute;
    bottom: 20px;
    left: 50%;
    transform: translateX(-50%);
    width: 32px;
    height: 32px;
    display: flex;
    align-items: center;
    justify-content: center;
    background-color: rgba(255, 255, 255, 0.1);
    border-radius: 50%;
    cursor: pointer;
    color: #ffffff;
    transition: all 0.3s ease;
    z-index: 10;
  }
  
  .collapse-btn:hover {
    background-color: rgba(255, 255, 255, 0.2);
    color: #ffffff;
  }
  </style>

<template>
  <el-main class="main-container">
    <!-- 左侧菜单栏和头部 -->
    <div class="main-content">
      <LayoutAside />
      <div class="content-container">
        <LayoutHeader />
        <router-view />
      </div>
    </div>
  </el-main>
</template>

<script setup>
import LayoutHeader from '@/views/components/LayoutComponents/LayoutHeader.vue';
import LayoutAside from '@/views/components/LayoutComponents/LayoutAside.vue';
</script>

<style scoped>
.main-container {
  display: flex;
  flex-direction: column;
  height: 98vh; /* 确保高度为视口高度 */
  overflow: hidden; /* 防止溢出 */
}

.main-content {
  display: flex;
  flex-grow: 1;
  overflow: hidden; /* 防止溢出 */
  
}

.content-container {
  display: flex;
  flex-direction: column;
  flex-grow: 1; /* 让内容区域填充剩余空间 */


}


.main-content > LayoutAside {
  height: 100%; /* 让左侧菜单栏上下占满位置 */
  position: fixed; /* 固定位置 */
  top: 0;
  left: 0;
  bottom: 0;

}
</style>

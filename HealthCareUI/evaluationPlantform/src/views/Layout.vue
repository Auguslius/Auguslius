<template>
  <el-container class="layoutContainer">
    <!-- 主容器，包含 Header, Main, Footer -->
    <el-container direction="vertical" class="main-container">
      <LayoutMain />
    </el-container>
  </el-container>
</template>

<script setup>
import LayoutMain from '@/views/components/LayoutComponents/LayoutMain.vue';
</script>

<style scoped>
.layoutContainer {
  height: 100vh;
  display: flex;
  flex-direction: row;
  margin: 0; /* 移除外边距 */
  padding: 0; /* 移除内边距 */
}

.main-container {
  flex-grow: 1; /* 让主区域占用剩余的空间 */
  display: flex;
  flex-direction: column;
  margin: 0; /* 移除外边距 */
  padding: 0; /* 移除内边距 */
}
</style>

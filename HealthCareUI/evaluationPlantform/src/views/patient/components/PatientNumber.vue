<template>
  <div class="stat-card">
    <div class="stat-label">就诊人数</div>
    <div class="stat-value">
      <el-icon class="icon-bg"><User /></el-icon>
      <span>{{ totalCount }}</span>
    </div>
  </div>
</template>

<script setup lang="ts">
import { User } from '@element-plus/icons-vue'

defineProps({
  totalCount: {
    type: Number,
    default: 0
  }
})
</script>

<style scoped>
.stat-card {
  height: 100%;
  width: 100%;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  background: #fff;
  border-radius: 8px;

}

.stat-label {
  font-size: 20px;
  color: #666;
  align-self: flex-start;
  font-family: PingFangSC-Ultralight;
  font-style: oblique;
  font-weight: 600;
}

.stat-value {
  font-size: 60px;
  padding: 40px;
  color: #333;
  font-weight: bold;
  align-self: flex-end;
  position: relative;
}

.icon-bg {
  position: absolute;
  right: -20px;
  top: 50%;
  transform: translateY(-35%);
  font-size: 200px;
  color: #67C23A;
  opacity: 0.2;
  z-index: 1;
}

span {
  position: relative;
  z-index: 2;
}
</style>

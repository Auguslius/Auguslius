<template>
  <div class="stat-card">
    <div class="stat-label">昨日新增</div>
    <div class="stat-value">
      <el-icon class="trend-icon-bg"><ArrowUp /></el-icon>
      <span class="increase">+{{ increaseCount }}</span>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { ArrowUp } from '@element-plus/icons-vue'

const increaseCount = ref(45) // 临时数据，后期替换为API数据
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

.trend-icon-bg {
  position: absolute;
  right: -20px;
  top: 50%;
  transform: translateY(-35%);
  font-size: 200px;
  color: #67C23A;
  opacity: 0.2;
  z-index: 1;
}

.increase {
  position: relative;
  z-index: 2;
  color: #67C23A;
}

.trend-icon {
  margin-left: 8px;
  font-size: 40px;
  vertical-align: middle;
}
</style>

<template>
  <div class="stat-card">
    <div class="stat-label">患者性别分布</div>
    <div class="chart-container" ref="chartRef"></div>
    <div class="chart-footer">
      <div class="gender-stats">
        <div class="gender-stat male">
          <span class="dot"></span>
          <span class="text">男：{{ maleCount }}人</span>
        </div>
        <div class="gender-stat female">
          <span class="dot"></span>
          <span class="text">女：{{ femaleCount }}人</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch, nextTick, onBeforeUnmount } from 'vue'
import * as echarts from 'echarts'

const props = defineProps({
  maleCount: {
    type: Number,
    default: 0
  },
  femaleCount: {
    type: Number,
    default: 0
  }
})

const chartRef = ref(null)
let chart = null

const initChart = async () => {
  if (!chartRef.value) return
  
  // 确保DOM已经渲染并且有高度
  await nextTick()
  
  // 使用定时器确保容器已完全渲染
  setTimeout(() => {
    if (!chart && chartRef.value) {
      chart = echarts.init(chartRef.value)
      updateChart()
      
      // 监听窗口大小变化
      window.addEventListener('resize', handleResize)
    }
  }, 300)
}

const updateChart = () => {
  if (!chart) return
  
  // 计算男女百分比
  const total = props.maleCount + props.femaleCount
  const malePercent = total > 0 ? Math.round((props.maleCount / total) * 100) : 0
  const femalePercent = total > 0 ? Math.round((props.femaleCount / total) * 100) : 0
  
  const option = {
    color: ['#409EFF', '#FF6A84'],
    tooltip: {
      trigger: 'item',
      formatter: '{b}: {c} ({d}%)'
    },
    legend: {
      orient: 'horizontal',
      bottom: '0%',
      itemWidth: 10,
      itemHeight: 10,
      textStyle: {
        fontSize: 12,
        color: '#606266'
      },
      selectedMode: false
    },
    series: [
      {
        type: 'pie',
        radius: ['50%', '70%'],
        center: ['50%', '42%'],
        avoidLabelOverlap: false,
        itemStyle: {
          borderRadius: 6,
          borderColor: '#fff',
          borderWidth: 2,
          shadowBlur: 10,
          shadowColor: 'rgba(0, 0, 0, 0.1)'
        },
        label: {
          show: false
        },
        labelLine: {
          show: false
        },
        emphasis: {
          scaleSize: 10,
          itemStyle: {
            shadowBlur: 20,
            shadowColor: 'rgba(0, 0, 0, 0.2)'
          }
        },
        data: [
          { 
            value: props.maleCount, 
            name: '男'
          },
          { 
            value: props.femaleCount, 
            name: '女'
          }
        ]
      }
    ]
  }
  
  chart.setOption(option)
}

// 监听窗口大小变化
const handleResize = () => {
  chart && chart.resize()
}

// 监听数据变化
watch([() => props.maleCount, () => props.femaleCount], () => {
  if (chart) {
    updateChart()
  } else {
    initChart()
  }
})

onMounted(() => {
  initChart()
})

onBeforeUnmount(() => {
  window.removeEventListener('resize', handleResize)
  chart && chart.dispose()
  chart = null
})
</script>

<style scoped>
.stat-card {
  height: 100%;
  width: 100%;
  display: flex;
  flex-direction: column;
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
  margin-bottom: 10px;
}

.chart-container {
  flex: 1;
  min-height: 100px;
  height: calc(100% - 65px);
}

.chart-footer {
  display: flex;
  justify-content: center;
  margin-top: 10px;
  padding: 0 10px;
}

.gender-stats {
  display: flex;
  justify-content: space-around;
  width: 100%;
}

.gender-stat {
  display: flex;
  align-items: center;
  font-size: 14px;
}

.gender-stat .dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  margin-right: 5px;
}

.gender-stat.male .dot {
  background-color: #409EFF;
}

.gender-stat.female .dot {
  background-color: #FF6A84;
}

.gender-stat .text {
  color: #606266;
  font-weight: 500;
}

.total-count {
  font-size: 14px;
  color: #606266;
  font-weight: 500;
}
</style>

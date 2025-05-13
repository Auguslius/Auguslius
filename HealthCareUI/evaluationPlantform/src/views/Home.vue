<template>
  <div class="home">
    <el-row :gutter="20" class="row-position">
      <el-col :span="6">
        <el-card shadow="hover" class="top-card">
          <div class="card-header">
            <span class="card-title">就诊人数</span>
          </div>
          <div class="patient-count-chart">
            <div class="count-display">
              <div class="count-number">{{ patientCount }}</div>
              <div class="count-label">位患者</div>
            </div>
            <div class="count-icon">
              <el-icon class="icon-person"><User /></el-icon>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">    
        <el-card shadow="hover" class="top-card">
          <div class="card-header">
            <span class="card-title">性别分布</span>
          </div>
          <div id="genderPieChart" class="chart-container-small"></div>
          <div class="gender-ratio">
            <div class="gender-item male">
              <span class="gender-dot" style="background-color: #409EFF;"></span>
              <span class="gender-label">男</span>
              <span class="gender-count">{{ maleCount }}人</span>
              <span class="gender-percent">({{ getMalePercent }}%)</span>
            </div>
            <div class="gender-divider"></div>
            <div class="gender-item female">
              <span class="gender-dot" style="background-color: #FF6A84;"></span>
              <span class="gender-label">女</span>
              <span class="gender-count">{{ femaleCount }}人</span>
              <span class="gender-percent">({{ getFemalePercent }}%)</span>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="top-card my-patients-card">
          <div class="card-header">
            <span class="card-title">我的患者</span>
            <el-button type="primary" link size="small" class="view-more-button">查看更多</el-button>
          </div>
          <el-scrollbar class="patient-list-scrollbar">
            <div v-if="myPatients.length === 0" class="no-patients">暂无患者信息</div>
            <div v-else>
              <div v-for="patient in myPatients" :key="patient.id" class="patient-item">
                <span class="patient-name">{{ patient.name }}</span>
                <span class="patient-last-visit">上次就诊: {{ patient.lastVisit }}</span>
              </div>
            </div>
          </el-scrollbar>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="top-card">
          <div class="card-header">
            <span class="card-title">今日天气</span>
          </div>
          <div class="weather-container" v-loading="weatherLoading">
            <div class="weather-main">
              <div class="weather-info">
                <div class="weather-city">{{ weatherData.city }}</div>
                <div class="weather-date">{{ weatherData.date }}</div>
                <div class="weather-time">{{ weatherData.time }}</div>
              </div>
              <div class="weather-temp">{{ weatherData.temperature }}</div>
            </div>
            <div class="weather-detail">
              <div class="weather-condition">
                <el-icon :class="getWeatherIcon"><Sunny v-if="weatherData.icon === 'sunny'" /><Cloudy v-else-if="weatherData.icon === 'cloudy'" /><Lightning v-else /></el-icon>
                <span>{{ weatherData.weather }}</span>
              </div>
              <div class="weather-metrics">
                <div class="weather-metric">
                  <span class="metric-label">湿度:</span>
                  <span class="metric-value">{{ weatherData.humidity }}</span>
                </div>
                <div class="weather-metric">
                  <span class="metric-label">风速:</span>
                  <span class="metric-value">{{ weatherData.windSpeed }}</span>
                </div>
              </div>
            </div>
            <div class="weather-tips">{{ weatherData.tips }}</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="row-position second-row">
      <el-col :span="12">
        <el-card shadow="hover" class="middle-card">
          <div id="mmseChart" class="chart-container"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover" class="middle-card">
          <div class="card-header">
            <span class="card-title">MMSE评分分布</span>
          </div>
          <div id="scoreDistributionChart" class="chart-container" v-loading="scoreDistributionLoading"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="row-position third-row">
      <el-col :span="24">
        <el-card shadow="hover" class="bottom-card">
          <div class="card-header">
            <span class="card-title">患者年龄分布</span>
          </div>
          <div id="ageDistributionChart" class="chart-container" v-loading="ageDistributionLoading"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { onMounted, ref, nextTick, computed } from 'vue';
import * as echarts from 'echarts/core';
import { PieChart, BarChart, LineChart } from 'echarts/charts';
import { TitleComponent, TooltipComponent, LegendComponent, GridComponent } from 'echarts/components';
import { LabelLayout } from 'echarts/features';
import { CanvasRenderer } from 'echarts/renderers';
import { User, Sunny, Cloudy, Lightning } from '@element-plus/icons-vue';
import { getMMSEScoreDistribution, getMMSEQuestionCategory } from '@/api/mmse';
import { getPatientCount, getPatientAgeDistribution } from '@/api/patient';
import { getWeatherInfo } from '@/api/weather';
import useUserInfoStore from '@/stores/userInfo';
import { ElMessage } from 'element-plus';

// 注册必要的组件
echarts.use([
  TitleComponent,
  TooltipComponent,
  LegendComponent,
  GridComponent,
  PieChart,
  BarChart,
  LineChart,
  CanvasRenderer,
  LabelLayout
]);

const userInfoStore = useUserInfoStore();  
// MMSE题目类型数据
const mmseCategories = ref([]);

// MMSE评分分布数据
const scoreDistribution = ref({});
const scoreDistributionLoading = ref(false);

// 患者统计数据
const patientCount = ref(0);
const maleCount = ref(0);
const femaleCount = ref(0);

// 我的患者数据 (示例)
const myPatients = ref([
  { id: 1, name: '张三', lastVisit: '2024-07-20' },
  { id: 2, name: '李四', lastVisit: '2024-07-19' },
  { id: 3, name: '王五', lastVisit: '2024-07-18' },
  { id: 4, name: '赵六', lastVisit: '2024-07-17' },
  // 可以添加更多患者数据
]);

// 计算性别百分比
const getMalePercent = computed(() => {
  const total = maleCount.value + femaleCount.value;
  return total > 0 ? ((maleCount.value / total) * 100).toFixed(1) : 0;
});

const getFemalePercent = computed(() => {
  const total = maleCount.value + femaleCount.value;
  return total > 0 ? ((femaleCount.value / total) * 100).toFixed(1) : 0;
});

// 年龄分布数据
const ageDistribution = ref({});
const ageDistributionLoading = ref(false);

// 初始化MMSE题目类型图表
const initChart = () => {
  const chartDom = document.getElementById('mmseChart');
  if (!chartDom) return;
  
  const myChart = echarts.init(chartDom);
  
  const option = {
    title: {
      text: 'MMSE题目类型分布',
      left: 'center'
    },
    tooltip: {
      trigger: 'item',
      formatter: '{a} <br/>{b}: {c} ({d}%)'
    },
    legend: {
      orient: 'vertical',
      left: 'left',
      padding: [15, 5, 5, 15]
    },
    series: [
      {
        name: 'MMSE题目分类',
        type: 'pie',
        radius: ['40%', '70%'],
        avoidLabelOverlap: true,
        itemStyle: {
          borderRadius: 10,
          borderColor: '#fff',
          borderWidth: 2
        },
        label: {
          show: true,
          formatter: '{b}: {c}题'
        },
        emphasis: {
          label: {
            show: true,
            fontSize: '14',
            fontWeight: 'bold'
          }
        },
        labelLine: {
          show: true
        },
        data: mmseCategories.value
      }
    ],
    color: ['#4ebaee', '#26c6da', '#00a65a', '#f39c12', '#605ca8', '#ff5959']
  };
  
  option && myChart.setOption(option);
  
  // 响应窗口大小变化
  window.addEventListener('resize', () => {
    myChart.resize();
  });
};

// 初始化MMSE评分分布图表
const initScoreDistributionChart = () => {
  const chartDom = document.getElementById('scoreDistributionChart');
  if (!chartDom) return;
  
  const myChart = echarts.init(chartDom);
  
  // 准备数据并转换格式
  const originalData = scoreDistribution.value;
  
  // 定义我们想要的新标签和对应的原始标签
  const labelMapping = {
    '0-9分': '重度认知障碍(0-9分)',
    '10-20分': '中度认知障碍(10-20分)',
    '21-26分': '轻度认知障碍(21-26分)',
    '27-30分': '正常(27-30分)'
  };
  
  // 按照我们希望的顺序排序
  const orderedLabels = ['0-9分', '10-20分', '21-26分', '27-30分'];
  
  // 创建新的数据集
  const categories = orderedLabels;
  const data = orderedLabels.map(label => originalData[labelMapping[label]] || 0);
  
  // 为每个类别分配颜色
  const colors = {
    '27-30分': '#00a65a',  // 绿色
    '21-26分': '#f39c12',  // 黄色
    '10-20分': '#ff5959',  // 红色
    '0-9分': '#605ca8'     // 紫色
  };
  
  // 创建柱状图数据，包含颜色
  const seriesData = categories.map((category, index) => {
    return {
      value: data[index],
      itemStyle: {
        color: colors[category] || '#4ebaee'  // 使用配置的颜色或默认蓝色
      }
    };
  });
  
  const option = {
    title: {
      text: '',
      subtext: '数据来源: MMSE量表评估',
      left: 'center'
    },
    tooltip: {
      trigger: 'axis',
      formatter: function(params) {
        const data = params[0];
        const category = data.name;
        const count = data.value;
        
        // 添加认知状态描述
        let description = '';
        if (category === '27-30分') description = '正常';
        else if (category === '21-26分') description = '轻度认知障碍';
        else if (category === '10-20分') description = '中度认知障碍';
        else if (category === '0-9分') description = '重度认知障碍';
        
        return `${category}(${description}): ${count}人`;
      }
    },
    xAxis: {
      type: 'category',
      data: categories,
      axisLabel: {
        interval: 0,
      }
    },
    yAxis: {
      type: 'value',
      name: '患者数量',
      minInterval: 1  // 确保y轴刻度是整数
    },
    series: [
      {
        name: '患者数量',
        type: 'bar',
        data: seriesData,
        barWidth: '50%',
        label: {
          show: true,
          position: 'top',
          formatter: '{c}'
        }
      }
    ],
    grid: {
      left: '3%',
      right: '4%',
      bottom: '10%',
      top: '15%',
      containLabel: true
    }
  };
  
  option && myChart.setOption(option);
  
  // 响应窗口大小变化
  window.addEventListener('resize', () => {
    myChart.resize();
  });
};

// 获取MMSE评分分布数据
const fetchScoreDistribution = async () => {
  scoreDistributionLoading.value = true;
  try {
    const response = await getMMSEScoreDistribution();
    if (response.code === 0 || response.code === 200) {
      scoreDistribution.value = response.data || {
        '正常(27-30分)': 0,
        '轻度认知障碍(21-26分)': 0,
        '中度认知障碍(10-20分)': 0,
        '重度认知障碍(0-9分)': 0
      };
      console.log('MMSE评分分布数据:', scoreDistribution.value);
    } else {
      throw new Error(response.message || '获取数据失败');
    }
  } catch (error) {
    console.error('获取MMSE评分分布数据失败:', error);
    // 使用默认数据
    scoreDistribution.value = {
      '正常(27-30分)': 0,
      '轻度认知障碍(21-26分)': 0,
      '中度认知障碍(10-20分)': 0,
      '重度认知障碍(0-9分)': 0
    };
  } finally {
    scoreDistributionLoading.value = false;
    // 初始化评分分布图表
    initScoreDistributionChart();
  }
};

// 初始化性别分布饼图
const initGenderPieChart = () => {
  const chartDom = document.getElementById('genderPieChart');
  if (!chartDom) return;
  
  const myChart = echarts.init(chartDom);
  
  const option = {
    color: ['#409EFF', '#FF6A84'],
    tooltip: {
      trigger: 'item',
      formatter: '{b}: {c}人 ({d}%)'
    },
    series: [
      {
        name: '性别分布',
        type: 'pie',
        radius: ['35%', '60%'],
        center: ['50%', '50%'],
        avoidLabelOverlap: false,
        itemStyle: {
          borderRadius: 0,
          borderColor: '#fff',
          borderWidth: 1
        },
        label: {
          show: false
        },
        emphasis: {
          label: {
            show: true,
            fontSize: 12,
            fontWeight: 'bold'
          }
        },
        labelLine: {
          show: false
        },
        data: [
          { value: maleCount.value, name: '男' },
          { value: femaleCount.value, name: '女' }
        ]
      }
    ]
  };
  
  option && myChart.setOption(option);
  
  // 响应窗口大小变化
  window.addEventListener('resize', () => {
    myChart.resize();
  });
};

// 获取患者统计数据
const fetchPatientStats = async () => {
  try {
    // 获取患者统计数据（包含总数和性别分布）
    const response = await getPatientCount();
    if (response.code === 0 || response.code === 200) {
      const statsData = response.data || {};
      patientCount.value = statsData.totalCount || 0;
      maleCount.value = statsData.maleCount || 0;
      femaleCount.value = statsData.femaleCount || 0;
      
      // 初始化性别分布图表
      nextTick(() => {
        initGenderPieChart();
      });
    }
  } catch (error) {
    console.error('获取患者统计数据失败:', error);
    // 使用默认数据
    patientCount.value = 0;
    maleCount.value = 0;
    femaleCount.value = 0;
    
    // 即使用默认数据也初始化图表
    nextTick(() => {
      initGenderPieChart();
    });
  }
};

// 天气数据
const weatherData = ref({
  city: '',
  weather: '',
  temperature: '',
  humidity: '',
  windSpeed: '',
  date: '',
  time: '',
  tips: '',
  icon: ''
});
const weatherLoading = ref(false);

// 获取天气图标样式
const getWeatherIcon = computed(() => {
  return {
    'weather-icon': true,
    'icon-sunny': weatherData.value.icon === 'sunny',
    'icon-cloudy': weatherData.value.icon === 'cloudy',
    'icon-rainy': weatherData.value.icon === 'rainy'
  }
});

// 获取天气数据
const fetchWeatherData = async () => {
  weatherLoading.value = true;
  try {
    const response = await getWeatherInfo();
    if (response.code === 0) {
      weatherData.value = response.data;
    } else {
      throw new Error(response.message || '获取天气数据失败');
    }
  } catch (error) {
    console.error('获取天气数据失败:', error);
  } finally {
    weatherLoading.value = false;
  }
};

// 初始化年龄分布图表
const initAgeDistributionChart = () => {
  const chartDom = document.getElementById('ageDistributionChart');
  if (!chartDom) return;
  
  const myChart = echarts.init(chartDom);
  
  // 准备数据
  const ageRanges = Object.keys(ageDistribution.value);
  const counts = Object.values(ageDistribution.value);
  
  const option = {
    title: {
      text: '患者年龄分布统计',
      left: 'center',
      top: '10px',
      textStyle: {
        fontSize: 16,
        fontWeight: 'normal'
      }
    },
    tooltip: {
      trigger: 'axis',
      formatter: '{b}: {c}人'
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      top: '60px',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      data: ageRanges,
      axisLabel: {
        interval: 0,
        rotate: 30
      },
      axisLine: {
        lineStyle: {
          color: '#E4E7ED'
        }
      }
    },
    yAxis: {
      type: 'value',
      name: '患者数量',
      minInterval: 1,
      axisLine: {
        show: true,
        lineStyle: {
          color: '#E4E7ED'
        }
      },
      splitLine: {
        lineStyle: {
          color: '#F2F6FC'
        }
      }
    },
    series: [
      {
        name: '患者数量',
        type: 'line',
        data: counts,
        smooth: true,
        symbol: 'circle',
        symbolSize: 8,
        itemStyle: {
          color: '#409EFF'
        },
        lineStyle: {
          width: 3,
          color: '#409EFF'
        },
        areaStyle: {
          color: {
            type: 'linear',
            x: 0,
            y: 0,
            x2: 0,
            y2: 1,
            colorStops: [
              {
                offset: 0,
                color: 'rgba(64, 158, 255, 0.3)'
              },
              {
                offset: 1,
                color: 'rgba(64, 158, 255, 0.1)'
              }
            ]
          }
        },
        emphasis: {
          itemStyle: {
            color: '#409EFF',
            borderColor: '#fff',
            borderWidth: 2,
            shadowColor: 'rgba(0, 0, 0, 0.2)',
            shadowBlur: 10
          }
        },
        markPoint: {
          data: [
            { type: 'max', name: '最大值' },
            { type: 'min', name: '最小值' }
          ]
        }
      }
    ]
  };
  
  option && myChart.setOption(option);
  
  // 响应窗口大小变化
  window.addEventListener('resize', () => {
    myChart.resize();
  });
};

// 获取年龄分布数据
const fetchAgeDistribution = async () => {
  ageDistributionLoading.value = true;
  try {
    const response = await getPatientAgeDistribution();
    if (response.code === 0 || response.code === 200) {
      ageDistribution.value = response.data || {};
      console.log('患者年龄分布数据:', ageDistribution.value);
    } else {
      throw new Error(response.message || '获取数据失败');
    }
  } catch (error) {
    console.error('获取患者年龄分布数据失败:', error);
    // 使用默认模拟数据
    ageDistribution.value = {
      '0-10岁': 5,
      '11-20岁': 8,
      '21-30岁': 15,
      '31-40岁': 25,
      '41-50岁': 42,
      '51-60岁': 38,
      '61-70岁': 30,
      '71-80岁': 18,
      '81-90岁': 7,
      '90岁以上': 2
    };
  } finally {
    ageDistributionLoading.value = false;
    // 初始化年龄分布图表
    initAgeDistributionChart();
  }
};

// 获取MMSE题目分类数据
const fetchMMSECategories = async () => {
  try {
    const response = await getMMSEQuestionCategory();
    if (response && response.data) {
      // 将API返回的数据转换为图表需要的格式
      mmseCategories.value = response.data.map(item => {
        return {
          value: item.count,
          name: getSectionName(item.section)
        };
      });
      console.log('获取MMSE题目分类数据成功:', mmseCategories.value);
      // 初始化图表
      nextTick(() => {
        initChart();
      });
    }
  } catch (error) {
    console.error('获取MMSE题目分类数据失败:', error);
    // 使用默认数据
    mmseCategories.value = [
      { value: 10, name: '定向力' },
      { value: 5, name: '计算力' },
      { value: 1, name: '记忆力' },
      { value: 2, name: '回忆能力' },
      { value: 4, name: '语言能力' }
    ];
    // 初始化图表
    nextTick(() => {
      initChart();
    });
  }
};

// 将英文分类转换为中文显示
const getSectionName = (section) => {
  const sectionMap = {
    'Orientation': '定向力',
    'Memory': '记忆力',
    'Calculation': '计算力',
    'Recall': '回忆能力',
    'Language': '语言能力'
  };
  return sectionMap[section] || section;
};

onMounted(() => {
  // 获取用户信息
  userInfoStore.fetchUserInfo().then(() => {
    console.log('用户信息加载成功');
  }).catch(error => {
    const errorMsg = error.response?.data?.msg || error.response?.data?.message || error.message || '获取用户信息失败';
    ElMessage.error(errorMsg);
  });
  
  // 获取数据并初始化图表
  fetchMMSECategories(); // 获取MMSE题目分类数据
  fetchScoreDistribution();
  fetchPatientStats();
  fetchWeatherData(); // 获取天气数据
  fetchAgeDistribution(); // 获取患者年龄分布数据
});
</script>

<style scoped>
.home {
  padding: 15px;
  position: relative;
  height: calc(100vh - 30px); /* 减去padding */
  overflow: hidden; /* 移除滚动条 */
  background-color: #f5f7fa;
  display: flex;
  flex-direction: column;
}
.top-card {
  min-height: 150px;
  height: 150px;
  display: flex;
  flex-direction: column;
  margin-bottom: 10px;
}
.middle-card {
  min-height: 280px;
  height: 280px;
  margin-bottom: 10px;
}
.bottom-card {
  min-height: 220px;
  flex: 1; /* 允许底部卡片灵活扩展 */
}

.row-position {
  position: relative;
  margin-bottom: 10px;
  left: 0;
  right: 0;
  flex: 1; /* 允许第三行灵活扩展 */
}

.second-row {
  margin-top: 0;
}

.third-row {
  margin-top: 0;
  flex: 1; /* 允许第三行灵活扩展 */
}

/* 修改卡片通用样式 */
:deep(.el-card) {
  border: none;
  border-radius: 0;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.05);
  transition: all 0.2s ease;
  height: 100%;
  background-color: #ffffff;
}

:deep(.el-card):hover {
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.08);
}

:deep(.el-card__body) {
  padding: 12px;
  height: 100%;
  display: flex;
  flex-direction: column;
}

/* 卡片标题样式 */
.card-title {
  font-size: 16px;
  font-weight: 500;
  margin-bottom: 12px;
  color: #303133;
  position: relative;
  padding-left: 8px;
  line-height: 1;
}

.card-title::before {
  content: '';
  position: absolute;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
  width: 3px;
  height: 14px;
  background-color: #409EFF;
}

.card-header {
  display: flex;
  margin-bottom: 8px;
}

/* 图表容器样式 */
.chart-container {
  width: 100%;
  height: 230px;
  overflow: hidden;
  flex: 1;
}

/* 图表容器小尺寸样式 */
.chart-container-small {
  width: 100%;
  height: 110px;
  overflow: visible;
  margin-top: auto;
  margin-bottom: auto;
}

/* 就诊人数图表 */
.patient-count-chart {
  display: flex;
  height: 120px;
  align-items: center;
  justify-content: space-between;
  padding: 0 12px;
  margin-top: auto;
  margin-bottom: auto;
}

.count-display {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}

.count-number {
  font-size: 50px;
  font-weight: bold;
  color: #409EFF;
  line-height: 1;
}

.count-label {
  font-size: 14px;
  color: #606266;
  margin-top: 5px;
}

.count-icon {
  background-color: rgba(64, 158, 255, 0.08);
  width: 55px;
  height: 55px;
  border-radius: 0;
  display: flex;
  align-items: center;
  justify-content: center;
}

.icon-person {
  font-size: 38px;
  color: #409EFF;
}

/* 性别饼图比率展示 */
.gender-ratio {
  display: flex;
  justify-content: space-around;
  align-items: center;
  padding: 0 8px;
  margin-top: 5px;
}

.gender-item {
  display: flex;
  align-items: center;
  font-size: 12px;
}

.gender-dot {
  width: 8px;
  height: 8px;
  border-radius: 0;
  margin-right: 3px;
}

.gender-label {
  font-weight: 500;
  margin-right: 3px;
}

.gender-count {
  margin-right: 3px;
}

.gender-percent {
  color: #909399;
  font-size: 11px;
}

.gender-divider {
  width: 1px;
  height: 15px;
  background-color: #EBEEF5;
}

/* 天气展示样式 */
.weather-container {
  display: flex;
  flex-direction: column;
  height: 100%;
  padding: 0 5px;
}

.weather-main {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 5px;
}

.weather-info {
  display: flex;
  flex-direction: column;
}

.weather-city {
  font-size: 18px;
  font-weight: 500;
  margin-bottom: 2px;
}

.weather-date, .weather-time {
  font-size: 12px;
  color: #909399;
  margin-bottom: 2px;
}

.weather-temp {
  font-size: 32px;
  font-weight: bold;
  color: #409EFF;
}

.weather-detail {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 5px;
}

.weather-condition {
  display: flex;
  align-items: center;
}

.weather-icon {
  font-size: 24px;
  margin-right: 5px;
}

.icon-sunny {
  color: #F56C6C;
}

.icon-cloudy {
  color: #909399;
}

.icon-rainy {
  color: #409EFF;
}

.weather-metrics {
  display: flex;
  flex-direction: column;
}

.weather-metric {
  font-size: 12px;
  margin-bottom: 2px;
}

.metric-label {
  color: #909399;
  margin-right: 3px;
}

.metric-value {
  font-weight: 500;
}

.weather-tips {
  font-size: 12px;
  color: #606266;
  background-color: rgba(64, 158, 255, 0.08);
  padding: 5px;
  margin-top: auto;
}

/* 我的患者卡片特定样式 */
.my-patients-card .card-header {
  justify-content: space-between;
  align-items: center;
}

.view-more-button {
  padding: 0;
}

.patient-list-scrollbar {
  flex: 1; /* 让滚动区域填充剩余空间 */
  margin-top: 5px;
}

.no-patients {
  text-align: center;
  color: #909399;
  font-size: 13px;
  padding-top: 20px;
}

.patient-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 6px 0;
  font-size: 13px;
  border-bottom: 1px dashed #EBEEF5;
}

.patient-item:last-child {
  border-bottom: none;
}

.patient-name {
  color: #303133;
  flex-shrink: 0; /* 防止名字被压缩 */
  margin-right: 10px;
}

.patient-last-visit {
  color: #909399;
  white-space: nowrap; /* 防止日期换行 */
  overflow: hidden;
  text-overflow: ellipsis; /* 超出显示省略号 */
}
</style>

<template>
    <div class="patient-new-distribution">
        <div class="stat-label">近五日新增患者</div>
        <div class="chart-container" ref="chartRef"></div>
    </div>
</template>

<script setup>
import { ref, onMounted, watch, nextTick, onBeforeUnmount } from 'vue'
import * as echarts from 'echarts'

const props = defineProps({
    distributionData: {
        type: Array,
        default: () => []
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
    if (!chart || !props.distributionData.length) return
    
    // 准备数据
    const dates = props.distributionData.map(item => item.date.substring(5)).reverse() // 只显示月和日
    const counts = props.distributionData.map(item => item.count).reverse()
    
    // 图表配置
    const option = {
        tooltip: {
            trigger: 'axis',
            formatter: '{b}: {c} 位患者',
            axisPointer: {
                type: 'shadow'
            }
        },
        grid: {
            left: '3%',
            right: '4%',
            bottom: '8%',
            top: '8%',
            containLabel: true
        },
        xAxis: {
            type: 'category',
            data: dates,
            axisLine: {
                lineStyle: {
                    color: '#909399'
                }
            },
            axisLabel: {
                fontSize: 10,
                color: '#606266'
            }
        },
        yAxis: {
            type: 'value',
            minInterval: 1,
            axisLine: {
                show: false
            },
            axisLabel: {
                color: '#606266'
            },
            splitLine: {
                lineStyle: {
                    color: '#EBEEF5'
                }
            }
        },
        series: [
            {
                name: '新增患者',
                type: 'bar',
                barWidth: '50%',
                data: counts,
                itemStyle: {
                    color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                        { offset: 0, color: '#67C23A' },
                        { offset: 1, color: '#95D475' }
                    ])
                },
                emphasis: {
                    itemStyle: {
                        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                            { offset: 0, color: '#67C23A' },
                            { offset: 1, color: '#95D475' }
                        ])
                    }
                }
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
watch(() => props.distributionData, () => {
    if (props.distributionData.length > 0) {
        if (chart) {
            updateChart()
        } else {
            initChart()
        }
    }
}, { deep: true })

onMounted(() => {
    initChart()
})

onBeforeUnmount(() => {
    window.removeEventListener('resize', handleResize)
    chart && chart.dispose()
    chart = null
})
</script>

<style lang="scss" scoped>
.patient-new-distribution {
    padding: 5px;
    height: 100%;
    display: flex;
    flex-direction: column;
    
    .stat-label {
        font-size: 20px;
        color: #666;
        align-self: flex-start;
        font-family: PingFangSC-Ultralight;
        font-style: oblique;
        font-weight: 600;
        margin-bottom: 20px;
    }
    
    .chart-container {
        flex: 1;
        min-height: 100px; /* 确保最小高度 */
        height: calc(100% - 50px); /* 调整为50px，为标题和间距留出空间 */
    }
}
</style> 
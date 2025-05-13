<script setup>
import { ref, onMounted } from 'vue';
import AMapLoader from '@amap/amap-jsapi-loader';
import InstitutionList from './institutionList.vue';

const map = ref(null);

// 苏州医院数据
const suzhouHospitals = [
  { 
    name: '苏州大学附属第一医院',
    position: [120.6289, 31.3136],
    address: '苏州市姑苏区平海路899号'
  },
  { 
    name: '苏州大学附属第二医院',
    position: [120.5851, 31.3021],
    address: '苏州市姑苏区三香路1055号'
  },
  { 
    name: '苏州市立医院',
    position: [120.6175, 31.3220],
    address: '苏州市姑苏区道前街26号'
  },
  { 
    name: '苏州中医院',
    position: [120.6257, 31.3042],
    address: '苏州市姑苏区沧浪新城枫津路18号'
  },
  { 
    name: '苏州九龙医院',
    position: [120.6095, 31.2746],
    address: '苏州市工业园区星港街9号'
  },
  { 
    name: '苏州科技城医院',
    position: [120.4296, 31.2543],
    address: '苏州市高新区漓江路1号'
  },
  { 
    name: '苏州市吴中人民医院',
    position: [120.6314, 31.2695],
    address: '苏州市吴中区东吴南路61号'
  },
  { 
    name: '苏州明基医院',
    position: [120.7403, 31.2582],
    address: '苏州工业园区园区星湖街168号'
  }
];

// 初始化地图
const initMap = async () => {
  try {
    const AMap = await AMapLoader.load({
      key: 'd5043f2b82db327c0f701126bc5be7cf',
      version: '2.0',
      plugins: ['AMap.Scale', 'AMap.HawkEye', 'AMap.ToolBar']
    });

    // 创建地图实例，中心点定位到苏州
    map.value = new AMap.Map('map-container', {
      zoom: 12, // 设置合适的缩放级别
      mapStyle: "amap://styles/light",
      center: [120.5954, 31.3027] // 苏州中心坐标
    });

    // 添加控件
    AMap.plugin(['AMap.Scale', 'AMap.HawkEye', 'AMap.ToolBar'], function() {
      const scale = new AMap.Scale();
      const hawkEye = new AMap.HawkEye();
      const toolBar = new AMap.ToolBar();

      map.value.addControl(scale);
      map.value.addControl(hawkEye);
      map.value.addControl(toolBar);
    });

    // 添加医院标记
    suzhouHospitals.forEach(hospital => {
      const marker = new AMap.Marker({
        position: hospital.position,
        title: hospital.name,
        animation: 'AMAP_ANIMATION_DROP', // 添加动画效果
        icon: new AMap.Icon({
          size: new AMap.Size(30, 36),
          image: 'https://webapi.amap.com/theme/v1.3/markers/n/mark_r.png',
          imageSize: new AMap.Size(30, 36)
        })
      });
      
      // 将创建的点标记添加到地图
      map.value.add(marker);
    });

    // 绑定地图事件
    map.value.on('click', (event) => {
      console.log('地图点击坐标：', event.lnglat);
    });
  } catch (error) {
    console.error('地图加载失败：', error);
  }
};

onMounted(() => {
  initMap();
});
</script>

<template>
  <div :style="{ display: 'flex', height: '90vh', width: '100%', marginTop: '20px', padding: '0 20px' }">
    <div id="map-container" :style="{ flex: 2, height: '100%', backgroundColor: '#f0f0f0' }"></div>
    <InstitutionList />
  </div>
</template>

<style scoped>
/* 添加一些基本样式 */
#map-container {
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}
</style>
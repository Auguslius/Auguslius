import request from '@/utils/request';
import axios from 'axios';

/**
 * 获取当前城市天气信息
 * 使用高德开放平台API
 * @param {String} city 城市名称，默认为"北京"
 * @returns {Promise} Promise对象
 */
export function getWeatherInfo(city = '苏州') {
  // 这里使用模拟数据，实际开发中应替换为真实API调用
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve({
        code: 0,
        data: {
          city: city,
          weather: '晴',
          temperature: '25°C',
          humidity: '45%',
          windSpeed: '3级',
          date: new Date().toLocaleDateString(),
          time: new Date().toLocaleTimeString(),
          tips: '天气不错，适合外出',
          icon: 'sunny'
        },
        message: '获取天气信息成功'
      });
    }, 500);
  });
  
  // 实际开发中的API调用示例
  /* return axios.get('https://restapi.amap.com/v3/weather/weatherInfo', {
    params: {
      key: 'YOUR_API_KEY',
      city: city,
      extensions: 'base',
      output: 'JSON'
    }
  }); */
} 
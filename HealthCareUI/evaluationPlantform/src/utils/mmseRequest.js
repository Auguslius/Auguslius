import axios from 'axios';
import { ElMessage } from 'element-plus';
import { useTokenStore } from '@/stores/token.js';

const baseURL = '/mmse-api';  // 使用代理路径而不是直接访问8081端口
const instance = axios.create({ baseURL })

// 添加请求拦截器
instance.interceptors.request.use(
    (config) => {
        // 添加token
        const tokenStore = useTokenStore();
        if(tokenStore.token){
            config.headers.Authorization = tokenStore.token
        }
        return config;
    },
    (err) => {
        return Promise.reject(err)
    }
)

import router from '@/router/router.js'
// 添加响应拦截器
instance.interceptors.response.use(
    result => {
        // 处理成功的情况 - 适配多种可能的成功码
        if(result.data.code === 0 || result.data.code === 200){
            return result.data;
        }
        ElMessage.error(result.data.message ? result.data.message : '服务异常')
        return Promise.reject(result.data)
    },
    err => {
        if(err.response && err.response.status === 401){
            ElMessage.error('请先登录')
            router.push('/login')
        }else{
            ElMessage.error('服务异常：' + (err.message || '未知错误'))
        }
        return Promise.reject(err);
    }
)

export default instance 
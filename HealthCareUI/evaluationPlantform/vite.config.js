import { fileURLToPath, URL } from 'node:url'
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
    vue(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url)),
    },
  },
  server: {
    proxy: {
      '/api': {
        target: 'http://localhost:8080',  // 后端服务器地址
        changeOrigin: true,  // 修改请求头中的 Origin 字段
        secure: false,  // 如果后端是 https 的话需要设置为 false 来关闭 SSL 校验
        rewrite: (path)=>path.replace(/^\/api/, '')
      },
      '/mmse-api': {
        target: 'http://localhost:8081',  // MMSE接口服务器地址
        changeOrigin: true,
        secure: false,
        rewrite: (path) => path.replace(/^\/mmse-api/, '')
      }
    },
  },
})

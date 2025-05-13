//定义store
import { defineStore } from 'pinia';
import { ref } from 'vue';

export const useTokenStore = defineStore('token', () => {
  const token = ref('');

  const setToken = (newToken) => {
    console.log('TokenStore - 设置新token:', newToken);
    token.value = newToken;
  };

  const getToken = () => token.value;

  const removeToken = () => {
    console.log('TokenStore - 移除token');
    token.value = '';
  };

  return {
    token,
    setToken,
    getToken,
    removeToken,
  };
}, { persist: true });
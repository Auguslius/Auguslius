import { defineStore } from 'pinia';
import { ref, computed } from 'vue';
import { userInfoService, userListService, userDeleteService, userInfoUpdateService, userLogoutService, userLoginService } from '@/api/user';
import { useRouter } from 'vue-router';
import { useTokenStore } from '@/stores/token.js';
import request from '@/utils/request';

export const useUserInfoStore = defineStore('userInfo', () => {
  const info = ref({});
  const router = useRouter();
  const tokenStore = useTokenStore();

  const setInfo = (newInfo) => {
    info.value = newInfo;
  };

  const getInfo = () => {
    return info.value;
  };

  const removeInfo = () => {
    info.value = {};
  };

  const fetchUserInfo = async () => {
    try {
      const res = await userInfoService();
      setInfo(res.data);
      return res;
    } catch (error) {
      throw error;
    }
  };

  const deleteUser = async (id) => {
    const response = await userDeleteService(id);
    return response;
  };

  const updateUser = async (userData) => {
    try {
      const res = await userInfoUpdateService(userData);
      return res;
    } catch (error) {
      throw error;
    }
  };

  const fetchUserList = async (pageNo, pageSize, searchParams = {}) => {
    try {
      const res = await userListService(pageNo, pageSize, searchParams);
      return res;
    } catch (error) {
      throw error;
    }
  };

  // 添加登录方法
  const login = async (loginData) => {
    try {
        const res = await userLoginService(loginData);
        if (res.code === 0) {
            const token = res.data;
            const userInfo = res.data.userInfo;
            tokenStore.setToken(token);
            setInfo({ ...userInfo, token });
            return res;
        }
        throw new Error(res.message || '登录失败');
    } catch (error) {
        throw error;
    }
};

  const logout = async (number) => {
    try {
      if (!number) {
        throw new Error('用户编号不能为空');
      }
      const res = await userLogoutService(number);
      if (res.code === '200' || res.code === 200 || res.message === '退出成功') {
        // 清除所有状态
        removeInfo();
        tokenStore.removeToken();
        router.push('/login');
        return res;
      }
      throw new Error(res.message || '退出失败');
    } catch (error) {
      if (error.message && error.message.includes('成功')) {
        removeInfo();
        tokenStore.removeToken();
        router.push('/login');
        return;
      }
      throw error;
    }
  };

  const getUserDisplayInfo = computed(() => {
    return {
      name: info.value.name || info.value.username || '未登录',
      avatar: info.value.userPic || '',
      role: info.value.position || '用户'
    };
  });

  return {
    info,
    setInfo,
    removeInfo,
    getInfo,
    fetchUserInfo,
    deleteUser,
    updateUser,
    fetchUserList,
    logout,
    login,
    getUserDisplayInfo
  };
}, { persist: true });

// 添加默认导出
export default useUserInfoStore;
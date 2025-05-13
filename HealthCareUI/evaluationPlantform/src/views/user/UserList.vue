<script setup>
import { ref, onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import useUserInfoStore from '@/stores/userInfo';

const userInfoStore = useUserInfoStore();

const userList = ref([]);

const fetchAllUsers = async () => {
  try {
    await userInfoStore.fetchUserInfo();
    userList.value = userInfoStore.getInfo();
  } catch (error) {
    ElMessage.error('获取用户信息失败');
  }
};

onMounted(() => {
  fetchAllUsers();
});
</script>

<template>
  <div class="user-list">
    <el-table :data="userList" style="width: 100%">
      <el-table-column prop="username" label="用户名" width="180"></el-table-column>
      <el-table-column prop="nickname" label="昵称" width="180"></el-table-column>
      <el-table-column prop="address" label="地址" width="180"></el-table-column>
      <el-table-column prop="position" label="职位" width="180"></el-table-column>
      <el-table-column prop="email" label="邮箱" width="180"></el-table-column>
      <el-table-column prop="userPic" label="用户头像" width="180">
        <template #default="scope">
          <img :src="scope.row.userPic" alt="用户头像" style="width: 50px; height: 50px; border-radius: 50%;" />
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<style lang="scss" scoped>
.user-list {
  width: 800px;
  height: 600px;
  margin: 0;
  padding: 20px;
  background-color: #fff;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  border-radius: 8px;
  position: absolute;
  top: 0;
  left: 0;
}
</style>

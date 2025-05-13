<template>
  <el-dialog v-loading="loading" v-model="dialogVisible" width="30%" @close="handleClose">
    <el-form :model="form" label-width="80px">
      <el-form-item label="用户名">
        <el-input v-model="form.username"></el-input>
      </el-form-item>
      <el-form-item label="昵称">
        <el-input v-model="form.nickname"></el-input>
      </el-form-item>
      <el-form-item label="年龄">
        <el-input v-model="form.age"></el-input>
      </el-form-item>
      <el-form-item label="编号">
        <el-input v-model="form.number"></el-input>
      </el-form-item>
      <el-form-item label="机构">
        <el-input v-model="form.institution"></el-input>
      </el-form-item>
      <el-form-item label="职位">
        <el-input v-model="form.position"></el-input>
      </el-form-item>
      <el-form-item label="科室">
        <el-input v-model="form.room"></el-input>
      </el-form-item>
      <el-form-item label="邮箱">
        <el-input v-model="form.email"></el-input>
      </el-form-item>
      <el-form-item label="地址">
        <el-input v-model="form.address"></el-input>
      </el-form-item>
      <el-form-item label="电话">
        <el-input v-model="form.phone"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleSubmit">提交</el-button>
        <el-button @click="handleClose">取消</el-button>
      </el-form-item>
    </el-form>
  </el-dialog>
</template>

<script setup>
import { ref, watch } from 'vue';
import { ElMessage } from 'element-plus'
import useUserInfoStore from '@/stores/userInfo'


const props = defineProps({
  user: Object
});
const emit = defineEmits(['close']);

const dialogVisible = ref(true);
// 使用独立的表单数据，拷贝 props.user
const form = ref({ ...props.user });

// 监听父组件传入的 user 数据变化
watch(() => props.user, (newVal) => {
  form.value = { ...newVal };
});

const userInfoStore = useUserInfoStore()

const handleSubmit = async () => {
  try {
    console.log('提交表单', form.value)
    // 调用更新接口，更新用户信息
    const res = await userInfoStore.updateUser(form.value)
    if (res.code === 0) {
      ElMessage.success('更新成功')
      emit('fetchUserList')
    } else {
      throw new Error(res.msg || '更新失败')
    }
  } catch (error) {
    ElMessage.error(error.message || '提交失败')
  } finally {
    handleClose()
  }
};

const handleClose = () => {
  dialogVisible.value = false;
  emit('close');
};
</script>

<style scoped>
.el-form {
  width: 400px;
}
</style>

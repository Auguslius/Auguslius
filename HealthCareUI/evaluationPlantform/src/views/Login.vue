<template>
    <el-row class="login-page">
        <el-col :span="12" class="bg">
            <!-- 添加蒙版和文字 -->
            <div class="overlay"></div>
            <div class="chinese-text-container">
                <img class="logo" src="@/assets/pic/logo.png" alt="Logo" />
                <div class="chinese-text">智能评估平台</div>
            </div>
            <div>
                <div class="english-text">
                    <span class="green-text">Alzheimer's</span> <span class="white-text">Smart Assessment
                        Platform</span>
                </div>
            </div>
            <div class="tagline">关爱每一个记忆，守护每一个生命</div>
        </el-col>
        <!-- 右侧表单 -->
        <el-col :span="6" :offset="3" class="form">
            <!-- 注册表单 -->
            <el-form ref="form" size="large" autocomplete="off" v-if="isRegister" :model="registerData" :rules="rules">
                <el-form-item>
                    <h1>注册</h1>
                </el-form-item>
                <el-form-item prop="username">
                    <el-input :prefix-icon="User" placeholder="请输入用户名" v-model="registerData.username"></el-input>
                </el-form-item>
                <el-form-item prop="password">
                    <el-input :prefix-icon="Lock" type="password" placeholder="请输入密码"
                        v-model="registerData.password"></el-input>
                </el-form-item>
                <el-form-item prop="rePassword">
                    <el-input :prefix-icon="Lock" type="password" placeholder="请输入再次密码"
                        v-model="registerData.rePassword"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button class="button custom-button" type="primary" auto-insert-space
                        @click="register">
                        注册
                    </el-button>
                </el-form-item>
                <el-form-item class="flex">
                    <el-link type="info" :underline="false" @click="switchToLogin">
                        ← 返回
                    </el-link>
                </el-form-item>
            </el-form>
            <!-- 登录表单 -->
            <el-form ref="form" size="large" autocomplete="off" v-else-if="isLogin" :model="registerData" :rules="rules">
                <el-form-item>
                    <h1>登录</h1>
                </el-form-item>
                <el-form-item prop="username">
                    <el-input :prefix-icon="User" placeholder="请输入用户名" v-model="registerData.username"></el-input>
                </el-form-item>
                <el-form-item prop="password">
                    <el-input name="password" :prefix-icon="Lock" type="password" placeholder="请输入密码"
                        v-model="registerData.password"></el-input>
                </el-form-item>
                <el-form-item class="flex">
                    <div class="flex">
                        <el-checkbox>记住我</el-checkbox>
                        <el-link type="primary" :underline="false" @click="switchToForgotPassword">忘记密码？</el-link>
                    </div>
                </el-form-item>
                <el-form-item>
                    <el-button class="button custom-button" type="primary" auto-insert-space
                        @click="login">登录</el-button>
                </el-form-item>
                <el-form-item class="flex">
                    <el-link type="info" :underline="false" @click="switchToRegister">
                        注册 →
                    </el-link>
                </el-form-item>
            </el-form>
            <!-- 找回密码表单 -->
            <el-form ref="form" size="large" autocomplete="off" v-else-if="isForgotPassword" :model="forgotPasswordData" :rules="forgotPasswordRules">
                <el-form-item>
                    <h1>找回密码</h1>
                </el-form-item>
                <el-form-item>
                    <el-radio-group v-model="forgotPasswordMethod">
                        <el-radio label="email">通过邮箱找回</el-radio>
                        <el-radio label="phone">通过手机号找回</el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item v-if="forgotPasswordMethod === 'email'" prop="email">
                    <el-input :prefix-icon="User" placeholder="请输入注册邮箱" v-model="forgotPasswordData.email"></el-input>
                </el-form-item>
                <el-form-item v-if="forgotPasswordMethod === 'phone'" prop="phone">
                    <el-input :prefix-icon="User" placeholder="请输入注册手机号" v-model="forgotPasswordData.phone"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button class="button custom-button" type="primary" auto-insert-space @click="requestForgotPasswordToken">找回密码</el-button>
                </el-form-item>
                <el-form-item class="flex">
                    <el-link type="info" :underline="false" @click="switchToLogin">← 返回</el-link>
                </el-form-item>
            </el-form>
            <!-- 重置密码表单 -->
            <el-form ref="form" size="large" autocomplete="off" v-else :model="resetPasswordData" :rules="resetPasswordRules">
                <el-form-item>
                    <h1>重置密码</h1>
                </el-form-item>
                <el-form-item prop="newPassword">
                    <el-input :prefix-icon="Lock" type="password" placeholder="请输入新密码" v-model="resetPasswordData.newPassword"></el-input>
                </el-form-item>
                <el-form-item prop="confirmPassword">
                    <el-input :prefix-icon="Lock" type="password" placeholder="请再次输入新密码" v-model="resetPasswordData.confirmPassword"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button class="button custom-button" type="primary" auto-insert-space @click="resetPassword">重置密码</el-button>
                </el-form-item>
                <el-form-item class="flex">
                    <el-link type="info" :underline="false" @click="switchToLogin">← 返回</el-link>
                </el-form-item>
            </el-form>
        </el-col>
    </el-row>
</template>

<script setup>
import { User, Lock } from '@element-plus/icons-vue'
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { useUserInfoStore } from '@/stores/userInfo';
//控制注册与登录表单的显示， 默认显示登录
const isLogin = ref(true)
const isRegister = ref(false)
const isForgotPassword = ref(false)
const isResetPassword = ref(false)
const forgotPasswordMethod = ref('email')
let resetToken = ''
//定义数据模型
const registerData = ref({
    username: '',
    password: '',
    rePassword: ''
})
const forgotPasswordData = ref({
    email: '',
    phone: ''
})
const resetPasswordData = ref({
    newPassword: '',
    confirmPassword: ''
})

//校验密码的函数
const checkRePassword = (rule, value, callback) => {
    if (value === '') {
        callback(new Error('请再次确认密码'))
    } else if (value !== registerData.value.password) {
        callback(new Error('请确保两次输入的密码一样'))
    } else {
        callback()
    }
}

//定义表单校验规则
const rules = {
    username: [
        { required: true, message: '请输入用户名', trigger: 'blur' },
        { min: 5, max: 16, message: '长度为5~16位非空字符', trigger: 'blur' }
    ],
    password: [
        { required: true, message: '请输入密码', trigger: 'blur' },
        { min: 5, max: 16, message: '长度为5~16位非空字符', trigger: 'blur' }
    ],
    rePassword: [
        { validator: checkRePassword, trigger: 'blur' }
    ]
}
const forgotPasswordRules = {
    email: [
        { required: true, message: '请输入注册邮箱', trigger: 'blur' },
        { type: 'email', message: '请输入有效的邮箱地址', trigger: 'blur' }
    ],
    phone: [
        { required: true, message: '请输入注册手机号', trigger: 'blur' },
        { pattern: /^1[3-9]\d{9}$/, message: '请输入有效的手机号', trigger: 'blur' }
    ]
}
const resetPasswordRules = {
    newPassword: [
        { required: true, message: '请输入新密码', trigger: 'blur' },
        { min: 5, max: 16, message: '长度为5~16位非空字符', trigger: 'blur' }
    ],
    confirmPassword: [
        { required: true, message: '请再次输入新密码', trigger: 'blur' },
        { validator: (rule, value, callback) => {
            if (value !== resetPasswordData.value.newPassword) {
                callback(new Error('两次输入的密码不一致'))
            } else {
                callback()
            }
        }, trigger: 'blur' }
    ]
}

import { userRegisterService, userLoginService, userForgotPasswordRequest, userValidatePasswordReset, userResetPassword, userInfoService } from '@/api/user.js'
const register = async () => {
  // 获取表单中的密码和确认密码
  const password = registerData.value.password;
  const rePassword = registerData.value.rePassword;

  // 校验两次密码是否一致
  if (password !== rePassword) {
    ElMessage.error('两次密码不一致，请重新输入');
    return; 
    // 如果密码不一致，直接返回，不发送请求
  }

  // 如果密码一致，发送注册请求
  try {
    let result = await userRegisterService(registerData.value);
    ElMessage.success(result.msg ? result.msg : '注册成功');
  } catch (error) {
    console.error('注册失败:', error);
    ElMessage.error('注册失败，请重试');
  }
};
import {useTokenStore} from '@/stores/token.js'

const router = useRouter()
const tokenStore = useTokenStore();
import { useRouter } from "vue-router";


const login = async () => {
    try {
        const result = await userInfoStore.login(registerData.value);
        
        // 登录成功后获取用户详细信息
        const userInfoResponse = await userInfoService();
        const userInfo = userInfoResponse.data;
        
        // 检查用户是否已认证
        if (userInfo.isAuthenticated === 0) {
            ElMessage.warning('请先完善个人资料');
            router.push('/user/Info');
            return;
        }
        
        ElMessage.success('登录成功');
        router.push('/home');
    } catch (error) {
        // 优先显示后端返回的错误消息
        const backendMessage = error.response?.data?.msg || error.response?.data?.message;
        ElMessage.error(backendMessage || error.message );
    }
}
const requestForgotPasswordToken = async () => {
    try {
        let result;
        if (forgotPasswordMethod.value === 'email') {
            result = await userForgotPasswordRequest({ email: forgotPasswordData.value.email });
        } else {
            result = await userForgotPasswordRequest({ phone: forgotPasswordData.value.phone });
        }
        ElMessage.success(result.msg ? result.msg : '修改请求已发送');
        console.log('result',result);
        resetToken = result.data; // 存储token值

        isResetPassword.value = true;
        isForgotPassword.value = false;
    } catch (error) {
        ElMessage.error('找回密码失败，请重试');
    }
}
const resetPassword = async () => {
    try {
        console.log('resetToken',resetToken);
        let validateResult = await userValidatePasswordReset(resetToken); // 使用存储的token值
        if (validateResult.data) {
            let result = await userResetPassword({
                token: resetToken, // 使用存储的token值
                newPassword: resetPasswordData.value.newPassword
            });
            ElMessage.success(result.msg ? result.msg : '密码重置成功');
            isResetPassword.value = false;
        } else {
            ElMessage.error('验证码无效，请重试');
        }
    } catch (error) {
        ElMessage.error('重置密码失败，请重试');
    }
}

const userInfoStore = useUserInfoStore();

const handleLogin = async (formData) => {
  try {
    await userInfoStore.login(formData);
    // 登录成功后的处理...
  } catch (error) {
    // 错误处理...
  }
};

//定义函数,清空数据模型的数据
const clearRegisterData = () => {
    registerData.value = {
        username: '',
        password: '',
        rePassword: ''
    }
}
const clearForgotPasswordData = () => {
    forgotPasswordData.value = {
        email: '',
        phone: ''
    }
}
const clearResetPasswordData = () => {
    resetPasswordData.value = {
        newPassword: '',
        confirmPassword: ''
    }
}

//定义函数,切换表单状态
const switchToLogin = () => {
    isLogin.value = true
    isRegister.value = false
    isForgotPassword.value = false
    isResetPassword.value = false
    clearRegisterData()
    clearForgotPasswordData()
    clearResetPasswordData()
}
const switchToRegister = () => {
    isLogin.value = false
    isRegister.value = true
    isForgotPassword.value = false
    isResetPassword.value = false
    clearRegisterData()
}
const switchToForgotPassword = () => {
    isLogin.value = false
    isRegister.value = false
    isForgotPassword.value = true
    isResetPassword.value = false
    clearForgotPasswordData()
}
</script>




<style lang="scss" scoped>
.login-page {
    height: 100vh;
    background-color: #fff;

    .bg {
        position: relative;
        background: url('@/assets/pic/bg.png') no-repeat center / cover;
        border-radius: 0 20px 20px 0;
        overflow: hidden;
    }

    /* 半透明灰色蒙版 */
    .overlay {
        position: absolute;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
        background-color: rgba(0, 0, 0, 0.5);
        z-index: 1;
    }

    .chinese-text-container {
        display: flex;
        align-items: center;
        position: absolute;
        top: 20%;
        left: 20px;
        z-index: 2;
    }

    /*logo*/
    .logo {
        width: 45px;
        height: auto;
        margin-right: 10px;
    }

    /*中文文字*/
    .chinese-text {
        left: 20px;
        font-size: 45px;
        font-weight: bold;
        color: #ffffff;
        text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.5);
        z-index: 2;
    }

    /*英文文字*/
    .english-text {
        position: absolute;
        top: 26%;
        left: 20px;
        font-size: 35px;
        font-weight: normal;
        color: #008E65;
        font-family: 'Arial', sans-serif;
        text-shadow: 1px 1px 3px rgba(0, 0, 0, 0.5);
        z-index: 2;
    }

    .green-text {
        color: #008E65;
        font-weight: 800;
    }

    .white-text {
        color: #fff;
    }

    /*底部文字*/
    .tagline {
        position: absolute;
        bottom: 2.5%;
        right: 20px;
        font-size: 20px;
        font-weight: 500;
        color: #fff;
        font-family: 'Helvetica Neue', sans-serif;
        font-style: italic;
        text-shadow: 3px 3px 6px rgba(0, 0, 0, 0.8), 0 0 25px rgba(255, 255, 255, 0.5);
        z-index: 2;
    }

    .form {
        display: flex;
        flex-direction: column;
        justify-content: center;
        user-select: none;
        position: relative;
        z-index: 2;
    }

    .title {
        margin: 0 auto;
    }

    .button {
        width: 100%;
    }

    .custom-button {
        background-color: #008E65;
        border-color: #008E65;
    }

    .custom-button:hover {
        background-color: #26d5bb;
        /* 鼠标悬停时的颜色（可选） */
        border-color: #26d5bb;
    }


    .flex {
        width: 100%;
        display: flex;
        justify-content: space-between;
    }
}
</style>

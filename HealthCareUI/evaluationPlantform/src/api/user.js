//导入request.js请求工具
import request from '@/utils/request.js'
import { useTokenStore } from '@/stores/token.js'

//提供调用注册接口的函数
export const userRegisterService = (registerData) => {
    //借助于UrlSearchParams完成传递
    const params = new URLSearchParams()
    for(let key in registerData){
        params.append(key,registerData[key]);
    }
    return request.post('/user/register',params);
}

// 提供调用登录接口的函数
export const userLoginService = (loginData) => {
    const params = new URLSearchParams();
    params.append('username', loginData.username);
    params.append('password', loginData.password);
    return request.post('/user/login', params);
}

// 修改密码三部
export const userForgotPasswordRequest = (forgotPasswordData) => {
    return request.post('/password-reset/requestPwd', forgotPasswordData);
}

export const userValidatePasswordReset = (token) => {
    return request.get('/password-reset/validate', {
        params: { token }
    });
}

export const userResetPassword = (resetData) => {
    return request.post('/password-reset/reset', resetData);
}

//获取用户详细信息
export const userInfoService = () => {
    return request.get('/user/userInfo')
}

//修改个人信息
export const userInfoUpdateService = (userInfoData) => {
    return request.patch('/user/update', userInfoData);
}

export const userDeleteService = (id) => {
    console.log('userDeleteService - id:', id);
    return request.delete(`/user/${id}`)
}

//修改头像
export const userAvatarUpdateService = (avatarUrl) => {
    const params = new URLSearchParams();
    params.append('avatarUrl',avatarUrl)
    return request.patch('/user/updateAvatar',params)
}

// 获取用户列表
export const userListService = (pageNo, pageSize, searchParams = {}) => {
    return request.get('/user/page', {
        params: {
            pageNo,
            pageSize,
            ...searchParams
        }
    });
}

// 用户登出
export const userLogoutService = (number) => {
    return request.post('/user/logout', { number });
}



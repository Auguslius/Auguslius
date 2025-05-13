package org.example.web.rest.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.common.enums.CommonEnum;
import org.example.common.enums.PasswordResetEnum;
import org.example.common.enums.UserEnum;
import org.example.exception.BusinessException;
import org.example.domain.entity.User;
import org.example.domain.entity.UserPasswordResetRequest;
import org.example.domain.dto.PasswordResetDTO;
import org.example.domain.dto.PasswordResetRequestDTO;
import org.example.utils.Md5Util;
import org.example.web.rest.mapper.UserMapper;
import org.example.web.rest.mapper.UserPasswordResetRequestMapper;
import org.example.web.rest.service.PasswordResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class PasswordResetServiceImpl extends ServiceImpl<UserPasswordResetRequestMapper, UserPasswordResetRequest> implements PasswordResetService{

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserPasswordResetRequestMapper passwordResetRequestMapper;

    @Override
    public String requestPasswordReset(PasswordResetRequestDTO passwordResetRequestDTO) {
        //验证手机邮箱是否为空
        if (passwordResetRequestDTO.getPhone() == null && passwordResetRequestDTO.getEmail() == null) {
            throw new BusinessException(CommonEnum.FAIL.getCode(), PasswordResetEnum.PASSWORD_RESET_FAIL.getMessage());
        }
        User user = userMapper.selectByEmailOrPhone(passwordResetRequestDTO.getEmail(), passwordResetRequestDTO.getPhone());
        if (user == null) {
            throw new BusinessException(CommonEnum.FAIL.getCode(), UserEnum.USER_NOT_EXIST.getMessage());
        }
        // 检查用户状态是否为active
        if (!"active".equals(user.getStatus())) {
            throw new BusinessException(CommonEnum.FAIL.getCode(), UserEnum.USER_INACTIVE.getMessage());
        }
        String token = UUID.randomUUID().toString();
        UserPasswordResetRequest passwordResetRequest = new UserPasswordResetRequest();
        passwordResetRequest.setNumber(user.getNumber());
        passwordResetRequest.setEmail(passwordResetRequestDTO.getEmail());
        passwordResetRequest.setPhone(passwordResetRequestDTO.getPhone());
        passwordResetRequest.setToken(token);
        passwordResetRequest.setRequestedTime(new java.util.Date());
        passwordResetRequest.setCreateBy(user.getUsername());
        passwordResetRequestMapper.insert(passwordResetRequest);
        return token;
    }

    @Override
    public Boolean validatePasswordResetRequest(String token) {
        // 通过 MyBatis-Plus lambdaQuery 查询
        UserPasswordResetRequest passwordResetRequest = passwordResetRequestMapper
                .selectOne(new LambdaQueryWrapper<UserPasswordResetRequest>()
                        .eq(UserPasswordResetRequest::getToken, token));

        if (passwordResetRequest == null) {
            throw new BusinessException(CommonEnum.FAIL.getCode(), PasswordResetEnum.PASSWORD_VALIDATED_FAIL.getMessage());
        }
        if (passwordResetRequest.getUsed()) {
            throw new BusinessException(CommonEnum.FAIL.getCode(), PasswordResetEnum.PASSWORD_VALIDATED_FAIL.getMessage());
        }
        return true;
    }


    @Override
    public Boolean resetPassword(PasswordResetDTO passwordResetDTO) {
        // 通过 token 查询密码重置请求
        UserPasswordResetRequest passwordResetRequest = passwordResetRequestMapper.selectOne(
                new QueryWrapper<UserPasswordResetRequest>().eq("token", passwordResetDTO.getToken())
        );

        if (passwordResetRequest == null) {
            throw new BusinessException(CommonEnum.FAIL.getCode(), UserEnum.USER_NOT_EXIST.getMessage());
        }
        if (passwordResetRequest.getUsed()) {
            throw new BusinessException(CommonEnum.FAIL.getCode(), PasswordResetEnum.PASSWORD_RESET_FAIL.getMessage());
        }

        // 通过 number 查询用户
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getNumber, passwordResetRequest.getNumber()));

        if (user == null) {
            throw new BusinessException(CommonEnum.FAIL.getCode(), UserEnum.USER_NOT_EXIST.getMessage());
        }

        // 使用 MD5 对新密码进行加密
        String encryptedPassword = Md5Util.getMD5String(passwordResetDTO.getNewPassword());

        // 更新用户密码
        user.setPassword(encryptedPassword);
        userMapper.updateById(user);

        // 标记密码重置请求为已使用
        passwordResetRequest.setUsed(true);
        passwordResetRequest.setUpdateTime(new Date());
        passwordResetRequestMapper.updateById(passwordResetRequest);

        return true;
    }


}

package org.example.rest.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.apache.commons.lang3.StringUtils;
import org.example.common.Enum.LoginExceptionEnum;
import org.example.domain.vo.CaptchaVO;
import org.example.domain.vo.ClientVO;
import org.example.domain.vo.LoginVO;
import org.example.exception.BusinessException;
import org.example.domain.dto.LoginDto;
import org.example.domain.dto.ValidateDto;
import org.example.domain.entity.Client;
import org.example.rest.mapper.ClientMapper;
import org.example.rest.service.LoginService;
import org.example.utils.JwtUtil;
import org.example.utils.RedisUtil;
import org.example.utils.UuidUtil;
import org.example.utils.ValidateCodeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@Service
public class LoginServiceImpl extends ServiceImpl<ClientMapper, Client> implements LoginService {

    private static final Logger log = LoggerFactory.getLogger(LoginServiceImpl.class);

    private static final String TOKEN_KEY_PREFIX = "TOKEN:";
    private static final Integer VALIDATE_CODE_EXPIRE_TIME = 120;



    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private ClientMapper clientMapper;

    /*
     * 获取验证码
     *
     * @return CaptchaVO 验证码信息
     * */
    @Override
    public CaptchaVO getValidateCode() {
        ValidateDto validateDto = ValidateCodeUtil.getRandomCode();
        // 获取验证码值
        String codeValue = validateDto.getValue();
        // 生成唯一标识
        String captchaId = UUID.randomUUID().toString();

        // 存入Redis，设置过期时间
        redisUtil.set("validate_code:" + captchaId, codeValue, VALIDATE_CODE_EXPIRE_TIME);

        // 构建返回对象
        CaptchaVO captchaVO = new CaptchaVO();
        captchaVO.setCaptchaId(captchaId);
        captchaVO.setCode(codeValue);
        captchaVO.setImage(validateDto.getBase64Str());
        captchaVO.setExpire(VALIDATE_CODE_EXPIRE_TIME);

        return captchaVO;
    }


    /*
    * 账号密码登录
    *
    * @param loginDto 登录信息
    * @return loginVO 当前登录用户
    * */
    @Override
    public LoginVO loginByPassword(LoginDto loginDto) {
        String userInputCode = loginDto.getCode();
        String captchaId = loginDto.getCaptchaId(); // 获取验证码标识
        String username = loginDto.getUsername();
        String password = loginDto.getPassword();

        // 验证码验证
        if (StringUtils.isBlank(captchaId) || StringUtils.isBlank(userInputCode)) {
            throw new BusinessException(LoginExceptionEnum.CAPTCHA_ID_EMPTY.getCode(), LoginExceptionEnum.CAPTCHA_ID_EMPTY.getMessage());
        }

        // 从Redis获取验证码
        String correctCode = (String) redisUtil.get("validate_code:" + captchaId);
        if (correctCode == null) {
            throw new BusinessException(LoginExceptionEnum.CAPTCHA_EXPIRED.getCode(), LoginExceptionEnum.CAPTCHA_EXPIRED.getMessage());
        }

        // 验证码不匹配
        if (!correctCode.equalsIgnoreCase(userInputCode)) {
            throw new BusinessException(LoginExceptionEnum.CAPTCHA_ERROR.getCode(), LoginExceptionEnum.CAPTCHA_ERROR.getMessage());
        }

        // 验证成功后，删除Redis中的验证码
        redisUtil.del("validate_code:" + captchaId);

        //用户名密码校验
        if (StringUtils.isBlank(username)) {
            throw new BusinessException(LoginExceptionEnum.USERNAME_EMPTY.getCode(), LoginExceptionEnum.USERNAME_EMPTY.getMessage());
        }
        if (StringUtils.isBlank(password)) {
            throw new BusinessException(LoginExceptionEnum.PASSWORD_EMPTY.getCode(), LoginExceptionEnum.PASSWORD_EMPTY.getMessage());
        }

        // 查询用户
        LambdaQueryWrapper<Client> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Client::getUsername, username);
        Client client = clientMapper.selectOne(queryWrapper);

        // 用户不存在或密码错误
        if (client == null || !password.equals(client.getPassword())) {
            throw new BusinessException(LoginExceptionEnum.USERNAME_PASSWORD_ERROR.getCode(),
                    LoginExceptionEnum.USERNAME_PASSWORD_ERROR.getMessage());
        }

        // 验证用户状态
        if (client.getStatus() == 0) {
            throw new BusinessException(LoginExceptionEnum.UNCHECKED_USER.getCode(),
                    LoginExceptionEnum.UNCHECKED_USER.getMessage());
        } else if (client.getStatus() == 2) {
            throw new BusinessException(LoginExceptionEnum.DISABLED_USER.getCode(),
                    LoginExceptionEnum.DISABLED_USER.getMessage());
        } else if (client.getStatus() == 3) {
            throw new BusinessException(LoginExceptionEnum.AUTH_FAILED.getCode(),
                    LoginExceptionEnum.AUTH_FAILED.getMessage());
        }

        // 更新登录信息
        client.setLoginCount(client.getLoginCount() == null ? 1 : client.getLoginCount() + 1);
        client.setLastLoginTime(new Date());

        clientMapper.updateById(client);

        // 生成token
        String token = jwtUtil.generateToken(client.getUuid(), client.getUsername());

        // 将token存入Redis，设置过期时间
        String redisKey = TOKEN_KEY_PREFIX + client.getUuid();
        redisUtil.set(redisKey, token, 24 * 60 * 60); // 24小时过期

        // 构建返回VO对象
        LoginVO loginVO = new LoginVO();
        loginVO.setUuid(client.getUuid());
        loginVO.setUsername(client.getUsername());
        loginVO.setToken(token);

        return loginVO;

    }

    /**
     * 用户名密码注册
     *
     * @param loginDto 登录DTO
     * @return 登录结果
     */
    @Override
    public Boolean registerByPassword(LoginDto loginDto) {
        String username = loginDto.getUsername();
        String password = loginDto.getPassword();
        String confirmPassword = loginDto.getConfirmPassword();
        String userInputCode = loginDto.getCode();
        String captchaId = loginDto.getCaptchaId(); // 获取验证码标识

        if (StringUtils.isBlank(captchaId) || StringUtils.isBlank(userInputCode)) {
            throw new BusinessException(LoginExceptionEnum.CAPTCHA_ID_EMPTY.getCode(), LoginExceptionEnum.CAPTCHA_ID_EMPTY.getMessage());
        }

        // 从Redis获取验证码
        String correctCode = (String) redisUtil.get("validate_code:" + captchaId);
        if (correctCode == null) {
            throw new BusinessException(LoginExceptionEnum.CAPTCHA_EXPIRED.getCode(), LoginExceptionEnum.CAPTCHA_EXPIRED.getMessage());
        }

        // 验证码不匹配
        if (!correctCode.equalsIgnoreCase(userInputCode)) {
            throw new BusinessException(LoginExceptionEnum.CAPTCHA_ERROR.getCode(), LoginExceptionEnum.CAPTCHA_ERROR.getMessage());
        }

        // 验证成功后，删除Redis中的验证码
        redisUtil.del("validate_code:" + captchaId);

        if (StringUtils.isBlank(username)) {
            throw new BusinessException(LoginExceptionEnum.USERNAME_EMPTY.getCode(), LoginExceptionEnum.USERNAME_EMPTY.getMessage());
        }
        //查询用户名是否重复
        LambdaQueryWrapper<Client> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Client::getUsername, username);
        if (clientMapper.selectOne(queryWrapper) != null) {
            throw new BusinessException(LoginExceptionEnum.USERNAME_HAS_EXISTED.getCode(), LoginExceptionEnum.USERNAME_HAS_EXISTED.getMessage());
        }

        if (StringUtils.isBlank(password)) {
            throw new BusinessException(LoginExceptionEnum.PASSWORD_EMPTY.getCode(), LoginExceptionEnum.PASSWORD_EMPTY.getMessage());
        }

        if(!password.equals(confirmPassword)){
            throw new BusinessException(LoginExceptionEnum.PASSWORD_ERROR.getCode(), LoginExceptionEnum.PASSWORD_ERROR.getMessage());
        }

        try {
            // 生成uuid
            String uuid = UuidUtil.generateUuid();
            Client client = new Client();
            client.setUuid(uuid);
            client.setUsername(username);
            client.setPassword(password);
            client.setStatus(1);
            client.setUpdateTime(new Date());
            clientMapper.insert(client);
            return true;
        } catch (Exception e) {
            throw new BusinessException(LoginExceptionEnum.REGISTER_FAILED.getCode(), LoginExceptionEnum.REGISTER_FAILED.getMessage());
       }
    }

    /**
     * 获取当前登录用户
     *
     * @param token 用户token
     * @return clientVO 当前登录用户
     */
    @Override
    public ClientVO getCurrentClient(String token) {
        if (StringUtils.isBlank(token)) {
            throw new BusinessException(LoginExceptionEnum.NOT_LOGIN.getCode(), LoginExceptionEnum.NOT_LOGIN.getMessage());
        }

        try {
            // 验证token
            if (!jwtUtil.validateToken(token)) {
                throw new BusinessException(LoginExceptionEnum.TOKEN_INVALID.getCode(), LoginExceptionEnum.TOKEN_INVALID.getMessage());
            }

            // 从token中获取uuid
            String uuid = jwtUtil.getUserUuidFromToken(token);

            // 验证Redis中的token
            String redisKey = TOKEN_KEY_PREFIX + uuid;
            String redisToken = (String) redisUtil.get(redisKey);
            if (redisToken == null || !redisToken.equals(token)) {
                throw new BusinessException(LoginExceptionEnum.TOKEN_EXPIRED.getCode(), LoginExceptionEnum.TOKEN_EXPIRED.getMessage());
            }

            // 查询用户信息
            LambdaQueryWrapper<Client> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Client::getUuid, uuid);
            Client client = clientMapper.selectOne(queryWrapper);

            // 转换为VO对象
            ClientVO clientVO = new ClientVO();
            BeanUtils.copyProperties(client, clientVO);
            return clientVO;
        } catch (Exception e) {
            log.error("获取当前用户信息失败", e);
            throw new BusinessException(LoginExceptionEnum.TOKEN_INVALID.getCode(), LoginExceptionEnum.TOKEN_INVALID.getMessage());
        }
    }

    /**
     * 退出登录
     *
     * @param token String
     */
    @Override
    public void logout(String token) {
        if(StringUtils.isBlank(token)){
            throw new BusinessException(LoginExceptionEnum.NOT_LOGIN.getCode(), LoginExceptionEnum.NOT_LOGIN.getMessage());
        }

        try{
            // 获取用户ID
            String userUuid = jwtUtil.getUserUuidFromToken(token);
            // 从Redis中删除token
            String redisKey = TOKEN_KEY_PREFIX + userUuid;
            redisUtil.del(redisKey);
        }catch (Exception e){
            throw new BusinessException(LoginExceptionEnum.TOKEN_INVALID.getCode(), LoginExceptionEnum.TOKEN_INVALID.getMessage());
        }
    }
}

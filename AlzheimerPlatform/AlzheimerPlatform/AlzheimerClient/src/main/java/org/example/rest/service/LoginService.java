package org.example.rest.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.domain.dto.LoginDto;
import org.example.domain.entity.Client;
import org.example.domain.vo.CaptchaVO;
import org.example.domain.vo.ClientVO;
import org.example.domain.vo.LoginVO;

/**
 * 登录服务接口
 *
 * @author lyx
 */
public interface LoginService extends IService<Client> {

    /**
     * 获取验证码
     *
     * @return 验证码VO
     */
    CaptchaVO getValidateCode();

    /**
     * 用户名密码登录
     *
     * @param loginDto 登录DTO
     * @return 登录结果VO
     */
    LoginVO loginByPassword(LoginDto loginDto);

    /**
     * 用户名密码注册
     *
     * @param loginDto 登录DTO
     * @return 登录结果
     */
    Boolean registerByPassword(LoginDto loginDto);

    /**
     * 获取当前登录用户
     *
     * @param token 用户token
     * @return 当前登录用户VO
     */
    ClientVO getCurrentClient(String token);

    /**
     * 退出登录
     *
     * @param token 用户token
     */
    void logout(String token);


}

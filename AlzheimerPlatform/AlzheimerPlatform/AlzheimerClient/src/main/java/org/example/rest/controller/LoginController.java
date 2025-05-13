package org.example.rest.controller;

import org.example.common.result.Result;
import org.example.domain.dto.LoginDto;
import org.example.rest.service.LoginService;
import org.example.domain.vo.CaptchaVO;
import org.example.domain.vo.ClientVO;
import org.example.domain.vo.LoginVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 登录控制器
 *
 * @author lyx
 */
@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private LoginService loginService;

    /**
     * 获取图片验证码
     *
     * @return 验证码图片和唯一标识
     */
    @GetMapping("/code")
    public Result<CaptchaVO> getValidateCode() {
        return Result.success(loginService.getValidateCode());
    }

    /**
     * 用户名密码登录
     *
     * @param loginDto 登录DTO
     * @return 登录结果
     */
    @PostMapping("/password")
    public Result<LoginVO> loginByPassword(@RequestBody LoginDto loginDto) {
        return Result.success(loginService.loginByPassword(loginDto));
    }
    /**
     * 用户名密码注册
     *
     * @param loginDto 登录DTO
     * @return 登录结果
     */
    @PostMapping("/register")
    public Result<?> register(@RequestBody LoginDto loginDto) {
        return Result.success(loginService.registerByPassword(loginDto));
    }


    /**
     * 获取当前登录用户
     *
     * @param token Token
     * @return 当前登录用户
     */
    @GetMapping("/current")
    public Result<ClientVO> getCurrentClient(@RequestHeader("Authorization") String token) {
        return Result.success(loginService.getCurrentClient(token));
    }

    /**
     * 退出登录
     *
     * @param token Token
     * @return 结果
     */
    @PostMapping("/logout")
    public Result<Void> logout(@RequestHeader("Authorization") String token) {
        loginService.logout(token);
        return Result.success();
    }
}
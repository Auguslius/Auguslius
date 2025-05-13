package org.example.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 登录DTO类
 *
 * @author lyx
 */
@Data
public class LoginDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
    * uuid
    */
    private String uuid;

    /**
     * 验证码唯一标识
     */
    private String captchaId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 密码确认
     */
    private String confirmPassword;

    /**
     * 图片验证码
     */
    private String code;


} 
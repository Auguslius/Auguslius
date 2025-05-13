package org.example.common.Enum;

import lombok.Getter;

/**
 * 登录异常枚举类
 *
 * @author admin
 */
@Getter
public enum LoginExceptionEnum {

    /**
     * 手机号为空
     */
    PHONE_EMPTY(4001, "手机号不能为空"),

    /**
     * 手机号格式错误
     */
    PHONE_FORMAT_ERROR(4002, "手机号格式错误"),

    /**
     * 账号不存在
     */
    ACCOUNT_NOT_EXIST(4003, "账号不存在"),

    /**
     * 短信验证码为空
     */
    SMS_CODE_EMPTY(4004, "短信验证码不能为空"),

    /**
     * 图片验证码为空
     */
    CODE_EMPTY(4005, "图片验证码不能为空"),

    /**
     * 图片验证码错误
     */
    CODE_ERROR(4006, "图片验证码错误"),

    /**
     * 短信验证码超时
     */
    SMS_CODE_TIMEOUT(4007, "短信验证码已过期"),

    /**
     * 短信验证码错误
     */
    SMS_CODE_ERROR(4008, "短信验证码错误"),

    /**
     * 用户未审核
     */
    UNCHECKED_USER(4009, "用户未审核"),

    /**
     * 用户已禁用
     */
    DISABLED_USER(4010, "用户已禁用"),

    /**
     * 用户审核失败
     */
    AUTH_FAILED(4011, "用户审核失败"),

    /**
     * 用户名为空
     */
    USERNAME_EMPTY(4012, "用户名不能为空"),

    /**
     * 密码为空
     */
    PASSWORD_EMPTY(4013, "密码不能为空"),

    /**
     * 用户名或密码错误
     */
    USERNAME_PASSWORD_ERROR(4014, "用户名或密码错误"),

    /**
     * 用户未登录
     */
    NOT_LOGIN(401, "用户未登录"),

    /**
     * token已失效
     */
    TOKEN_INVALID(401, "token已失效"),

    /**
     * token已过期
     */
    TOKEN_EXPIRED(401, "token已过期"),

    /**
     * 验证码id不能为空
     */
    CAPTCHA_ID_EMPTY(4018, "验证码id不能为空"),

    /**
     * 验证码过期
     */
    CAPTCHA_EXPIRED(4019, "验证码过期"),

    /**
     * 验证码错误
     */
    CAPTCHA_ERROR(4020, "验证码错误"),

    /**
     * 用户名已存在
     */
    USERNAME_HAS_EXISTED(4021, "用户名已存在"),

    /**
     * 密码错误
     */
    PASSWORD_ERROR(4022, "密码错误"),

    /**
     * 注册失败
     */
    REGISTER_FAILED(4023, "注册失败");



    private final Integer code;
    private final String message;

    LoginExceptionEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
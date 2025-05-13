package org.example.common.enums;

import lombok.Getter;

@Getter
public enum LoginEnum {

    LOGIN_SUCCESS(0, "登录成功"),
    LOGIN_FAIL(1, "登录失败"),
    LOGIN_USERNAME_ERROR(2, "用户名错误"),
    LOGIN_PASSWORD_ERROR(3, "密码错误"),
    LOGIN_USER_NOT_EXIST(4, "用户不存在"),
    LOGIN_USER_LOCKED(5, "用户被锁定");

    private Integer code;
    private String message;

    LoginEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}

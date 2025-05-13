package org.example.common.enums;

import lombok.Getter;

@Getter
public enum RegisterEnum {

    REGISTER_SUCCESS(0,"注册成功"),
    REGISTER_FAIL(1,"注册失败"),
    REGISTER_USERNAME_EXIST(2,"用户名已存在"),
    REGISTER_USERNAME_ERROR(3,"用户名错误"),
    REGISTER_PASSWORD_ERROR(4,"密码错误");

    private Integer code;
    private String message;
    RegisterEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}

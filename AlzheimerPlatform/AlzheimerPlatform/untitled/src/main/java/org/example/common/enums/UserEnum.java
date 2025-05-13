package org.example.common.enums;

import lombok.Getter;

@Getter
public enum UserEnum {

    LOGIN_SUCCESS(0,"登录成功"),
    LOGIN_FAIL(1,"登录失败"),
    UPDATE_SUCCESS(2,"更新成功"),
    UPDATE_FAIL(3,"更新失败"),
    UPDATE_NAME_HAS_EXISTED(4,"用户名重复"),
    UPDATE_START(5,"更新开始"),
    UPDATE_PASSWORD_ERROR(6,"密码错误"),
    UPDATE_PASSWORD_NOT_CONSISTENT(7,"新密码与确认密码不一致"),
    UPDATE_LACK_PARAMETER(8,"缺少参数"),
    DELETE_SUCCESS(9,"删除成功"),
    DELETE_FAIL(10,"删除失败"),
    ADD_SUCCESS(11,"添加成功"),
    ADD_FAIL(12,"添加失败"),
    USER_NOT_LOGIN(13,"用户未登录"),
    USER_NOT_EXIST(14,"用户不存在"),
    USER_HAS_EXISTED(15,"用户已存在"),
    USER_INACTIVE(16,"用户未激活"),
    USER_LOCKED(17,"用户被锁定"),
    LOGOUT_SUCCESS(18,"退出成功"),
    REGISTER_SUCCESS(19,"注册成功"),
    REGISTER_FAIL(20,"注册失败"),
    ;




    private Integer code;
    private String message;

    UserEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}

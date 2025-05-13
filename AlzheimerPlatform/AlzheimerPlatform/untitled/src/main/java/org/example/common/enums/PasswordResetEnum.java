package org.example.common.enums;

import lombok.Getter;

@Getter
public enum PasswordResetEnum {

    PASSWORD_RESET_SUCCESS(2, "密码重置成功"),
    PASSWORD_RESET_FAIL(3, "密码重置失败"),
    PASSWORD_RESET_REQUEST_SUCCESS(4, "密码请求成功"),
    PASSWORD_RESET_REQUEST_FAIL(5, "密码请求失败"),
    PASSWORD_VALIDATED_SUCCESS(6, "密码验证成功"),
    PASSWORD_VALIDATED_FAIL(7, "密码验证失败");


    private final Integer code;
    private final String message;
    PasswordResetEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}

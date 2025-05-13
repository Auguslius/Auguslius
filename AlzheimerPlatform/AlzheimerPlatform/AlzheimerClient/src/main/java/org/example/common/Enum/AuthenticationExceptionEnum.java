package org.example.common.Enum;

import lombok.Getter;

@Getter
public enum AuthenticationExceptionEnum {

    PATIENT_NOT_EXIST(4021,"患者不存在"),
    PATIENT_EXIST(4022,"患者已存在"),
    PATIENT_AUTHENTICATION_FAIL(4023,"患者认证失败"),
    PATIENT_AUTHENTICATION_SUCCESS(4024,"患者认证成功");

    private Integer code;

    private String message;

    AuthenticationExceptionEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

}

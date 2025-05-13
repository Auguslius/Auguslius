package org.example.common.Enum;

import lombok.Getter;

@Getter
public enum InstitutionDoctorEnum {

    DOCTOR_NOT_EXIST(4031,"医生不存在"),
    CLIENT_NOT_EXIST(4032,"患者不存在"),
    Patient_NOT_EXIST(4033,"患者未绑定，请前往绑定"),
    DOCTOR_ALREADY_BOUND(4034,"用户已绑定医生"),
    BIND_UPDATE_FAILED(4035,"其它绑定错误");


    private Integer code;

    private String message;

    InstitutionDoctorEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

}

package org.example.common.enums;

import lombok.Getter;

/**
 * 病历相关操作的枚举类
 */
@Getter
public enum MedicalRecordEnum {

    // 成功消息
    ADD_SUCCESS(0, "添加病历成功"),
    UPDATE_SUCCESS(0, "更新病历成功"),
    DELETE_SUCCESS(0, "删除病历成功"),
    QUERY_SUCCESS(0, "查询病历成功"),

    // 错误消息
    ADD_FAIL(1, "添加病历失败"),
    UPDATE_FAIL(1, "更新病历失败"),
    DELETE_FAIL(1, "删除病历失败"),
    QUERY_FAIL(1, "查询病历失败"),

    // 验证错误
    RECORD_UUID_EMPTY(2, "病历编号不能为空"),
    PATIENT_UUID_EMPTY(3, "患者编号不能为空"),
    RECORD_NOT_EXIST(4, "病历记录不存在"),
    INCOMPLETE_INFO(5, "病历信息不完整"),

    // 权限错误
    NO_PERMISSION(6, "无权操作该病历"),

    // 业务限制
    MAX_RECORDS_EXCEEDED(7, "患者病历数量已达上限");

    private final Integer code;
    private final String message;

    MedicalRecordEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
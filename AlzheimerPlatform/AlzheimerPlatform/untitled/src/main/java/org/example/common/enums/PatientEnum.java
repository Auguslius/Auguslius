package org.example.common.enums;

import lombok.Getter;

@Getter
public enum PatientEnum {

    ADD_SUCCESS(2,"添加成功"),
    ADD_FAIL(3,"添加失败"),
    UPDATE_SUCCESS(4,"更新成功"),
    UPDATE_FAIL(5,"更新失败"),
    DELETE_SUCCESS(6,"删除成功"),
    DELETE_FAIL(7,"删除失败"),
    QUERY_SUCCESS(8,"查询成功"),
    QUERY_FAIL(9,"查询失败"),
    DOCTOR_NO_SURPLUS(10,"医生剩余量不足")
    ;

    private Integer code;
    private String message;

    PatientEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
   }

}

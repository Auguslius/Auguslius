package org.example.common.enums;

import lombok.Getter;

@Getter
public enum MMSEQuestionEnum {

    URL_CATEGORY_ERROR(2,"URL错误");

    private Integer code;

    private  String message;

    MMSEQuestionEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}

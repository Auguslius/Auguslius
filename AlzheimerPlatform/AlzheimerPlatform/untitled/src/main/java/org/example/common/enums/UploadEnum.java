package org.example.common.enums;

import lombok.Getter;

@Getter
public enum UploadEnum {

    UPLOAD_SUCCESS(2,"上传成功"),

    UPLOAD_FAIL(3,"上传失败");

    private Integer code;

    private String message;

    UploadEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}

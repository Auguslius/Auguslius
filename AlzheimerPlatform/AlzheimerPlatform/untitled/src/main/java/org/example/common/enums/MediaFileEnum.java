package org.example.common.enums;

import lombok.Getter;

@Getter
public enum MediaFileEnum {

    UPLOAD_SUCCESS(0, "上传文件成功"),
    UPLOAD_FAIL(1, "上传文件失败"),
    GET_FILE_SUCCESS(2, "获取文件成功"),
    GET_FILE_FAIL(3, "获取文件失败"),
    GET_FILE_LIST_SUCCESS(4, "获取文件列表成功"),
    GET_FILE_LIST_FAIL(5, "获取文件列表失败"),
    DELETE_FILE_SUCCESS(6, "删除文件成功"),
    DELETE_FILE_FAIL(7, "删除文件失败"),
    EMPTY_FILE(8, "文件为空"),
    EMPTY_FILE_PATH(9, "文件路径为空"),
    EMPTY_FILE_TYPE(10, "文件类型为空"),
    EMPTY_FILE_SIZE(11, "文件大小为空"),
    EMPTY_FILE_URL(12, "文件URL为空"),
    EMPTY_FILE_CONTENT_TYPE(13, "文件Content-Type为空"),
    EMPTY_FILE_ORIGINAL_NAME(14, "文件原始名称为空");

    private final Integer code;
    private final String message;

    MediaFileEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

}
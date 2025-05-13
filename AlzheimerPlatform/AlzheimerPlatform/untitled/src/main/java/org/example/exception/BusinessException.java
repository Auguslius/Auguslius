package org.example.exception;

import lombok.Getter;
import org.example.common.enums.CommonEnum;

@Getter
public class BusinessException extends RuntimeException {

    private final int code; // 错误码
    private final String message; // 错误信息

    /**
     * 构造方法（带错误码和错误信息）
     */
    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    /**
     * 构造方法（仅带错误信息，默认错误码为失败）
     */
    public BusinessException(String message) {
        super(message);
        this.code = CommonEnum.FAIL.getCode(); // 默认错误码
        this.message = message;
    }


}
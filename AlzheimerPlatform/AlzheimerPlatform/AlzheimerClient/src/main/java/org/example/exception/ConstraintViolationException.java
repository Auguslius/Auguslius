package org.example.exception;


/**
 * 约束违反异常
 * 用于处理参数验证失败的情况
 */
public class ConstraintViolationException extends RuntimeException {

    private final Integer code;

    public ConstraintViolationException(String message) {
        super(message);
        this.code = 400;
    }

    public ConstraintViolationException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
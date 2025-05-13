package org.example.exception;

import io.netty.util.internal.StringUtil;
import org.example.common.result.Result;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 捕获所有异常
    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e) {

        e.printStackTrace();

        String errorMessage = StringUtils.hasLength(e.getMessage()) ? e.getMessage() : "操作失败";

        return Result.operateException(errorMessage);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Result<String>> handleBusinessException(BusinessException e) {
        // 返回 400 Bad Request 状态码
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new Result<>(e.getCode(), e.getMessage(), null));
    }

    /**
     * 处理数据库唯一约束异常（Duplicate entry）
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public Result handleSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException ex) {
        String duplicateField = extractDuplicateField(ex.getMessage());
        return Result.fail(1, duplicateField + " 已存在，请更换");
    }

    /**
     * 处理 Spring DataIntegrityViolationException（可能包含 SQLIntegrityConstraintViolationException）
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public Result handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        // 打印完整的异常信息
        ex.printStackTrace();

        // 检查是否是 SQL 约束违规异常
        Throwable cause = ex.getCause();
        if (cause instanceof SQLIntegrityConstraintViolationException) {
            SQLIntegrityConstraintViolationException sqlEx = (SQLIntegrityConstraintViolationException) cause;
            sqlEx.printStackTrace();
            return Result.fail(1, "数据库约束异常: " + sqlEx.getMessage());
        }

        return Result.fail(1, "数据库数据异常: " + ex.getMessage());
    }


    /**
     * 解析 `Duplicate entry 'xxx' for key 'institution.institution_pk_x'`
     */
    private String extractDuplicateField(String message) {
        if (message.contains("Duplicate entry")) {
            String[] parts = message.split("for key");
            if (parts.length > 1) {
                return parts[1].replaceAll("'", "").trim(); // 直接返回重复的 key 名
            }
        }
        return "数据"; // 如果无法解析，默认返回“数据”
    }

}
package org.example.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 验证码DTO类
 *
 * @author lyx
 */
@Data
public class ValidateDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Base64编码的图片
     */
    private String base64Str;

    /**
     * 验证码值
     */
    private String value;
} 
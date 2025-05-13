package org.example.domain.vo;

import lombok.Data;
import java.io.Serializable;

/**
 * 验证码响应VO
 *
 * @author lyx
 */
@Data
public class CaptchaVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 验证码唯一标识
     */
    private String captchaId;

    /**
     * 验证码值
     */
    private String code;

    /**
     * 验证码图片Base64
     */
    private String image;

    /**
     * 过期时间(秒)
     */
    private Integer expire;
}

package org.example.domain.vo;


import lombok.Data;
import java.io.Serializable;

/**
 * 登录响应VO
 *
 * @author lyx
 */
@Data
public class LoginVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户唯一标识
     */
    private String uuid;

    /**
     * 用户名
     */
    private String username;

    /**
     * 认证token
     */
    private String token;
}
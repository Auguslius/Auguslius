package org.example.domain.vo;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户信息VO
 *
 * @author lyx
 */
@Data
public class ClientVO implements Serializable {

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
     * 用户状态
     */
    private Integer status;

    /**
     * 上次登录时间
     */
    private Date lastLoginTime;

    /**
     * 登录次数
     */
    private Integer loginCount;

    /**
     * 手机
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 真名
     */
    private String realName;
    // 可以添加其他需要展示给前端的用户信息字段
}
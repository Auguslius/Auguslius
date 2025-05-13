package org.example.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@TableName("user_login")
public class UserLogin {

    /**
     * 用户登录ID:主键自增
     */
    @TableId(type = IdType.AUTO)
    @Schema(description = "用户登录ID:主键自增")
    private Integer id;

    /**
     * 用户ID:必填
     */
    @Schema(description = "用户ID:必填")
    private Integer userId;

    /**
     * 登录时间:非必填
     */
    @Schema(description = "登录时间:非必填")
    private java.util.Date loginTime;

    /**
     * 登出时间:非必填
     */
    @Schema(description = "登出时间:非必填")
    private java.util.Date logoutTime;

    /**
     * 登录IP地址:非必填
     */
    @Schema(description = "登录IP地址:非必填")
    private String ipAddress;

    /**
     * 登录状态:非必填
     */
    @Schema(description = "登录状态:非必填")
    private String status;

    /**
     * 用户名:必填
     */
    @Schema(description = "用户名:非必填")
    private String userName;

    /**
     * 创建时间:非必填
     */
    @Schema(description = "创建时间:非必填")
    private java.util.Date createTime;

    /**
     * 修改时间:非必填
     */
    @Schema(description = "修改时间:非必填")
    private java.util.Date updateTime;
}

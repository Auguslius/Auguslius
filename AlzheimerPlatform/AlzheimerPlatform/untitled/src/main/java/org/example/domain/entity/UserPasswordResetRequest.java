package org.example.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 密码重置请求实体类
 *
 * @author lenovo
 * @date 2025-03-13 14:35:32
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("password_reset_requests")
public class UserPasswordResetRequest {

    /**
     * 请求ID
     */
    @TableId(type = IdType.AUTO)
    @Schema(description = "请求ID")
    private Integer requestId;

    /**
     * 用户ID
     */
    @Schema(description = "用户ID")
    private Integer number;

    /**
     * 邮箱
     */
    @Schema(description = "邮箱")
    private String email;

    /**
     * 手机号
     */
    @Schema(description = "手机号")
    private String phone;

    /**
     * 重置密码令牌
     */
    @Schema(description = "重置密码令牌")
    private String token;

    /**
     * 请求时间
     */
    @Schema(description = "请求时间")
    private Date requestedTime;

    /**
     * 是否已使用
     */
    @Schema(description = "是否已使用")
    private Boolean used;

    /**
     * 创建人
     */
    @Schema(description = "创建人")
    private String createBy;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private Date createTime;

    /**
     * 修改人
     */
    @Schema(description = "修改人")
    private String updateBy;

    /**
     * 修改时间
     */
    @Schema(description = "修改时间")
    private Date updateTime;
}

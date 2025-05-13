package org.example.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@TableName("user_password_reset")
public class UserPasswordReset {
    /**
     * 密码重置ID
     */
    @TableId(type = IdType.AUTO)
    @Schema(description = "密码重置ID")
    private Integer id;

    /**
     * 用户ID
     */
    @Schema(description = "用户编号")
    private Integer number;

    /**
     * 密码重置令牌
     */
    @Schema(description = "密码重置令牌")
    private String resetToken;

    /**
     * 令牌过期时间
     */
    @Schema(description = "令牌过期时间")
    private Date tokenExpiration;

    /**
     * 重置方式
     */
    @Schema(description = "重置方式")
    private String resetMethod;

    /**
     * 创建人
     */
    @Schema(description = "创建人")
    private String createBy;

    /**
     * 修改人
     */
    @Schema(description = "修改人")
    private String updateBy;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private Date createTime;

    /**
     * 修改时间
     */
    @Schema(description = "修改时间")
    private Date updateTime;
}

package org.example.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.example.domain.group.CreateGroup;
import org.example.domain.group.QueryGroup;

import java.util.Date;


@Data
public class PasswordResetRequestDTO {

//    /**
//     * 用户ID
//     */
//    @NotNull(groups = { CreateGroup.class, QueryGroup.class }, message = "用户ID不能为空")
//    @Schema(description = "用户ID")
//    private Integer userId;

    /**
     * 邮箱
     */
    @Email(groups = { CreateGroup.class, QueryGroup.class }, message = "邮箱格式不正确")
    @Schema(description = "邮箱")
    private String email;

    /**
     * 手机号
     */
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", groups = { CreateGroup.class, QueryGroup.class }, message = "手机号格式不正确")
    @Schema(description = "手机号")
    private String phone;

//    /**
//     * 重置密码令牌
//     */
//    @NotBlank(groups = { CreateGroup.class, QueryGroup.class }, message = "令牌不能为空")
//    @Schema(description = "重置密码令牌")
//    private String token;

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

package org.example.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.example.domain.group.CreateGroup;

/**
 * 密码重置实体类
 */
@Data
public class PasswordResetDTO {

    /**
     * 重置密码令牌
     */
    @NotBlank(groups = { CreateGroup.class }, message = "令牌不能为空")
    @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$", groups = { CreateGroup.class }, message = "令牌格式不正确")
    @Schema(description = "重置密码令牌")
    private String token;

    /**
     * 新密码
     */
    @NotBlank(groups = { CreateGroup.class }, message = "新密码不能为空")
    @Schema(description = "新密码")
    private String newPassword;
}

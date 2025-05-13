package org.example.domain.query;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.example.domain.group.QueryGroup;

/**
 * 密码重置请求查询实体类
 *
 * @author lenovo
 * @date 2025-03-13 14:35:32
 */
@Data
public class PasswordResetRequestQuery {

    /**
     * 重置密码令牌
     */
    @NotBlank(groups = { QueryGroup.class }, message = "令牌不能为空")
    @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$", groups = { QueryGroup.class }, message = "令牌格式不正确")
    @Schema(description = "重置密码令牌")
    private String token;
}

package org.example.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.example.domain.group.QueryGroup;

@Data
public class LogoutDTO {

    /**
     * 用户ID:必填
     */
    @NotNull(groups = { QueryGroup.class }, message = "用户Number不能为空")
    @Schema(description = "用户Number:必填")
    private Integer number;
}
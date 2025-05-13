package org.example.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data
@Schema(description = "机构信息传输对象")
public class InstitutionDTO {

    @Schema(description = "机构 ID")
    private String uuid;

    @NotBlank(message = "机构名称不能为空")
    @Size(max = 255, message = "机构名称长度不能超过 255 个字符")
    @Schema(description = "机构名称")
    private String institutionName;

    @NotBlank(message = "机构电话不能为空")
    @Size(max = 255, message = "机构电话长度不能超过 255 个字符")
    @Pattern(regexp = "^\\+?[0-9]*$", message = "机构电话只能包含数字和可选的前缀 '+'")
    @Schema(description = "机构电话")
    private String institutionPhone;

    @Size(max = 512, message = "详细地址长度不能超过 512 个字符")
    @Schema(description = "详细地址")
    private String address;

    @Min(value = 1, message = "机构种类 ID 必须大于 0")
    @Schema(description = "机构种类 ID")
    private int institutionCategoryId;

    @Min(value = 1, message = "机构级别必须大于 0")
    @Max(value = 5, message = "机构级别最大为 5")
    @Schema(description = "机构级别")
    private int institutionLevel;

    @NotNull(message = "状态不能为空")
    @Range(min = 0, max = 2, message = "状态必须在 0 到 2 之间")
    @Schema(description = "状态")
    private int status;


}
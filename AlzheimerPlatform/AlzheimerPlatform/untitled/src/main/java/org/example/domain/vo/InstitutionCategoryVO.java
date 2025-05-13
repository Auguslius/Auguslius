package org.example.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Schema(description = "机构种类VO")
public class InstitutionCategoryVO {
    @Schema(description = "ID")
    private Integer id;

    @Schema(description = "种类名称")
    private String categoryName;

    @Schema(description = "种类别名")
    private String categoryAlias;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @Schema(description = "层级")
    private Integer level;

    @Schema(description = "层级名称")
    private String levelName;

    @Schema(description = "创建人")
    private String createUser;

    @Schema(description = "备注")
    private String remark;

}
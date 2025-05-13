package org.example.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "机构种类实体")
@TableName("institution_category")
public class InstitutionCategory {
    @NotNull(message = "ID 不能为空")
    @Schema(description = "ID")
    private Integer id;
    @Schema(description = "种类名称")
    @NotNull(message = "种类名称不能为空")
    private String categoryName;
    @Schema(description = "种类别名")
    private String categoryAlias;
    @Schema(description = "创建人")
    @NotNull(message = "创建人不能为空")
    private Integer createUser;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
    @Schema(description = "层级")
    private Integer level;
    @Schema(description = "层级名称")
    private String levelName;
    @Schema(description = "备注")
    private String remark;
}

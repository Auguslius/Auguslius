package org.example.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "层级信息VO")
public class InstitutionCategoryLevelVO {

    @Schema(description = "层级数")
    private Integer count;

    @Schema(description = "层级列表")
    private List<InstitutionCategoryLevelListVO> levelList;
}

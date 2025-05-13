package org.example.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
class InstitutionCategoryLevelListDTO {

    @Schema(description = "层级")
    private Integer level;

    @Schema(description = "层级名称")
    private String levelName;
}

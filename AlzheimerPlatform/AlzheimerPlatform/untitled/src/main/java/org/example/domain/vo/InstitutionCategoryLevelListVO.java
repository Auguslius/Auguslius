package org.example.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "层级信息表VO")
public class InstitutionCategoryLevelListVO {

    private int level;

    private String levelName;
}

package org.example.domain.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "机构查询条件实体")
public class InstitutionQuery extends PageQuery{

    @Schema(description = "机构 uuid")
    private String uuid;

    @Schema(description = "机构名")
    private String institutionName;

    @Schema(description = "机构级别")
    private Integer InstitutionLevel;

    private Integer pageSize = 5;
}

package org.example.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "患者数量统计VO")
@Data
public class PatientCountVO {

    @Schema(description = "男性数量")
    private int maleCount;

    @Schema(description = "女性数量")
    private int femaleCount;

    @Schema(description = "总数")
    private int totalCount;

}

package org.example.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


import java.util.Map;

@Data
@Schema(description = "MMSE患者答案打分请求")
public class MMSEAnswerScoreDTO {

    @NotBlank(message = "患者UUID不能为空")
    @Schema(description = "患者的唯一标识符", requiredMode = Schema.RequiredMode.REQUIRED)
    private String patientUuid;
    
    @NotBlank(message = "答案UUID不能为空")
    @Schema(description = "答案的唯一标识符", requiredMode = Schema.RequiredMode.REQUIRED)
    private String mmseAnswerUuid;
    
    @NotNull(message = "分数不能为空")
    @Schema(description = "存储题目得分，格式为Map<问题position, 分数>", requiredMode = Schema.RequiredMode.REQUIRED)
    private Map<String, Integer> scoreMap;
} 
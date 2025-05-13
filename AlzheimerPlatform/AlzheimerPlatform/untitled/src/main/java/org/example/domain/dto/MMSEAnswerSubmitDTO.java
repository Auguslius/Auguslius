package org.example.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
@Schema(description = "MMSE患者答案提交请求")
public class MMSEAnswerSubmitDTO {

    @NotBlank(message = "患者UUID不能为空")
    @Schema(description = "患者的唯一标识符", requiredMode = Schema.RequiredMode.REQUIRED)
    private String patientUuid;
    
    @NotNull(message = "答案内容不能为空")
    @Schema(description = "存储患者的答案，格式为Map<问题position, Map<属性名, 属性值>>", requiredMode = Schema.RequiredMode.REQUIRED)
    private Map<String, Map<String, Object>> answersMap;

    @Schema(description = "答案提交时间")
    private Date answerTime;
} 
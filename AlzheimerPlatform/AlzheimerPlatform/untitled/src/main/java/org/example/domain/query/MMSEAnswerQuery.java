package org.example.domain.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "MMSE答案查询条件实体")
public class MMSEAnswerQuery extends PageQuery {
    
    @Schema(description = "患者的唯一标识符")
    private String patientUuid;
    
    @Schema(description = "患者姓名，模糊匹配")
    private String patientName;
    
    @Schema(description = "最小得分，用于范围查询")
    private Integer minScore;
    
    @Schema(description = "最大得分，用于范围查询")
    private Integer maxScore;
    
    @Schema(description = "答案提交时间开始，用于时间范围查询")
    private Date answerTimeStart;
    
    @Schema(description = "答案提交时间结束，用于时间范围查询")
    private Date answerTimeEnd;
    
    @Schema(description = "认知状态（正常/轻度认知障碍/中度认知障碍/重度认知障碍）")
    private String cognitiveStatus;
} 
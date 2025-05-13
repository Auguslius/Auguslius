package org.example.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
@Schema(description = "MMSE患者答案响应视图")
public class MMSEAnswerVO {
    
    @Schema(description = "答案的唯一标识符（UUID）")
    private String mmseAnswerUuid;
    
    @Schema(description = "患者的唯一标识符")
    private String patientUuid;
    
    @Schema(description = "患者姓名")
    private String patientName;
    
    @Schema(description = "存储患者的答案，格式为Map<问题position, 答案>")
    private Map<String, Object> answersMap;
    
    @Schema(description = "每题得分 Map<问题position, 得分>")
    private Map<String, Integer> scoreMap;
    
    @Schema(description = "总得分")
    private Integer totalScore;
    
    @Schema(description = "认知状态评估", example = "正常/轻度认知障碍/中度认知障碍/重度认知障碍")
    private String cognitiveStatus;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "答案提交时间")
    private Date answerTime;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间")
    private Date createTime;
} 
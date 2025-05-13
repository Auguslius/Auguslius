package org.example.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Schema(description = "MMSE答案详情视图")
public class MMSEAnswerDetailVO {
    
    @Schema(description = "答案的唯一标识符（UUID）")
    private String mmseAnswerUuid;
    
    @Schema(description = "患者的唯一标识符")
    private String patientUuid;
    
    @Schema(description = "患者姓名")
    private String patientName;
    
    @Schema(description = "总得分")
    private Integer totalScore;
    
    @Schema(description = "认知状态评估", example = "正常/轻度认知障碍/中度认知障碍/重度认知障碍")
    private String cognitiveStatus;
    
    @Schema(description = "问题答案列表")
    private List<QuestionAnswerItem> questionAnswers;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "答案提交时间")
    private Date answerTime;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间")
    private Date createTime;
    
    @Data
    @Schema(description = "问题答案项")
    public static class QuestionAnswerItem {
        
        @Schema(description = "问题ID")
        private Integer questionId;
        
        @Schema(description = "问题位置")
        private Integer position;
        
        @Schema(description = "所属大项")
        private String section;
        
        @Schema(description = "问题内容")
        private String questionText;
        
        @Schema(description = "问题类型")
        private String questionType;
        
        @Schema(description = "患者答案")
        private Object answer;
        
        @Schema(description = "期望的答案")
        private String expectedAnswer;
        
        @Schema(description = "得分")
        private Integer score;
        
        @Schema(description = "最高分")
        private Integer maxScore;
    }
} 
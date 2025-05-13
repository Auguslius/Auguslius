package org.example.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
@TableName("mmse_answers")
@Schema(description = "MMSE患者答案及得分实体")
public class MMSEAnswer {
    
    @Schema(description = "答案的唯一标识符（UUID）")
    private String mmseAnswerUuid;
    
    @Schema(description = "患者的唯一标识符")
    private String patientUuid;
    
    @Schema(description = "存储患者的答案，JSON 格式")
    private String answersJson;
    
    @Schema(description = "每题得分 JSON 格式 {\"position1\": 1, \"position2\": 0, ...}")
    private String score;
    
    @Schema(description = "总得分")
    private Integer totalScore;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "答案提交时间")
    private Date answerTime;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间")
    private Date createTime;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新时间")
    private Date updateTime;
} 
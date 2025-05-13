package org.example.common.Enum;

import lombok.Getter;

/**
 * MMSE答案相关枚举
 */
@Getter
public enum MMSEAnswerEnum {

    // 错误码
    UUID_EMPTY_ERROR(1001, "MMSE答案UUID不能为空"),
    PATIENT_UUID_EMPTY_ERROR(1002, "患者UUID不能为空"),
    ANSWERS_EMPTY_ERROR(1003, "患者答案不能为空"),
    JSON_PARSE_ERROR(1004, "JSON解析错误"),
    SAVE_ERROR(1005, "保存答案失败"),
    ANSWER_UUID_EMPTY_ERROR(1006, "MMSE答案UUID不能为空"),
    SCORE_EMPTY_ERROR(1007, "分数不能为空"),
    ANSWER_NOT_FOUND_ERROR(1008, "找不到对应的MMSE答案"),
    PATIENT_ANSWER_MISMATCH_ERROR(1009, "患者UUID与答案不匹配"),
    UPDATE_ERROR(1010, "更新答案失败"),
    SCORE_ERROR(1011, "评分失败"),
    
    // 认知状态评估标准
    COGNITIVE_NORMAL(2001, "正常", 27, 30),
    COGNITIVE_MILD(2002, "轻度认知障碍", 21, 26),
    COGNITIVE_MODERATE(2003, "中度认知障碍", 10, 20),
    COGNITIVE_SEVERE(2004, "重度认知障碍", 0, 9);

    private final Integer code;
    private final String message;
    private Integer minScore;
    private Integer maxScore;

    /**
     * 错误类型枚举构造函数
     */
    MMSEAnswerEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 认知状态评估标准枚举构造函数
     */
    MMSEAnswerEnum(Integer code, String message, Integer minScore, Integer maxScore) {
        this.code = code;
        this.message = message;
        this.minScore = minScore;
        this.maxScore = maxScore;
    }

    /**
     * 根据分数获取认知状态
     */
    public static String getCognitiveStatusByScore(Integer score) {
        if (score == null) {
            return null;
        }
        
        for (MMSEAnswerEnum status : new MMSEAnswerEnum[] {
                COGNITIVE_NORMAL, COGNITIVE_MILD, COGNITIVE_MODERATE, COGNITIVE_SEVERE
        }) {
            if (score >= status.getMinScore() && score <= status.getMaxScore()) {
                return status.getMessage();
            }
        }
        
        return null;
    }
} 
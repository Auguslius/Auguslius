package org.example.rest.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.domain.entity.MMSEAnswer;
import org.example.domain.dto.MMSEAnswerScoreDTO;
import org.example.domain.dto.MMSEAnswerSubmitDTO;

import java.util.Map;

/**
 * MMSE答案服务接口
 */
public interface MMSEAnswerService extends IService<MMSEAnswer> {
    
    /**
     * 提交答案（只保存，不计算得分）
     * @param mmseAnswerDTO 答案DTO
     */
    void submitAnswer(MMSEAnswerSubmitDTO mmseAnswerDTO);


    void scoreAnswer(MMSEAnswerScoreDTO mmseAnswerScoreDTO);

    Map<String, Map<String, Object>> getQuestionAndAnswer(String patientUuid);
    
    /**
     * 获取所有患者的MMSE答案
     * 
     * @return 患者UUID到答案Map的映射
     */
    Map<String, Map<String, Object>> getAllPatientsAnswers();
    
    /**
     * 获取MMSE分数的分布情况
     * 
     * @return 不同分数区间的患者数量统计
     */
    Map<String, Integer> getScorePercentage();
}
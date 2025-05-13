package org.example.rest.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.example.common.Enum.MMSEAnswerEnum;
import org.example.exception.BusinessException;
import org.example.domain.entity.MMSEAnswer;
import org.example.domain.entity.Patient;

import org.example.domain.dto.MMSEAnswerScoreDTO;
import org.example.domain.dto.MMSEAnswerSubmitDTO;
import org.example.rest.mapper.MMSEAnswerMapper;
import org.example.rest.service.MMSEAnswerService;
import org.example.web.rest.mapper.PatientMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class MMSEAnswerServiceImpl extends ServiceImpl<MMSEAnswerMapper, MMSEAnswer> implements MMSEAnswerService {

    private static final Logger log = LoggerFactory.getLogger(MMSEAnswerServiceImpl.class);
    
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MMSEAnswerMapper mmseAnswerMapper;

    @Autowired
    private PatientMapper patientMapper;

    private static final String PATIENT_UUID = "uuid";

    @Override
    @Transactional
    public void submitAnswer(MMSEAnswerSubmitDTO mmseAnswerSubmitDTO) {
        try {
            // 验证参数
            validatePatientUuid(mmseAnswerSubmitDTO.getPatientUuid());
            
            if (mmseAnswerSubmitDTO.getAnswersMap() == null || mmseAnswerSubmitDTO.getAnswersMap().isEmpty()) {
                throw new BusinessException(MMSEAnswerEnum.ANSWERS_EMPTY_ERROR.getCode(), 
                        MMSEAnswerEnum.ANSWERS_EMPTY_ERROR.getMessage());
            }
            
            // 验证每个答案的内部Map是否为空
            for (Map.Entry<String, Map<String, Object>> entry : mmseAnswerSubmitDTO.getAnswersMap().entrySet()) {
                if (entry.getValue() == null || entry.getValue().isEmpty()) {
                    throw new BusinessException(MMSEAnswerEnum.ANSWERS_EMPTY_ERROR.getCode(),
                            "题目 " + entry.getKey() + " 的答案内容为空");
                }
                
                // 确保每个答案至少包含"value"字段
                if (!entry.getValue().containsKey("value")) {
                    throw new BusinessException(MMSEAnswerEnum.ANSWERS_EMPTY_ERROR.getCode(),
                            "题目 " + entry.getKey() + " 的答案缺少必要的'value'字段");
                }
            }
            
            // 创建MMSEAnswer实体
            MMSEAnswer mmseAnswer = new MMSEAnswer();
            
            // 设置UUID
            mmseAnswer.setMmseAnswerUuid(UUID.randomUUID().toString());
            
            // 设置患者ID
            mmseAnswer.setPatientUuid(mmseAnswerSubmitDTO.getPatientUuid());
            
            try {
                // 将答案转为JSON保存
                mmseAnswer.setAnswersJson(objectMapper.writeValueAsString(mmseAnswerSubmitDTO.getAnswersMap()));
            } catch (Exception e) {
                log.error("JSON解析错误", e);
                throw new BusinessException(MMSEAnswerEnum.JSON_PARSE_ERROR.getCode(), 
                        MMSEAnswerEnum.JSON_PARSE_ERROR.getMessage());
            }
            
            // 分数和总分设为null，不进行计算
            mmseAnswer.setScore(null);
            mmseAnswer.setTotalScore(null);

            // 保存到数据库
            boolean saved = save(mmseAnswer);
            if (!saved) {
                throw new BusinessException(MMSEAnswerEnum.SAVE_ERROR.getCode(), 
                        MMSEAnswerEnum.SAVE_ERROR.getMessage());
            }
            
        } catch (BusinessException e) {
            // 业务异常直接抛出
            throw e;
        } catch (Exception e) {
            log.error("提交MMSE答案失败", e);
            throw new BusinessException(MMSEAnswerEnum.SAVE_ERROR.getCode(), 
                    MMSEAnswerEnum.SAVE_ERROR.getMessage() + ": " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void scoreAnswer(MMSEAnswerScoreDTO mmseAnswerScoreDTO) {
        try {
            // 验证患者UUID和答案UUID
            validatePatientUuid(mmseAnswerScoreDTO.getPatientUuid());
            // 获取并验证答案记录
            MMSEAnswer mmseAnswer = validateMmseAnswerUuid(mmseAnswerScoreDTO.getMmseAnswerUuid());
            
            // 验证患者与答案是否匹配
            if (!mmseAnswer.getPatientUuid().equals(mmseAnswerScoreDTO.getPatientUuid())) {
                throw new BusinessException(MMSEAnswerEnum.PATIENT_ANSWER_MISMATCH_ERROR.getCode(), 
                        MMSEAnswerEnum.PATIENT_ANSWER_MISMATCH_ERROR.getMessage());
            }
            
            try {
                // 将分数map转为JSON字符串
                String scoreJson = objectMapper.writeValueAsString(mmseAnswerScoreDTO.getScoreMap());
                mmseAnswer.setScore(scoreJson);
                
                // 计算总分
                int totalScore = 0;
                for (Integer score : mmseAnswerScoreDTO.getScoreMap().values()) {
                    if (score != null) {
                        totalScore += score;
                    }
                }
                mmseAnswer.setTotalScore(totalScore);
                
                // 更新数据库
                QueryWrapper<MMSEAnswer> updateWrapper = new QueryWrapper<>();
                updateWrapper.eq("mmse_answer_uuid", mmseAnswer.getMmseAnswerUuid());
                boolean updated = update(mmseAnswer, updateWrapper);
                
                if (!updated) {
                    throw new BusinessException(MMSEAnswerEnum.UPDATE_ERROR.getCode(), 
                            MMSEAnswerEnum.UPDATE_ERROR.getMessage());
                }
                
                log.info("MMSE答案评分成功，患者UUID: {}, 答案UUID: {}, 总分: {}", 
                        mmseAnswerScoreDTO.getPatientUuid(), mmseAnswerScoreDTO.getMmseAnswerUuid(), totalScore);
                
            } catch (Exception e) {
                log.error("JSON解析错误", e);
                throw new BusinessException(MMSEAnswerEnum.JSON_PARSE_ERROR.getCode(), 
                        MMSEAnswerEnum.JSON_PARSE_ERROR.getMessage());
            }
        } catch (BusinessException e) {
            // 业务异常直接抛出
            throw e;
        } catch (Exception e) {
            log.error("MMSE答案评分失败", e);
            throw new BusinessException(MMSEAnswerEnum.SCORE_ERROR.getCode(), 
                    MMSEAnswerEnum.SCORE_ERROR.getMessage() + ": " + e.getMessage());
        }
    }

    @Override
    public Map<String, Map<String, Object>> getQuestionAndAnswer(String patientUuid) {
        // 验证患者UUID
        validatePatientUuid(patientUuid);
        
        try {
            // 获取该患者的MMSE答案记录
            MMSEAnswer latestAnswer = mmseAnswerMapper.selectOne(
                new QueryWrapper<MMSEAnswer>()
                    .eq("patient_uuid", patientUuid)
                    .orderByDesc("answer_time")
                    .last("LIMIT 1")
            );
            
            // 如果没有找到答案记录，返回空Map
            if (latestAnswer == null || latestAnswer.getAnswersJson() == null || latestAnswer.getAnswersJson().isEmpty()) {
                return new HashMap<>();
            }
            
            // 直接解析并返回答案JSON
            try {
                return objectMapper.readValue(
                    latestAnswer.getAnswersJson(), 
                    new TypeReference<Map<String, Map<String, Object>>>() {}
                );
            } catch (Exception e) {
                log.error("解析JSON失败", e);
                throw new BusinessException(MMSEAnswerEnum.JSON_PARSE_ERROR.getCode(), 
                        MMSEAnswerEnum.JSON_PARSE_ERROR.getMessage());
            }
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("获取问题和答案失败", e);
            throw new BusinessException(MMSEAnswerEnum.JSON_PARSE_ERROR.getCode(), 
                    MMSEAnswerEnum.JSON_PARSE_ERROR.getMessage() + ": " + e.getMessage());
        }
    }

    @Override
    public Map<String, Map<String, Object>> getAllPatientsAnswers() {
        try {
            // 获取所有患者的最新MMSE答案记录
            List<MMSEAnswer> allAnswers = mmseAnswerMapper.selectList(
                new QueryWrapper<MMSEAnswer>()
                    .select("DISTINCT patient_uuid, mmse_answer_uuid, answers_json, score, total_score, answer_time")
                    .orderByDesc("answer_time")
            );
            
            // 按患者UUID分组，只保留每个患者的最新记录
            Map<String, MMSEAnswer> latestAnswersByPatient = new HashMap<>();
            for (MMSEAnswer answer : allAnswers) {
                String patientUuid = answer.getPatientUuid();
                if (!latestAnswersByPatient.containsKey(patientUuid) || 
                    answer.getAnswerTime().after(latestAnswersByPatient.get(patientUuid).getAnswerTime())) {
                    latestAnswersByPatient.put(patientUuid, answer);
                }
            }
            
            // 构建患者UUID到答案内容的映射
            Map<String, Map<String, Object>> result = new HashMap<>();
            for (Map.Entry<String, MMSEAnswer> entry : latestAnswersByPatient.entrySet()) {
                String patientUuid = entry.getKey();
                MMSEAnswer answer = entry.getValue();
                
                // 为每个患者创建一个答案元数据Map
                Map<String, Object> patientAnswerData = new HashMap<>();
                patientAnswerData.put("mmseAnswerUuid", answer.getMmseAnswerUuid());
                patientAnswerData.put("answerTime", answer.getAnswerTime());
                patientAnswerData.put("totalScore", answer.getTotalScore());
                
                if (answer.getAnswersJson() != null && !answer.getAnswersJson().isEmpty()) {
                    try {
                        // 解析答案JSON，但不直接放入结果Map
                        Map<String, Map<String, Object>> answerDetails = objectMapper.readValue(
                            answer.getAnswersJson(), 
                            new TypeReference<Map<String, Map<String, Object>>>() {}
                        );
                        
                        // 将答案详情添加到患者答案数据中
                        patientAnswerData.put("answerDetails", answerDetails);
                    } catch (Exception e) {
                        log.error("解析患者 {} 的答案JSON失败", patientUuid, e);
                        patientAnswerData.put("error", "答案数据格式错误");
                    }
                } else {
                    patientAnswerData.put("info", "无答案数据");
                }
                
                // 将处理后的患者答案数据添加到结果Map
                result.put(patientUuid, patientAnswerData);
            }
            
            return result;
        } catch (Exception e) {
            log.error("获取所有患者MMSE答案失败", e);
            throw new BusinessException(MMSEAnswerEnum.JSON_PARSE_ERROR.getCode(),
                    "获取所有患者MMSE答案失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Integer> getScorePercentage() {
        Map<String, Integer> scorePercentage = new HashMap<>();
        
        try {
            // 初始化各分数区间的计数
            scorePercentage.put("正常(27-30分)", 0);
            scorePercentage.put("轻度认知障碍(21-26分)", 0);
            scorePercentage.put("中度认知障碍(10-20分)", 0);
            scorePercentage.put("重度认知障碍(0-9分)", 0);
            
            // 查询所有有评分的MMSE答案记录
            List<MMSEAnswer> allScoredAnswers = mmseAnswerMapper.selectList(
                new QueryWrapper<MMSEAnswer>()
                    .isNotNull("total_score")
            );
            
            // 获取每个患者的最新记录
            Map<String, MMSEAnswer> latestAnswersByPatient = new HashMap<>();
            for (MMSEAnswer answer : allScoredAnswers) {
                String patientUuid = answer.getPatientUuid();
                if (!latestAnswersByPatient.containsKey(patientUuid) || 
                    answer.getAnswerTime().after(latestAnswersByPatient.get(patientUuid).getAnswerTime())) {
                    latestAnswersByPatient.put(patientUuid, answer);
                }
            }
            
            // 统计各分数区间的患者数量
            for (MMSEAnswer answer : latestAnswersByPatient.values()) {
                Integer totalScore = answer.getTotalScore();
                if (totalScore != null) {
                    if (totalScore >= 27 && totalScore <= 30) {
                        scorePercentage.put("正常(27-30分)", scorePercentage.get("正常(27-30分)") + 1);
                    } else if (totalScore >= 21 && totalScore <= 26) {
                        scorePercentage.put("轻度认知障碍(21-26分)", scorePercentage.get("轻度认知障碍(21-26分)") + 1);
                    } else if (totalScore >= 10 && totalScore <= 20) {
                        scorePercentage.put("中度认知障碍(10-20分)", scorePercentage.get("中度认知障碍(10-20分)") + 1);
                    } else if (totalScore >= 0 && totalScore <= 9) {
                        scorePercentage.put("重度认知障碍(0-9分)", scorePercentage.get("重度认知障碍(0-9分)") + 1);
                    }
                }
            }
            
            log.info("MMSE分数分布统计完成：{}", scorePercentage);
            
        } catch (Exception e) {
            log.error("统计MMSE分数分布失败", e);
            // 发生异常时返回空的统计结果
            scorePercentage.clear();
            scorePercentage.put("统计失败", 0);
        }
        
        return scorePercentage;
    }

    /**
     * 验证患者UUID是否有效
     * 
     * @param patientUuid 患者UUID
     * @throws BusinessException 如果UUID为空或患者不存在
     */
    private void validatePatientUuid(String patientUuid) {
        if (patientUuid == null || patientUuid.isEmpty()) {
            throw new BusinessException(MMSEAnswerEnum.PATIENT_UUID_EMPTY_ERROR.getCode(),
                    MMSEAnswerEnum.PATIENT_UUID_EMPTY_ERROR.getMessage());
        }
        QueryWrapper<Patient> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(PATIENT_UUID, patientUuid);
        log.info("验证患者UUID是否有效，UUID: {}", patientUuid);
        if (patientMapper.selectOne(queryWrapper) == null){
            throw new BusinessException(MMSEAnswerEnum.PATIENT_UUID_EMPTY_ERROR.getCode(),
                    MMSEAnswerEnum.PATIENT_UUID_EMPTY_ERROR.getMessage());
        }
    }

    /**
     * 验证MMSE答案UUID是否有效
     * 
     * @param mmseAnswerUuid MMSE答案UUID
     * @return 答案记录对象
     * @throws BusinessException 如果答案UUID为空或答案不存在
     */
    private MMSEAnswer validateMmseAnswerUuid(String mmseAnswerUuid) {
        if (mmseAnswerUuid == null || mmseAnswerUuid.isEmpty()) {
            throw new BusinessException(MMSEAnswerEnum.ANSWER_UUID_EMPTY_ERROR.getCode(), 
                    MMSEAnswerEnum.ANSWER_UUID_EMPTY_ERROR.getMessage());
        }
        
        QueryWrapper<MMSEAnswer> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mmse_answer_uuid", mmseAnswerUuid);
        MMSEAnswer mmseAnswer = mmseAnswerMapper.selectOne(queryWrapper);
        if (mmseAnswer == null) {
            throw new BusinessException(MMSEAnswerEnum.ANSWER_NOT_FOUND_ERROR.getCode(),
                    MMSEAnswerEnum.ANSWER_NOT_FOUND_ERROR.getMessage());
        }
        return mmseAnswer;
    }

} 
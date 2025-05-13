package org.example.rest.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.common.result.Result;

import org.example.domain.dto.MMSEAnswerScoreDTO;
import org.example.domain.dto.MMSEAnswerSubmitDTO;
import org.example.rest.service.MMSEAnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/mmse-answers")
@Tag(name = "MMSE答案管理接口", description = "提交患者MMSE测试答案")
public class MMSEAnswerController {

    @Autowired
    private MMSEAnswerService mmseAnswerService;

    @Operation(summary = "提交MMSE答案")
    @PostMapping("/submit")
    public Result<Void> submitAnswer(@RequestBody @Validated MMSEAnswerSubmitDTO mmseAnswerSubmitDTO) {
        // 调用服务只保存答案，不进行批改
        mmseAnswerService.submitAnswer(mmseAnswerSubmitDTO);
        return Result.success();
    }

    @Operation(summary = "批改MMSE答案")
    @PostMapping("/score")
    public Result<Void> scoreAnswer(@RequestBody @Validated MMSEAnswerScoreDTO mmseAnswerScoreDTO) {
        mmseAnswerService.scoreAnswer(mmseAnswerScoreDTO);
        return Result.success();
    }

    @Operation(summary = "获取患者MMSE答案")
    @GetMapping("/getAnswer/{patientUuid}")
    public Result<Map<String, Map<String, Object>>> getQuestionAndAnswer(@PathVariable String patientUuid) {
        Map<String, Map<String, Object>> mmseAnswer = mmseAnswerService.getQuestionAndAnswer(patientUuid);
        return Result.success(mmseAnswer);
    }

    @Operation(summary = "获取所有患者MMSE答案")
    @GetMapping("/getAllAnswer")
    public Result<Map<String, Map<String, Object>>> getAllQuestionAndAnswer() {
        Map<String, Map<String, Object>> allAnswers = mmseAnswerService.getAllPatientsAnswers();
        return Result.success(allAnswers);
    }

    @Operation(summary = "获取MMSE分数分布统计")
    @GetMapping("/scoreDistribution")
    public Result<Map<String,Integer>> getScorePercentage(){
        Map<String,Integer> scorePercentage = mmseAnswerService.getScorePercentage();
        return Result.success(scorePercentage);
    }
} 
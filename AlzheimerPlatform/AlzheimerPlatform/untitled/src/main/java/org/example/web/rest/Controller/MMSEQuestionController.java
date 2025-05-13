package org.example.web.rest.Controller;

import io.swagger.v3.oas.annotations.Operation;
import org.example.common.enums.CommonEnum;
import org.example.common.result.Result;
import org.example.domain.entity.MMSEQuestion;
import org.example.domain.dto.MMSEQuestionDTO;
import org.example.domain.dto.PageDTO;
import org.example.domain.query.MMSEQuestionQuery;
import org.example.domain.vo.MMSEQuestionCategoryVO;
import org.example.domain.vo.MMSEQuestionShowVO;
import org.example.domain.vo.MMSEQuestionVO;
import org.example.web.rest.service.MMSEQuestionService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static cn.hutool.core.date.DateTime.now;

@RestController
@RequestMapping("/mmseQuestions")
public class MMSEQuestionController {

    @Autowired
    private MMSEQuestionService questionService;

    @Operation(summary = "MMSE新增")
    @PostMapping
    public Result<Boolean> create(@RequestBody @Validated MMSEQuestionDTO mmseQuestionDTO) {
        MMSEQuestion mmseQuestion = new MMSEQuestion();
        BeanUtils.copyProperties(mmseQuestionDTO, mmseQuestion);
        boolean result = questionService.save(mmseQuestion);
        return Result.success(CommonEnum.SUCCESS.getCode(), CommonEnum.SUCCESS.getMessage(), result);
    }

    @Operation(summary = "MMSE更新")
    @PutMapping
    public Result<Boolean> update(@RequestBody @Validated MMSEQuestionDTO mmseQuestionDTO) {
        MMSEQuestion mmseQuestion = new MMSEQuestion();
        BeanUtils.copyProperties(mmseQuestionDTO, mmseQuestion);
        mmseQuestion.setUpdateTime(now());
        boolean result = questionService.updateById(mmseQuestion);
        return Result.success(CommonEnum.SUCCESS.getCode(), CommonEnum.SUCCESS.getMessage(), result);
    }

    @Operation(summary = "MMSE删除")
    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Integer id) {
        boolean result = questionService.removeById(id);
        return Result.success(CommonEnum.SUCCESS.getCode(), CommonEnum.SUCCESS.getMessage(), result);
    }
    @Operation(summary = "MMSE查id")
    @GetMapping("/{id}")
    public Result<MMSEQuestionVO> getById(@PathVariable Integer id) {
        MMSEQuestion question = questionService.getById(id);
        MMSEQuestionVO vo = new MMSEQuestionVO();
        BeanUtils.copyProperties(question, vo);
        return Result.success(CommonEnum.SUCCESS.getCode(), CommonEnum.SUCCESS.getMessage(), vo);
    }

    @Operation(summary = "根据条件分页查询MMSE量表问题")
    @GetMapping("/mmseQuestionsPage")
    public Result<PageDTO<MMSEQuestionVO>> queryMMSEQuestionsPage(MMSEQuestionQuery query) {
        PageDTO<MMSEQuestionVO> pageDTO = questionService.queryMMSEQuestionsPage(query);
        return Result.success(pageDTO);  // 使用 Result.success() 返回封装后的结果
    }

    @Operation(summary = "展示所有题目")
    @GetMapping("/listMMSEQuestions")
    public Result<List<MMSEQuestionShowVO>> listMMSEQuestions(){
        List<MMSEQuestionShowVO> questions = questionService.listAllMMSEQuestions();
        return Result.success(CommonEnum.SUCCESS.getCode(), CommonEnum.SUCCESS.getMessage(), questions);
    }

    @Operation(summary = "展示所有题目大项数量")
    @GetMapping("/listMMSEQuestionCategory")
    public Result<List<MMSEQuestionCategoryVO>> listMMSEQuestionCategory(){
        List<MMSEQuestionCategoryVO> categories = questionService.listMMSEQuestionCategory();
        return Result.success(CommonEnum.SUCCESS.getCode(), CommonEnum.SUCCESS.getMessage(), categories);
    }
}

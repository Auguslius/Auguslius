package org.example.web.rest.Controller;


import cn.hutool.core.bean.BeanUtil;

import org.example.domain.dto.PageDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.common.enums.CommonEnum;
import org.example.common.enums.InstitutionEnum;
import org.example.common.result.Result;
import org.example.domain.dto.InstitutionDTO;
import org.example.domain.entity.Institution;
import org.example.domain.query.InstitutionQuery;
import org.example.domain.vo.InstitutionVO;
import org.example.web.rest.service.InstitutionService;
import org.springframework.web.bind.annotation.*;



import java.util.List;

//	82e3736108b344065399d388a97c8372	a0d6fd00f696565d979f6c6d217ab1e4
@Tag(name = "医疗机构管理")
@RestController
@RequestMapping("/institution")
@RequiredArgsConstructor
public class InstitutionController {

    private final InstitutionService institutionService;

    @Operation(summary = "添加医疗机构接口")
    @PostMapping
    public Result<Boolean> addInstitution(@RequestBody @Valid InstitutionDTO institutionDTO) {
        Institution institution = new Institution();
        BeanUtil.copyProperties(institutionDTO, institution);
        Boolean result = institutionService.addInstitution(institution);
        return Result.success(CommonEnum.SUCCESS.getCode(), CommonEnum.SUCCESS.getMessage(), result);
    }


    @Operation(summary ="删除医疗机构接口")
    @DeleteMapping("{uuid}")
    public Result deleteInstitutionById(@Parameter(description = "机构id") @PathVariable("uuid") String uuid) {
        Boolean result=institutionService.deleteInstitutionByUuid(uuid);
        return Result.success(CommonEnum.SUCCESS.getCode(), InstitutionEnum.Delete_Institution_Success.getMessage(),result);
    }

    @Operation(summary ="更新医疗机构接口")
    @PatchMapping
    public Result updateInstitution(@RequestBody @Valid InstitutionDTO institutionDTO) {
        Institution institution = BeanUtil.copyProperties(institutionDTO, Institution.class);
        Boolean result = institutionService.updateInstitution(institution);
        return Result.success(CommonEnum.SUCCESS.getCode(), InstitutionEnum.Update_Institution_Success.getMessage(),result);
    }

    @Operation(summary ="分页条件查询接口")
    @GetMapping("/page")
    public Result<PageDTO<InstitutionVO>> queryInstitutionPage(InstitutionQuery institutionQuery){
        // 获取分页数据
        PageDTO<InstitutionVO> page = institutionService.queryInstitutionPage(institutionQuery);
        // 返回成功的结果，包含状态码、消息和分页数据
        return Result.success(CommonEnum.SUCCESS.getCode(), InstitutionEnum.Query_Institution_Success.getMessage(), page);
    }

    @Operation(summary ="根据id查询接口")
    @GetMapping("/{uuid}")
    public Result<InstitutionVO> queryInstitutionById(@Parameter(description = "机构id") @PathVariable("uuid") String uuid) {
        InstitutionVO result = institutionService.queryInstitutionByUuid(uuid);
        return Result.success(CommonEnum.SUCCESS.getCode(), InstitutionEnum.Query_Institution_Success.getMessage(), result);
    }

    @Operation(summary ="查询所有机构接口")
    @GetMapping("/getAll")
    public Result<List<InstitutionVO>> getAllInstitution(){
        List<InstitutionVO> result = institutionService.getAllInstitution();
        return Result.success(CommonEnum.SUCCESS.getCode(), InstitutionEnum.Query_Institution_Success.getMessage(), result);
    }
}

package org.example.web.rest.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.common.enums.CommonEnum;
import org.example.common.result.Result;
import org.example.domain.dto.InstitutionCategoryDTO;
import org.example.domain.vo.InstitutionCategoryLevelListVO;
import org.example.domain.vo.InstitutionCategoryLevelVO;
import org.example.domain.vo.InstitutionCategoryVO;
import org.example.web.rest.service.InstitutionCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/institutionCategory")
@Validated
@Tag(name="机构种类管理")
public class InstitutionCategoryController {

    @Autowired
    private InstitutionCategoryService institutionCategoryService;

    @GetMapping
    @Operation(summary = "获取所有机构种类")
    public Result<List<InstitutionCategoryVO>> getAllInstitutionCategory() {
        return institutionCategoryService.getAllInstitutionCategory();
    }

    @PostMapping
    @Operation(summary = "新增机构种类")
    public Result<InstitutionCategoryVO> addInstitutionCategory(@RequestBody InstitutionCategoryDTO institutionCategoryDTO) {
        return institutionCategoryService.addInstitutionCategory(institutionCategoryDTO);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除机构种类")
    public Result deleteInstitutionCategory(@Parameter(description = "用户id") @PathVariable("id") Long id) {
        return institutionCategoryService.deleteInstitutionCategory(id);
    }
    @GetMapping("/getLevelList")
    @Operation(summary = "获取所有层级信息")
    public Result<InstitutionCategoryLevelVO> getLevelList() {
        List<InstitutionCategoryLevelListVO> levelList = institutionCategoryService.getDistinctLevels();
        Integer count = institutionCategoryService.getLevelCount();
        InstitutionCategoryLevelVO institutionCategoryLevelVO = new InstitutionCategoryLevelVO();
        institutionCategoryLevelVO.setCount(count);
        institutionCategoryLevelVO.setLevelList(levelList);

        return Result.success(institutionCategoryLevelVO);
    }
    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取机构种类")
    public Result<InstitutionCategoryVO> getInstitutionCategoryById(@PathVariable Long id) {
        return institutionCategoryService.getInstitutionCategoryById(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新机构种类")
    public Result<InstitutionCategoryVO> updateInstitutionCategory(@PathVariable Long id, @RequestBody InstitutionCategoryDTO institutionCategoryDTO) {
        return institutionCategoryService.updateInstitutionCategory(id, institutionCategoryDTO);
    }

    @GetMapping("/getLevelCount")
    @Operation(summary = "获取层级数量")
    public Integer getLevelCount() {
        return institutionCategoryService.getLevelCount();
    }


    @GetMapping("/getCategoryTree")
    @Operation(summary = "获取机构种类树")
    public Result<List<Map<String, Object>>> getCategoryTree() {
        List<Map<String, Object>> categoryTree = institutionCategoryService.getCategoryTree();
        return Result.success(CommonEnum.SUCCESS.getCode(), CommonEnum.SUCCESS.getMessage(),categoryTree);
    }

}

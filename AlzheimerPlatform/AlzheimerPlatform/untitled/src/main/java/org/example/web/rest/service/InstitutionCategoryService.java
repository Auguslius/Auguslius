package org.example.web.rest.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.common.result.Result;
import org.example.domain.dto.InstitutionCategoryDTO;
import org.example.domain.entity.InstitutionCategory;
import org.example.domain.vo.InstitutionCategoryLevelListVO;
import org.example.domain.vo.InstitutionCategoryVO;

import java.util.List;
import java.util.Map;

public interface InstitutionCategoryService extends IService<InstitutionCategory> {
    Result deleteInstitutionCategory(Long id);

    Result<InstitutionCategoryVO> addInstitutionCategory(InstitutionCategoryDTO institutionCategoryDTO);

    Result<InstitutionCategoryVO> getInstitutionCategoryById(Long id);

    Result<InstitutionCategoryVO> updateInstitutionCategory(Long id, InstitutionCategoryDTO institutionCategoryDTO);

    Integer getLevelCount();

    List<InstitutionCategoryLevelListVO> getDistinctLevels();

    Result<List<InstitutionCategoryVO>> getAllInstitutionCategory();

    List<Map<String, Object>> getCategoryTree();
}

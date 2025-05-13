package org.example.rest.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.domain.entity.Institution;

import java.util.List;

public interface InstitutionService extends IService<Institution>{
    List<Institution> getInstitutionByInstitutionCategoryId(int institutionCategoryId);
}

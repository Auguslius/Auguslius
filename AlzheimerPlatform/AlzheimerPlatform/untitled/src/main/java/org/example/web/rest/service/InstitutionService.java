package org.example.web.rest.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.common.result.Result;
import org.example.domain.entity.Institution;
import org.example.domain.query.InstitutionQuery;
import org.example.domain.vo.InstitutionVO;
import org.example.domain.dto.PageDTO;
import java.util.List;

public interface InstitutionService extends IService<Institution> {

    Boolean addInstitution(Institution institution);

    Boolean deleteInstitutionByUuid(String uuid);

    Boolean updateInstitution(Institution institution);

    PageDTO<InstitutionVO> queryInstitutionPage(InstitutionQuery institutionQuery);

    Result<List<InstitutionVO>> queryInstitutionById(List<Long> ids);

    InstitutionVO queryInstitutionByUuid(String uuid);

    List<InstitutionVO> getAllInstitution();
}

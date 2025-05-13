package org.example.web.rest.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.validation.Valid;
import org.example.common.enums.CommonEnum;
import org.example.common.enums.InstitutionEnum;
import org.example.common.result.Result;
import org.example.exception.BusinessException;
import org.example.domain.entity.CategoryLevel;
import org.example.domain.entity.Institution;
import org.example.domain.dto.PageDTO;
import org.example.domain.query.InstitutionQuery;
import org.example.domain.vo.InstitutionVO;
import org.example.web.rest.mapper.CategoryLevelMapper;
import org.example.web.rest.mapper.InstitutionCategoryMapper;
import org.example.web.rest.mapper.InstitutionMapper;
import org.example.web.rest.service.InstitutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.*;


@Service
public class InstitutionImpl extends ServiceImpl<InstitutionMapper, Institution> implements InstitutionService {

    @Autowired
    private InstitutionMapper institutionMapper;

    @Autowired
    private InstitutionCategoryMapper institutionCategoryMapper;

    @Autowired
    private CategoryLevelMapper categoryLevelMapper;
    @Override
    public Boolean addInstitution(@Valid Institution institution) {

        // 检查机构分类是否存在
        boolean categoryExists = institutionCategoryMapper.selectById(institution.getInstitutionCategoryId()) != null;
        if (!categoryExists) {
            throw new BusinessException(CommonEnum.FAIL.getCode(), InstitutionEnum.Add_Category_Not_Exist.getMessage());
        }
        // 根据 level 字段查询层级信息,level不是id
        LambdaQueryWrapper<CategoryLevel> levelQueryWrapper = new LambdaQueryWrapper<>();
        levelQueryWrapper.eq(CategoryLevel::getLevel, institution.getInstitutionLevel());
        CategoryLevel categoryLevel = categoryLevelMapper.selectOne(levelQueryWrapper);
        // 检查机构等级是否存在
        if (categoryLevel == null) {
            throw new BusinessException(CommonEnum.FAIL.getCode(), InstitutionEnum.Add_Level_Not_Exist.getMessage());
        }
        institution.setUuid(UUID.randomUUID().toString());
        institution.setUpdateTime(new java.util.Date());
        institution.setStatus(1);//禁用
        try{
            boolean saved = this.save(institution);
            if(!saved){
                throw new BusinessException(CommonEnum.FAIL.getCode(), InstitutionEnum.Add_Institution_Fail.getMessage());
            }
        }catch (DuplicateKeyException e) {
            throw new BusinessException(CommonEnum.FAIL.getCode(), InstitutionEnum.Update_Institution_Duplicate_Name.getMessage());
        }
        return true;
    }

    @Override
    public Boolean deleteInstitutionByUuid(@RequestBody String uuid) {
        // 查询是否存在
        Institution institution = lambdaQuery().eq(Institution::getUuid, uuid).one();
        if (institution == null) {
            throw new BusinessException(CommonEnum.FAIL.getCode(), InstitutionEnum.Delete_Institution_Fail.getMessage());
        }
        // 删除机构
        boolean deleted = lambdaUpdate().eq(Institution::getUuid, uuid).remove();
        if (!deleted) {
            throw new BusinessException(CommonEnum.FAIL.getCode(), InstitutionEnum.Delete_Institution_Fail.getMessage());
        }
        return true;
    }




    @Override
    public Result<List<InstitutionVO>> queryInstitutionById(List<Long> ids) {
        return null;
    }

    @Override
    public InstitutionVO queryInstitutionByUuid(String uuid) {
        Institution institution = lambdaQuery()
                .eq(Institution::getUuid, uuid)
                .one();
        return BeanUtil.copyProperties(institution, InstitutionVO.class);
    }

    @Override
    public List<InstitutionVO> getAllInstitution() {
        // 查询所有机构
        List<Institution> institutions = lambdaQuery().list();
        // 创建结果列表
        List<InstitutionVO> result = new ArrayList<>(institutions.size());
        // 将每个Institution转换为InstitutionVO并添加到结果列表
        for (Institution institution : institutions) {
            InstitutionVO institutionVO = BeanUtil.copyProperties(institution, InstitutionVO.class);
            result.add(institutionVO);
        }
        return result;
    }

    @Override
    public Boolean updateInstitution(@Valid Institution institution) {
        // 检查机构是否存在（改为使用 UUID 查询）
        Institution existingInstitution = lambdaQuery()
                .eq(Institution::getUuid, institution.getUuid())
                .one();
        if (existingInstitution == null) {
            throw new BusinessException(CommonEnum.FAIL.getCode(), InstitutionEnum.Update_Institution_Not_Exist.getMessage());
        }
        // 检查机构分类是否存在
        boolean institutionCategoryExists = institutionCategoryMapper.selectById(institution.getInstitutionCategoryId()) != null;
        if (!institutionCategoryExists) {
            throw new BusinessException(CommonEnum.FAIL.getCode(), InstitutionEnum.Update_Institution_Category_Not_Exist.getMessage());
        }
        // 使用 LambdaQueryWrapper 查询机构等级是否存在
        boolean levelExists = categoryLevelMapper.exists(new LambdaQueryWrapper<CategoryLevel>()
                .eq(CategoryLevel::getLevel, institution.getInstitutionLevel()));

        if (!levelExists) {
            throw new BusinessException(CommonEnum.FAIL.getCode(), InstitutionEnum.Update_Institution_Level_Not_Exist.getMessage());
        }
        // 设置更新时间
        institution.setUpdateTime(new Date());
        // 尝试更新（使用 UUID 作为条件）
        try {
            boolean updated = lambdaUpdate()
                    .eq(Institution::getUuid, institution.getUuid())
                    .update(institution);
            if (!updated) {
                throw new BusinessException(CommonEnum.FAIL.getCode(), InstitutionEnum.Update_Institution_Fail.getMessage());
            }
        } catch (DuplicateKeyException e) {
            throw new BusinessException(CommonEnum.FAIL.getCode(), InstitutionEnum.Update_Institution_Duplicate_Name.getMessage());
        }
        return true;
    }

    @Override
    public PageDTO<InstitutionVO> queryInstitutionPage(InstitutionQuery institutionQuery) {
       String InstitutionName = institutionQuery.getInstitutionName();
       Integer InstitutionLevel = institutionQuery.getInstitutionLevel();
        Page<Institution> page = institutionQuery.toMpPageDefaultSortByCreateTime();
        Page<Institution> p = lambdaQuery()//加入了页数和总页数
                .like(InstitutionName != null, Institution::getInstitutionName, InstitutionName)
                .like(InstitutionLevel != null, Institution::getInstitutionLevel, InstitutionLevel)
                .page(page);
            return PageDTO.of(p, institution -> {
                InstitutionVO institutionVO = BeanUtil.copyProperties(institution, InstitutionVO.class);
            return institutionVO;
        });
    }

}

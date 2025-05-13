package org.example.rest.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.domain.entity.Institution;

import org.example.rest.service.InstitutionService;
import org.example.web.rest.mapper.InstitutionMapper;
import org.example.web.rest.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InstitutionServiceImpl extends ServiceImpl<InstitutionMapper, Institution> implements InstitutionService {

    @Autowired
    private InstitutionMapper institutionMapper;

    @Autowired
    private UserMapper userMapper;

    public static final String CATEGORY_ID = "institution_category_id";


    @Override
    public List<Institution> getInstitutionByInstitutionCategoryId(int institutionCategoryId) {
        // 使用QueryWrapper构建查询条件
        QueryWrapper<Institution> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(CATEGORY_ID, institutionCategoryId);
        // 查询符合条件的所有机构
        return institutionMapper.selectList(queryWrapper);
    }
}

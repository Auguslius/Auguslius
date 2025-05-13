package org.example.web.rest.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.common.enums.CommonEnum;
import org.example.common.enums.InstitutionCategoryEnum;
import org.example.common.result.Result;
import org.example.domain.dto.InstitutionCategoryDTO;
import org.example.domain.entity.CategoryLevel;
import org.example.domain.entity.InstitutionCategory;
import org.example.domain.vo.InstitutionCategoryLevelListVO;
import org.example.domain.vo.InstitutionCategoryVO;
import org.example.exception.BusinessException;
import org.example.web.rest.mapper.CategoryLevelMapper;
import org.example.web.rest.mapper.InstitutionCategoryMapper;
import org.example.web.rest.service.InstitutionCategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class InstitutionCategoryImpl extends ServiceImpl<InstitutionCategoryMapper, InstitutionCategory> implements InstitutionCategoryService {

    @Autowired
    private InstitutionCategoryMapper InstitutionCategoryMapper;

    @Autowired
    private CategoryLevelMapper categoryLevelMapper;


    @Override
    public Result<List<InstitutionCategoryVO>> getAllInstitutionCategory() {
        return Result.success(CommonEnum.SUCCESS.getCode(), InstitutionCategoryEnum.Get_Institution_Success.getMessage(),
                BeanUtil.copyToList(InstitutionCategoryMapper.selectList(null), InstitutionCategoryVO.class));
    }


    @Override
    public Result<Void> deleteInstitutionCategory(Long id) {
        // 检查是否存在
        InstitutionCategory institutionCategory = getById(id);
        if (institutionCategory == null) {
            throw new BusinessException(InstitutionCategoryEnum.Delete_Institution_Fail.getCode(),
                    InstitutionCategoryEnum.Delete_Institution_Fail.getMessage());
        }
        // 删除操作
        removeById(id);
        return Result.success(CommonEnum.SUCCESS.getCode(), InstitutionCategoryEnum.Delete_Institution_Success.getMessage(), null);
    }

    @Override
    public Result<InstitutionCategoryVO> addInstitutionCategory(InstitutionCategoryDTO institutionCategoryDTO) {
        // 将 DTO 转换为 POJO
        InstitutionCategory institutionCategory = new InstitutionCategory();
        BeanUtils.copyProperties(institutionCategoryDTO, institutionCategory);
        // 校验别名是否重复
        if (institutionCategory.getCategoryAlias() != null) {
            LambdaQueryWrapper<InstitutionCategory> aliasQuery = new LambdaQueryWrapper<>();
            aliasQuery.eq(InstitutionCategory::getCategoryAlias, institutionCategory.getCategoryAlias());
            if (count(aliasQuery) > 0) {
                throw new BusinessException(InstitutionCategoryEnum.Category_Alias_Exists.getCode(),
                        InstitutionCategoryEnum.Category_Alias_Exists.getMessage());
            }
        }
        // 校验种类名是否重复
        LambdaQueryWrapper<InstitutionCategory> nameQuery = new LambdaQueryWrapper<>();
        nameQuery.eq(InstitutionCategory::getCategoryName, institutionCategory.getCategoryName());
        if (count(nameQuery) > 0) {
            throw new BusinessException(InstitutionCategoryEnum.Category_Name_Exists.getCode(),
                    InstitutionCategoryEnum.Category_Name_Exists.getMessage());
        }
        // 设置 level_name 字段，如果没有传递该字段，给它设置一个默认值
        if (institutionCategory.getLevelName() == null || institutionCategory.getLevel()==null) {
            institutionCategory.setLevel(5);
            institutionCategory.setLevelName("其它"); // 设置默认值
        }
        //level表插入
        if (institutionCategory.getLevel() != null && institutionCategory.getLevelName() != null) {
            // 创建查询条件
            QueryWrapper<CategoryLevel> levelWrapper = new QueryWrapper<>();
            levelWrapper.eq("level", institutionCategory.getLevel())
                    .eq("level_name", institutionCategory.getLevelName());

            // 查询是否已存在相同的 level 和 levelName
            CategoryLevel existingCategoryLevel = categoryLevelMapper.selectOne(levelWrapper);

            // 如果不存在重复记录，则插入
            if (existingCategoryLevel == null) {
                CategoryLevel categoryLevel = new CategoryLevel();
                categoryLevel.setLevel(institutionCategory.getLevel());
                categoryLevel.setLevelName(institutionCategory.getLevelName());

                // 执行插入
                categoryLevelMapper.insert(categoryLevel);
            }
        }
        // 新增操作
        save(institutionCategory);
        // 返回结果
        InstitutionCategoryVO institutionCategoryVO = BeanUtil.copyProperties(institutionCategory, InstitutionCategoryVO.class);
        return Result.success(CommonEnum.SUCCESS.getCode(),
                InstitutionCategoryEnum.Add_Institution_Success.getMessage(),
                institutionCategoryVO);
    }

    @Override
    public Result<InstitutionCategoryVO> getInstitutionCategoryById(Long id) {
        // 查询操作
        InstitutionCategory institutionCategory = getById(id);
        if (institutionCategory == null) {
            return Result.fail(CommonEnum.FAIL.getCode(), InstitutionCategoryEnum.Get_Institution_Fail.getMessage(), null);
        }
        // 返回结果
        InstitutionCategoryVO institutionCategoryVO = BeanUtil.copyProperties(institutionCategory, InstitutionCategoryVO.class);
        return Result.success(CommonEnum.SUCCESS.getCode(), InstitutionCategoryEnum.Get_Institution_Success.getMessage(), institutionCategoryVO);
    }

    @Override
    public Result<InstitutionCategoryVO> updateInstitutionCategory(Long id, InstitutionCategoryDTO institutionCategoryDTO) {
        // 查询当前记录
        InstitutionCategory institutionCategory = getById(id);
        if (institutionCategory == null) {
            return Result.fail(CommonEnum.FAIL.getCode(), InstitutionCategoryEnum.Update_Institution_Not_Found.getMessage(), null);
        }
        // 校验种类名是否重复（排除当前记录）
        if (!institutionCategory.getCategoryName().equals(institutionCategoryDTO.getCategoryName())) {
            LambdaQueryWrapper<InstitutionCategory> nameQuery = new LambdaQueryWrapper<>();
            nameQuery.eq(InstitutionCategory::getCategoryName, institutionCategoryDTO.getCategoryName())
                    .ne(InstitutionCategory::getId, id); // 排除当前记录
            if (count(nameQuery) > 0) {
                throw new BusinessException(InstitutionCategoryEnum.Category_Name_Exists.getCode(),
                        InstitutionCategoryEnum.Category_Name_Exists.getMessage());
            }
        }

        // 更新数据
        BeanUtils.copyProperties(institutionCategoryDTO, institutionCategory);
        updateById(institutionCategory);

        // 返回结果
        InstitutionCategoryVO institutionCategoryVO = BeanUtil.copyProperties(institutionCategory, InstitutionCategoryVO.class);
        return Result.success(CommonEnum.SUCCESS.getCode(), InstitutionCategoryEnum.Update_Institution_Success.getMessage(), institutionCategoryVO);
    }

    @Override
    public Integer getLevelCount() {
        LambdaQueryWrapper<InstitutionCategory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(InstitutionCategory::getLevel).groupBy(InstitutionCategory::getLevel);
        // 查询所有不同的 level 记录
        List<InstitutionCategory> levelList = InstitutionCategoryMapper.selectList(queryWrapper);
        // 获取不同 level 的数量
        return levelList.size();
    }

    public List<InstitutionCategoryLevelListVO> getDistinctLevels() {
        QueryWrapper<InstitutionCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("level", "level_name").groupBy("level", "level_name");

        List<Map<String, Object>> levelMaps = InstitutionCategoryMapper.selectMaps(queryWrapper);

        return levelMaps.stream()
                .map(map -> {
                    InstitutionCategoryLevelListVO vo = new InstitutionCategoryLevelListVO();
                    vo.setLevel((Integer) map.get("level"));
                    vo.setLevelName((String) map.get("level_name"));
                    return vo;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<Map<String, Object>> getCategoryTree() {

        List<InstitutionCategory> list = this.list();
        //构建树结构
        return buildTree(list);
    }

    private List<Map<String, Object>> buildTree(List<InstitutionCategory> list){
        Map<Integer,Map<String,Object>> levelMap = new HashMap<>();
        //构建最终树结构
        List<Map<String,Object>> tree = new ArrayList<>();

        //遍历数据
        for(InstitutionCategory category : list){
            Integer level = category.getLevel();
            String levelName = category.getLevelName();
            Integer id = category.getId();
            String categoryName = category.getCategoryName();

            if(!levelMap.containsKey(level)){
                Map<String,Object> rootNode = new HashMap<>();
                rootNode.put("value",level);
                rootNode.put("label",levelName);
                rootNode.put("children",new ArrayList<>());
                levelMap.put(level, rootNode);
                tree.add(rootNode); // 将根节点添加到树中

            }

            Map<String,Object> childNode = new HashMap<>();
            childNode.put("value",id);
            childNode.put("label",categoryName);
            ((List<Map<String, Object>>) levelMap.get(level).get("children")).add(childNode);
        }
        return tree;
    }



}
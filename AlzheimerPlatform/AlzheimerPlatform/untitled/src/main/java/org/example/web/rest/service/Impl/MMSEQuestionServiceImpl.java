package org.example.web.rest.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.example.domain.entity.MMSEQuestion;
import org.example.domain.dto.PageDTO;
import org.example.domain.query.MMSEQuestionQuery;
import org.example.domain.vo.MMSEQuestionCategoryVO;
import org.example.domain.vo.MMSEQuestionShowVO;
import org.example.domain.vo.MMSEQuestionVO;
import org.example.web.rest.mapper.MMSEQuestionMapper;
import org.example.web.rest.service.MMSEQuestionService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MMSEQuestionServiceImpl extends ServiceImpl<MMSEQuestionMapper, MMSEQuestion> implements MMSEQuestionService {
    @Override
    public PageDTO<MMSEQuestionVO> queryMMSEQuestionsPage(MMSEQuestionQuery query) {
        String section = query.getSection();
        String questionType = query.getQuestionType();
        String sortField = query.getSortField();
        String sortOrder = query.getSortOrder();

        // 构建分页查询条件
        Page<MMSEQuestion> page = query.toMpPage();  // 只获取分页参数

        // 构建排序条件
        LambdaQueryChainWrapper<MMSEQuestion> queryWrapper = lambdaQuery()
                .like(section != null, MMSEQuestion::getSection, section)
                .like(questionType != null, MMSEQuestion::getQuestionType, questionType);

        // 添加排序
        if ("position".equals(sortField) && "desc".equals(sortOrder)) {
            queryWrapper.orderByDesc(MMSEQuestion::getPosition);
        } else if ("position".equals(sortField) && "asc".equals(sortOrder)) {
            queryWrapper.orderByAsc(MMSEQuestion::getPosition);
        } else {
            // 默认排序
            queryWrapper.orderByDesc(MMSEQuestion::getUpdateTime);
        }

        Page<MMSEQuestion> p = queryWrapper.page(page);

        System.out.println("pages:" + p.getPages());
        System.out.println("total:" + p.getTotal());

        return PageDTO.of(p, mmseQuestion -> {
            // 1.拷贝基础属性
            MMSEQuestionVO vo = BeanUtil.copyProperties(mmseQuestion, MMSEQuestionVO.class);
            // 2.处理特殊逻辑，如果有的话
            return vo;
        });
    }

    @Override
    public List<MMSEQuestionShowVO> listAllMMSEQuestions() {
        // 查询所有MMSE问题，按position排序
        List<MMSEQuestion> questions = lambdaQuery()
                .orderByAsc(MMSEQuestion::getPosition)
                .list();
        
        // 转换为ShowVO
        return questions.stream()
                .map(question -> BeanUtil.copyProperties(question, MMSEQuestionShowVO.class))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<MMSEQuestionCategoryVO> listMMSEQuestionCategory() {
        // 查询所有MMSE问题
        List<MMSEQuestion> questions = list();
        
        // 按section分组并计算每组的数量
        Map<String, Long> sectionCountMap = questions.stream()
                .collect(Collectors.groupingBy(MMSEQuestion::getSection, Collectors.counting()));
        
        // 转换为VO列表
        List<MMSEQuestionCategoryVO> categoryList = new ArrayList<>();
        sectionCountMap.forEach((section, count) -> {
            MMSEQuestionCategoryVO categoryVO = new MMSEQuestionCategoryVO();
            categoryVO.setSection(section);
            categoryVO.setCount(count.intValue());
            categoryList.add(categoryVO);
        });
        
        return categoryList;
    }
}

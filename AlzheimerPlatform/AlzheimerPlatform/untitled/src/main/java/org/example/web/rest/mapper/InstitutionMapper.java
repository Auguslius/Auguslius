package org.example.web.rest.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.domain.entity.Institution;

@Mapper
public interface InstitutionMapper extends BaseMapper<Institution> {
    // 继承 BaseMapper，已经包含基础的 CRUD 方法
}
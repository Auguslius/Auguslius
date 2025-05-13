package org.example.rest.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.domain.entity.Patient;

/**
 * 认证服务接口
 *
 * @author lyx
 */
@Mapper
public interface AuthenticationMapper extends BaseMapper<Patient> {


}

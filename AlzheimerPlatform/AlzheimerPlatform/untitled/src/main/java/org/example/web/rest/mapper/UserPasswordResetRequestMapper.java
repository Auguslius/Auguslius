package org.example.web.rest.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.domain.entity.UserPasswordResetRequest;

/**
 * PasswordResetRequestMapper
 */
@Mapper
public interface UserPasswordResetRequestMapper extends BaseMapper<UserPasswordResetRequest> {

}

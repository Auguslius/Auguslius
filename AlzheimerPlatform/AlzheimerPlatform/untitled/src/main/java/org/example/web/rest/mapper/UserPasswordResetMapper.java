package org.example.web.rest.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.domain.entity.UserPasswordReset;

/**
 * 用户密码重置Mapper接口
 */
@Mapper
public interface UserPasswordResetMapper extends BaseMapper<UserPasswordReset> {
}

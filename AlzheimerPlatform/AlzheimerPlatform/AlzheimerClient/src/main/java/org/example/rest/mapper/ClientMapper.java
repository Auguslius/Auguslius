package org.example.rest.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.domain.entity.Client;

/**
 * 客户端用户Mapper接口
 *
 * @author lyx
 */
@Mapper
public interface ClientMapper extends BaseMapper<Client> {
}
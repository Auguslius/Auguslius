package org.example.web.rest.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.domain.entity.MediaFile;


/**
 * 媒体文件Mapper接口
 */
@Mapper
public interface MediaFileMapper extends BaseMapper<MediaFile> {
} 
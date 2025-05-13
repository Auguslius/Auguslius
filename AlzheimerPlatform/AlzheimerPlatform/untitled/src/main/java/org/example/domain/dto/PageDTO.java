package org.example.domain.dto;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/*、
* 后端返回数据处理
* */
@Data
@Schema(description = "分页结果")
public class PageDTO<T> {

    @Schema(description = "总记录数")
    private Long total;

    @Schema(description = "总页数")
    private Long pages;

    @Schema(description = "当前页数据")
    private List<T> list;


    //分页(页处理<pageQuery自带处理>)
    //      => 整合(总页的处理<pageDTO处理>)
    public static <POJO,VO> PageDTO<VO> of(Page<POJO> pageDTO, Class<VO> voClass){
        PageDTO<VO> dto = new PageDTO<>();
        // 1.总条数
        dto.setTotal(pageDTO.getTotal());
        // 2.总页数
        dto.setPages(pageDTO.getPages());
        // 3.当前页数据
        List<POJO> records = pageDTO.getRecords();
        if (CollUtil.isEmpty(records)) {
            dto.setList(Collections.emptyList());
            return dto;
        }
        // 4.拷贝user的VO
        dto.setList(BeanUtil.copyToList(records, voClass));
        // 5.返回
        return dto;
    }
    public static <POJO, VO> PageDTO<VO> of(Page<POJO> p, Function<POJO, VO> convertor){
        PageDTO<VO> dto = new PageDTO<>();
        // 1.总条数
        dto.setTotal(p.getTotal());
        // 2.总页数
        dto.setPages(p.getPages());
        // 3.当前页数据
        List<POJO> records = p.getRecords();
        if (CollUtil.isEmpty(records)) {
            dto.setList(Collections.emptyList());
            return dto;
        }
        // 4.拷贝user的VO
        dto.setList(records.stream().map(convertor).collect(Collectors.toList()));
        // 5.返回
        return dto;
    }

}

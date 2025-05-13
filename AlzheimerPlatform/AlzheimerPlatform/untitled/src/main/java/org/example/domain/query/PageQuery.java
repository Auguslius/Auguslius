package org.example.domain.query;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/*
* 前端数据接收
* */
@Data
@Schema(description = "分页查询实体")
public class PageQuery {
    @Schema(description = "当前页码(默认为1，前端更改)")
    private Integer pageNo = 1;
    @Schema(description ="每页显示的记录数（默认值为10）")
    private Integer pageSize = 8;
    @Schema(description ="排序字段")
    private String sortBy;
    @Schema(description ="是否升序")
    private Boolean isAsc = true;

    /*
    * 自定义条件查
    * */
    public <T> Page<T> toMpPage(OrderItem... items){
        // 创建 Page 对象
        System.out.println("pageNo:"+pageNo);
        System.out.println("pageSize:"+pageSize);
        Page<T> page = Page.of(pageNo, pageSize);

        // 排序条件
        if(StrUtil.isNotBlank(sortBy)){
            // 不为空
            page.addOrder(new OrderItem(sortBy, isAsc));
        }else if(items != null){
            // 为空，默认排序
            page.addOrder(items);
        }
        return page;
    }

    /*
    * 默认查
    * */
    public <T> Page<T> toMpPage(String defaultSortBy, Boolean defaultAsc){
        return toMpPage(new OrderItem(defaultSortBy, defaultAsc));
    }

    /*
    * 根据创建时间查
    * */
    public <T> Page<T> toMpPageDefaultSortByCreateTime(){
        return toMpPage(new OrderItem("create_time", false));
    }

    /*
     * 根据更新时间查
     * */
    public <T> Page<T> toMpPageDefaultSortByUpdateTime(){
        return toMpPage(new OrderItem("update_time", false));
    }
}

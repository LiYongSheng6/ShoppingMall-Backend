package com.shoppingmall.demo.model.Query;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Objects;

/**
 * @author redmi k50 ultra
 * * @date 2024/7/25
 */
@Data
@ApiModel(description = "分页查询实体")
public class PageQuery {
    @ApiModelProperty("当前页码")
    @NotNull(message = "当前页码不能为空")
    private Long pageNum ;

    @NotNull
    @ApiModelProperty("每页大小")
    @NotNull(message = "页面条数不能为空")
    private Long pageSize ;

    @ApiModelProperty("排序字段")
    private String sortBy;

    @ApiModelProperty("是否升序")
    private Boolean isAsc ;

    public <T>Page <T> toMpPage(OrderItem... items){
        //分页条件
        Page<T> page = Page.of(pageNum,pageSize);
        //排序条件
        if(StrUtil.isNotBlank(sortBy)){
            page.addOrder(new OrderItem(sortBy, Objects.requireNonNullElse(isAsc, false)));
        }else if(items != null){
            page.addOrder(items);
        }
        return page;
    }

    public <T>Page <T> toMpPage(String defaultSortBy, Boolean defaultAsc){
        return toMpPage(new OrderItem(defaultSortBy,defaultAsc));
    }

    /**
     * 默认时间降序
     * @return
     * @param <T>
     */
    public <T>Page <T> toMpPageDefaultSortByCreateTime(){
        boolean defaultAsc = false;
        if(isAsc!=null){
            defaultAsc = isAsc;
        }
        return toMpPage(new OrderItem("create_time", defaultAsc));
    }

    public <T>Page <T> toMpPageDefaultSortByUpdateTime(){
        boolean defaultAsc = false;
        if(isAsc!=null){
            defaultAsc = isAsc;
        }
        return toMpPage(new OrderItem("update_time", defaultAsc));
    }

}

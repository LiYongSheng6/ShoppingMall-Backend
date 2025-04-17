package com.shoppingmall.demo.model.VO;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 分页返回结果对象
 *
 * @author redmi k50 ultra
 * * @date 2024/7/21
 */
@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "分页结果")
public class PageVO<T> {
    /**
     * 总条数
     */
    @ApiModelProperty("总条数")
    private Long total;

    /**
     * 总页数
     */
    @ApiModelProperty("总页数")
    private Long pages;
    /**
     * 当前页码
     */
    @ApiModelProperty("当前页数")
    private Long current;

    /**
     * 当前页数据集合
     */
    @ApiModelProperty("数据集合")
    private List<T> items;

    public static <PO, VO> PageVO<VO> of(Page<PO> p, Class<VO> clazz) {
        PageVO<VO> dto = new PageVO<>();
        //总条数
        dto.setTotal(p.getTotal());
        //总页数
        dto.setPages(p.getPages());
        //当前页数据
        List<PO> records = p.getRecords();
        dto.setCurrent(p.getCurrent());
        if (CollUtil.isEmpty(records)) {
            dto.setItems(Collections.emptyList());
            return dto;
        }
        //拷贝PO至VO
        dto.setItems(BeanUtil.copyToList(records, clazz));
        return dto;
    }

    public static <PO, VO> PageVO<VO> of(Page<PO> p, Function<PO, VO> convertor) {
        PageVO<VO> dto = new PageVO<>();
        //总条数
        dto.setTotal(p.getTotal());
        //总页数
        dto.setPages(p.getPages());
        //当前页数据
        dto.setCurrent(p.getCurrent());
        List<PO> records = p.getRecords();
        if (CollUtil.isEmpty(records)) {
            dto.setItems(Collections.emptyList());
            return dto;
        }
        //拷贝PO至VO
        dto.setItems(records.stream().map(convertor).collect(Collectors.toList()));
        return dto;
    }
}

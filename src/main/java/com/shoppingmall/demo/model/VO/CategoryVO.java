package com.shoppingmall.demo.model.VO;


import cn.hutool.core.bean.BeanUtil;
import com.shoppingmall.demo.enums.CategoryType;
import com.shoppingmall.demo.model.DO.CategoryDO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class CategoryVO implements Serializable {

    /**
    * 分类id
    */
    @ApiModelProperty("分类id")
    private Long id;

    /**
    * 分类名称
    */
    @ApiModelProperty("分类名称")
    private String categoryName;

    /**
     * 分类所属商品类型
     */
    @ApiModelProperty("分类所属商品类型")
    private CategoryType type;

    public CategoryVO(CategoryDO categoryDO){
        BeanUtil.copyProperties(categoryDO, this);
    }

}

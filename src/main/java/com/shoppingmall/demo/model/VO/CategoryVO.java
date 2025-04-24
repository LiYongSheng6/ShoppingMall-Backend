package com.shoppingmall.demo.model.VO;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.shoppingmall.demo.enums.CategoryType;
import com.shoppingmall.demo.model.DO.CategoryDO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
     * 父级分类id
     */
    @ApiModelProperty("父级分类id")
    private Long parentId;

    /**
    * 分类名称
    */
    @ApiModelProperty("分类名称")
    private String categoryName;

    /**
     * 父级分类名称
     */
    @ApiModelProperty("父级分类名称")
    private String parentName;

    /**
     * 分类所属商品类型
     */
    @ApiModelProperty("分类所属商品类型")
    private CategoryType type;

    /**
     * 子分类
     */
    @TableField(exist = false)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<CategoryVO> children = new ArrayList<CategoryVO>();

    public CategoryVO(CategoryDO categoryDO){
        BeanUtil.copyProperties(categoryDO, this);
    }

}

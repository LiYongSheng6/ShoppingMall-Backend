package com.shoppingmall.demo.model.Query;

import com.shoppingmall.demo.enums.CategoryType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class CategoryQuery extends PageQuery {

    @Length(max = 255, message = "名称长度不能超过255")
    @ApiModelProperty("分类名称")
    private String categoryName;

    @ApiModelProperty("分类类型")
    private CategoryType type;
}

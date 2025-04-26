package com.shoppingmall.demo.model.DTO;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.shoppingmall.demo.annotation.CategoryTypePattern;
import com.shoppingmall.demo.config.deserializer.StringToLongDeserializer;
import com.shoppingmall.demo.enums.CategoryType;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.validation.annotation.Validated;

import java.io.Serializable;
import java.util.List;

/**
 * @TableName tag
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Validated
@Accessors(chain = true)
public class CategorySaveBatchDTO implements Serializable {

    /**
     * 分类名称列表
     */
    @NotNull(message = "[分类名称列表]不能为空")
    @ApiModelProperty("分类名称列表")
    private List<String> categoryNameList;

    /**
     * 父级分类id
     */
    @JsonDeserialize(using = StringToLongDeserializer.class)
    @NotNull(message = "[父级分类id]不能为空")
    @ApiModelProperty("父级分类id")
    private Long parentId;

    /**
     * 分类所属商品类型
     */
    @CategoryTypePattern
    @ApiModelProperty("分类所属商品类型")
    private CategoryType type;

}

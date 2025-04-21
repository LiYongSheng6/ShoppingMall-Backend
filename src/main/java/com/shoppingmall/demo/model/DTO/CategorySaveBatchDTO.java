package com.shoppingmall.demo.model.DTO;


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
     * 分类所属商品类型
     */
    @NotNull(message = "[分类所属商品类型]不能为空")
    @ApiModelProperty("分类所属商品类型")
    private Integer type;

}

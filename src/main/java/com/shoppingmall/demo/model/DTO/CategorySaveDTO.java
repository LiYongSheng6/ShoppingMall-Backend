package com.shoppingmall.demo.model.DTO;


import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;

import java.io.Serializable;

/**
* 
* @TableName tag
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Validated
@Accessors(chain = true)
public class CategorySaveDTO implements Serializable {

    /**
    * 分类名称
    */
    @Length(max= 255,message="编码长度不能超过255")
    @ApiModelProperty("分类名称")
    private String categoryName;

    /**
     * 分类所属商品类型
     */
    @NotNull(message="[分类所属商品类型]不能为空")
    @ApiModelProperty("分类所属商品类型")
    private Integer type;


}

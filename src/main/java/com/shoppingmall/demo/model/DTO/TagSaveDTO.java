package com.shoppingmall.demo.model.DTO;


import com.shoppingmall.demo.annotation.TagTypePattern;
import com.shoppingmall.demo.enums.TagType;
import io.swagger.annotations.ApiModelProperty;
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
public class TagSaveDTO implements Serializable {

    /**
    * 标签名称
    */
    @Length(max= 255,message="标签名称长度不能超过255")
    @ApiModelProperty("标签名称")
    private String tagName;

    /**
     * 标签所属商品类型
     */
    @TagTypePattern
    @ApiModelProperty("标签所属商品类型")
    private TagType type;
}

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
public class BandUpdateDTO implements Serializable {

    /**
     * 品牌id
     */
    @NotNull(message="[品牌id]不能为空")
    @ApiModelProperty("品牌id")
    private Long id;

    /**
    * 品牌名称
    */
    @Length(max= 255,message="品牌名称长度不能超过255")
    @ApiModelProperty("品牌名称")
    private String bandName;

    /**
     * 品牌所属商品类型
     */
    @ApiModelProperty("品牌所属商品类型")
    private Integer type;

    /**
     * 品牌简介
     */
    @Length(max= 512,message="简介内容长度不能超过512")
    @ApiModelProperty("品牌简介")
    private String description;
}

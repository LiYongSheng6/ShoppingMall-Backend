package com.shoppingmall.demo.model.DTO;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.shoppingmall.demo.annotation.GoodStatusPattern;
import com.shoppingmall.demo.annotation.GoodTypePattern;
import com.shoppingmall.demo.config.deserializer.StringToLongDeserializer;
import com.shoppingmall.demo.enums.GoodStatus;
import com.shoppingmall.demo.enums.GoodType;
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
public class GoodUpdateDTO implements Serializable {

    /**
     * 商品id
     */
    @JsonDeserialize(using = StringToLongDeserializer.class)
    @NotNull(message="[商品id]不能为空")
    @ApiModelProperty("商品id")
    private Long id;

    /**
     * 商品名称
     */
    @Length(max= 255,message="编码长度不能超过255")
    @ApiModelProperty("标签名称")
    private String goodName;

    /**
     * 商品封面url地址
     */
    @Length(max= 255,message="编码长度不能超过255")
    @ApiModelProperty("商品封面url地址")
    private String coverUrl;

    /**
     * 商品价格
     */
    @ApiModelProperty("商品价格")
    private Integer price;

    /**
     * 商品上架状态
     */
    @GoodStatusPattern(message = "商品状态只能为0或1")
    @ApiModelProperty("商品状态（0在售  1下架）")
    private GoodStatus status;

    /**
     * 商品类型
     */
    @GoodTypePattern
    @ApiModelProperty("商品类型")
    private GoodType type;

    /**
     * 分类id
     */
    @JsonDeserialize(using = StringToLongDeserializer.class)
    @ApiModelProperty("分类id")
    private Long categoryId;

    /**
     * 标签id
     */
    @JsonDeserialize(using = StringToLongDeserializer.class)
    @ApiModelProperty("标签id")
    private Long tagId;

    /**
     * 商品描述
     */
    @Length(max= 512,message="编码长度不能超过512")
    @ApiModelProperty("商品描述")
    private String description;

    /**
     * 商品详情
     */
    @Length(max= 512,message="编码长度不能超过512")
    @ApiModelProperty("商品详情")
    private String detail;

}

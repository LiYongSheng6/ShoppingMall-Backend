package com.shoppingmall.demo.model.DO;


import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.shoppingmall.demo.annotation.GoodStatusPattern;
import com.shoppingmall.demo.annotation.GoodTypePattern;
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

import java.io.Serializable;
import java.time.LocalDateTime;

/**
* 
* @TableName
*/
@Data
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@TableName("good")
public class GoodDO implements Serializable {

    /**
    * 商品id
    */
    @NotNull(message="[商品id]不能为空")
    @ApiModelProperty("商品id")
    private Long id;

    /**
     * 商品名称
     */
    @Length(max= 255,message="编码长度不能超过255")
    @ApiModelProperty("商品名称")
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
    @NotNull(message="[商品价格]不能为空")
    @ApiModelProperty("商品价格")
    private Integer price;

    /**
     * 商品存货数
     */
    @NotNull(message="[商品存货数]不能为空")
    @ApiModelProperty("商品存货数")
    private Integer stockNum;

    /**
     * 商品已购数
     */
    @NotNull(message="[商品已购数]不能为空")
    @ApiModelProperty("商品销量数")
    private Integer saleNum;

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
     * 创建人id
     */
    @NotNull(message="[创建人id]不能为空")
    @ApiModelProperty("创建人id")
    private Long creatorId;

    /**
     * 分类id
     */
    @NotNull(message="[分类id]不能为空")
    @ApiModelProperty("分类id")
    private Long categoryId;

    /**
     * 标签id
     */
    @NotNull(message="[标签id]不能为空")
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

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @ApiModelProperty("修改时间")
    private LocalDateTime updateTime;

}

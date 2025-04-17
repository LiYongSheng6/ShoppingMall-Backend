package com.shoppingmall.demo.model.DO;


import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
* @TableName tag
*/
@Data
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@TableName("band")
public class BandDO implements Serializable {

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
    @NotNull(message="[品牌所属商品类型]不能为空")
    @ApiModelProperty("品牌所属商品类型")
    private Integer type;

    /**
     * 品牌简介
     */
    @Length(max= 512,message="简介内容长度不能超过512")
    @ApiModelProperty("品牌简介")
    private String description;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @ApiModelProperty("修改时间")
    private LocalDateTime updateTime;

}

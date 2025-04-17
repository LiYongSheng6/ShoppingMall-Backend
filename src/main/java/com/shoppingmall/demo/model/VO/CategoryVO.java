package com.shoppingmall.demo.model.VO;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.shoppingmall.demo.model.DO.CategoryDO;
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
* @TableName tag
*/
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
    * 分类名称
    */
    @ApiModelProperty("分类名称")
    private String categoryName;

    /**
     * 分类所属商品类型
     */
    @ApiModelProperty("分类所属商品类型")
    private Integer type;

    public CategoryVO(CategoryDO categoryDO){
        BeanUtil.copyProperties(categoryDO, this);
    }

}

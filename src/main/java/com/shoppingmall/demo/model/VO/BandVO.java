package com.shoppingmall.demo.model.VO;


import cn.hutool.core.bean.BeanUtil;
import com.shoppingmall.demo.model.DO.BandDO;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
* 
* @TableName tag
*/
@Data
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class BandVO implements Serializable {

    /**
    * 品牌id
    */
    @ApiModelProperty("id")
    private String id;

    /**
    * 品牌名
   */
    @ApiModelProperty("品牌名")
    private String tagName;

    /**
     * 分类所属商品类型
     */
    @ApiModelProperty("分类所属商品类型")
    private Integer type;

    /**
     * 品牌简介
     */
    @ApiModelProperty("品牌简介")
    private String description;

    public BandVO(BandDO bandDO){
        BeanUtil.copyProperties(bandDO,this);
    }

}

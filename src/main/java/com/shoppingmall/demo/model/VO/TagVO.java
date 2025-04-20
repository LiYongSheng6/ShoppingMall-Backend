package com.shoppingmall.demo.model.VO;


import cn.hutool.core.bean.BeanUtil;
import com.shoppingmall.demo.model.DO.TagDO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class TagVO implements Serializable {

    /**
    * 标签id
    */
    @ApiModelProperty("id")
    private String id;

    /**
    * 标签名
   */
    @ApiModelProperty("标签名")
    private String tagName;

    /**
     * 分类所属商品类型
     */
    @ApiModelProperty("分类所属商品类型")
    private Integer type;

    public TagVO(TagDO tagDO){
        BeanUtil.copyProperties(tagDO,this);
    }

}

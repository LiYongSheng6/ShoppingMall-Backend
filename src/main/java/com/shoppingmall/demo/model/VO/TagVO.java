package com.shoppingmall.demo.model.VO;


import cn.hutool.core.bean.BeanUtil;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.shoppingmall.demo.enums.TagType;
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
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty("id")
    private Long id;

    /**
    * 标签名
   */
    @ApiModelProperty("标签名")
    private String tagName;

    /**
     * 分类所属商品类型
     */
    @ApiModelProperty("分类所属商品类型")
    private TagType type;

    public TagVO(TagDO tagDO){
        BeanUtil.copyProperties(tagDO,this);
    }

}

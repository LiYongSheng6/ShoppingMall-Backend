package com.shoppingmall.demo.model.VO;


import cn.hutool.core.bean.BeanUtil;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.shoppingmall.demo.enums.GoodStatus;
import com.shoppingmall.demo.enums.GoodType;
import com.shoppingmall.demo.model.DO.GoodDO;
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
public class GoodVO implements Serializable {

    /**
     * 商品id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty("商品id")
    private Long id;

    /**
     * 商品名称
     */
    @ApiModelProperty("标签名称")
    private String goodName;

    /**
     * 商品封面url地址
     */
    @ApiModelProperty("商品封面url地址")
    private String coverUrl;

    /**
     * 商品价格
     */
    @ApiModelProperty("商品价格")
    private Integer price;

    /**
     * 商品存货数
     */
    @ApiModelProperty("商品存货数")
    private Integer stockNum;

    /**
     * 商品已购数
     */
    @ApiModelProperty("商品销量数")
    private Integer saleNum;

    /**
     * 商品上架状态
     */
    @ApiModelProperty("商品状态（0在售  1下架）")
    private GoodStatus status;

    /**
     * 商品类型
     */
    @ApiModelProperty("商品类型")
    private GoodType type;

    /**
     * 创建人id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty("创建人id")
    private Long creatorId;

    /**
     * 创建人名称
     */
    @ApiModelProperty("创建人名称")
    private String creatorName;

    /**
     * 创建人头像
     */
    @ApiModelProperty("创建人头像")
    private String creatorAvatar;

    /**
     * 分类名称
     */
    @ApiModelProperty("分类名称")
    private String categoryName;

    /**
     * 标签名称
     */
    @ApiModelProperty("标签名称")
    private String tagName;

    /**
     * 商品描述
     */
    @ApiModelProperty("商品描述")
    private String description;

    /**
     * 商品详情
     */
    @ApiModelProperty("商品详情")
    private String detail;

    public GoodVO(GoodDO goodDO){
        BeanUtil.copyProperties(goodDO,this);
    }

}

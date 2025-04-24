package com.shoppingmall.demo.model.Query;

import com.shoppingmall.demo.enums.GoodRankType;
import com.shoppingmall.demo.enums.GoodStatus;
import com.shoppingmall.demo.enums.GoodType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class GoodQuery extends PageQuery{

    @ApiModelProperty("商品名称")
    private String goodName;

    @ApiModelProperty("商品类型")
    private GoodType type;

    @ApiModelProperty("商品榜单类型")
    private GoodRankType rankType;

    @ApiModelProperty("商品状态")
    private GoodStatus status;

    @ApiModelProperty("创建者id")
    private Long creatorId;

    @ApiModelProperty("分类id")
    private List<Long> categoryIds;

    @ApiModelProperty("标签id")
    private List<Long> tagIds;

    @ApiModelProperty("最低价格")
    private Integer minimizePrice;

    @ApiModelProperty("最高价格")
    private Integer maximizePrice ;

}

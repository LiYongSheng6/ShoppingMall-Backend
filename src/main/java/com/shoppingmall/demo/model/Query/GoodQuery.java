package com.shoppingmall.demo.model.Query;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.shoppingmall.demo.config.deserializer.StringListToLongListDeserializer;
import com.shoppingmall.demo.config.deserializer.StringToLongDeserializer;
import com.shoppingmall.demo.enums.GoodRankType;
import com.shoppingmall.demo.enums.GoodStatus;
import com.shoppingmall.demo.enums.GoodType;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.Min;
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

    @JsonDeserialize(using = StringToLongDeserializer.class)
    @ApiModelProperty("创建者id")
    private Long creatorId;

    @JsonDeserialize(using = StringListToLongListDeserializer.class)
    @ApiModelProperty("分类id")
    private List<Long> categoryIds;

    @JsonDeserialize(using = StringListToLongListDeserializer.class)
    @ApiModelProperty("标签id")
    private List<Long> tagIds;

    @ApiModelProperty("最低价格")
    @Min(value = 0, message = "价格不能为负数")
    private Integer minimizePrice;

    @ApiModelProperty("最高价格")
    @Min(value = 0, message = "价格不能为负数")
    private Integer maximizePrice ;

}

package com.shoppingmall.demo.model.Query;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.shoppingmall.demo.config.deserializer.StringToLongDeserializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
* 
* @TableName tag
*/
@Data
@Accessors(chain = true)
public class OrderQuery extends PageQuery {

    @JsonDeserialize(using = StringToLongDeserializer.class)
    @ApiModelProperty("用户id")
    private Long userId;

    @JsonDeserialize(using = StringToLongDeserializer.class)
    @ApiModelProperty("商家id")
    private Long businessId;

    @ApiModelProperty("订单状态（0待支付  1待发货  2待收货  3已完成  4已取消）")
    private Integer status;

}

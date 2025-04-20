package com.shoppingmall.demo.model.Query;

import com.shoppingmall.demo.model.Query.PageQuery;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
* 
* @TableName tag
*/
@Data
public class OrderQuery extends PageQuery {

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("订单状态（0待支付  1待发货  2待收货  3已完成  4已取消）")
    private Integer status;

}

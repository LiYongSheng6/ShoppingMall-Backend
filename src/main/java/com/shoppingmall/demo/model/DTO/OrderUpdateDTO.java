package com.shoppingmall.demo.model.DTO;

import com.shoppingmall.demo.annotation.OrderStatusPattern;
import com.shoppingmall.demo.enums.OrderStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.validation.annotation.Validated;

import java.io.Serializable;

/**
* 
* @TableName tag
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Validated
@Accessors(chain = true)
public class OrderUpdateDTO implements Serializable {

    @NotNull(message="[订单id]不能为空")
    @ApiModelProperty("订单id")
    private Long id;

    @NotNull(message="[用户id]不能为空")
    @ApiModelProperty("用户id")
    private Long userId;

    @NotNull(message="[商品id]不能为空")
    @ApiModelProperty("商品id")
    private Long goodId;

    @NotNull(message="[收货信息id]不能为空")
    @ApiModelProperty("收货信息id")
    private Long deliveryId;

    @NotNull(message="[商品购买数]不能为空")
    @ApiModelProperty("商品购买数")
    private Integer purchaseNum;

    @OrderStatusPattern
    @ApiModelProperty("订单状态（0待支付  1待发货  2待收货  3已完成  4已取消）")
    private OrderStatus status;

}

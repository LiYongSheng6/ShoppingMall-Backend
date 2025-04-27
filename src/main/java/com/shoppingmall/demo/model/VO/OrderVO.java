package com.shoppingmall.demo.model.VO;

import cn.hutool.core.bean.BeanUtil;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.shoppingmall.demo.enums.OrderStatus;
import com.shoppingmall.demo.model.DO.OrderDO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

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
public class OrderVO implements Serializable {

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty("订单id")
    private Long id;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("商家id")
    private Long businessId;

    @ApiModelProperty("商品id")
    private Long goodId;

    @ApiModelProperty("收货信息id")
    private Long deliveryId;

    @ApiModelProperty("用户名称")
    private String username;

    @ApiModelProperty("商家名称")
    private String businessName;

    @ApiModelProperty("商品名称")
    private String goodName;

    @ApiModelProperty("商品封面")
    private String coverUrl;

    @ApiModelProperty("商品价格")
    private Integer price;

    @ApiModelProperty("商品购买数")
    private Integer purchaseNum;

    @ApiModelProperty("订单总价")
    private Integer total;

    @ApiModelProperty("收货人姓名")
    private String consigneeName;

    @ApiModelProperty("收货人电话")
    private String phone;

    @ApiModelProperty("收货地址-省份")
    private String province;

    @ApiModelProperty("收货地址-城市")
    private String city;

    @ApiModelProperty("收货地址-区县")
    private String county;

    @ApiModelProperty("详细地址")
    private String address;

    @ApiModelProperty("订单状态（0待支付  1待发货  2待收货  3已完成  4已取消）")
    private OrderStatus status;

    public OrderVO(OrderDO orderDO){
        BeanUtil.copyProperties(orderDO,this);
    }

}

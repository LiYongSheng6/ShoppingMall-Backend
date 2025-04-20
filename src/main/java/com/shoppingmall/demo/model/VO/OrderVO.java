package com.shoppingmall.demo.model.VO;

import cn.hutool.core.bean.BeanUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.shoppingmall.demo.model.DO.OrderDO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

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

    @ApiModelProperty("订单id")
    private String id;

    @ApiModelProperty("用户名称")
    private String username;

    @ApiModelProperty("商品名称")
    private String goodName;

    @ApiModelProperty("商品价格")
    private Integer price;

    @ApiModelProperty("商品购买数")
    private Integer purchaseNum;

    @ApiModelProperty("订单总价")
    private Integer totalPrice;

    @ApiModelProperty("收货人姓名")
    private String consigneeName;

    @ApiModelProperty("收货人电话")
    private String phone;

    @ApiModelProperty("详细地址")
    private String detailAddress;

    @ApiModelProperty("订单状态（0待支付  1待发货  2待收货  3已完成  4已取消）")
    private Integer status;

    public OrderVO(OrderDO orderDO){
        BeanUtil.copyProperties(orderDO,this);
    }

}

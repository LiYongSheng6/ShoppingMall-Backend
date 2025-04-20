package com.shoppingmall.demo.model.DO;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.shoppingmall.demo.annotation.OrderStatusPattern;
import com.shoppingmall.demo.enums.OrderStatus;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotNull;
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
@TableName("order")
public class OrderDO implements Serializable {

    /**
     * 订单id
     */
    @NotNull(message="[订单id]不能为空")
    @ApiModelProperty("订单id")
    private Long id;

    /**
     * 用户id
     */
    @NotNull(message="[用户id]不能为空")
    @ApiModelProperty("用户id")
    private Long userId;

    /**
     * 商品id
     */
    @NotNull(message="[商品id]不能为空")
    @ApiModelProperty("商品id")
    private Long goodId;

    /**
     * 收货信息id
     */
    @NotNull(message="[收货信息id]不能为空")
    @ApiModelProperty("收货信息id")
    private Long deliveryId;

    /**
     * 商品购买数
     */
    @NotNull(message="[商品购买数]不能为空")
    @ApiModelProperty("商品购买数")
    private Integer purchaseNum;

    /**
     * 订单总价
     */
    @ApiModelProperty("订单总价")
    private Integer totalPrice;

    /**
     * 订单状态
     */
    @OrderStatusPattern
    @ApiModelProperty("订单状态（0待支付  1待发货  2待收货  3已完成  4已取消）")
    private OrderStatus status;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @ApiModelProperty("修改时间")
    private LocalDateTime updateTime;


}



package com.shoppingmall.demo.model.DO;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.shoppingmall.demo.annotation.OrderStatusPattern;
import com.shoppingmall.demo.constant.MessageConstants;
import com.shoppingmall.demo.constant.RegexConstants;
import com.shoppingmall.demo.enums.OrderStatus;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

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
@TableName("orders")
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
     * 商品名称
     */
    @Length(max = 255, message = "编码长度不能超过255")
    @ApiModelProperty("商品名称")
    private String goodName;

    /**
     * 商品封面url地址
     */
    @Length(max = 255, message = "编码长度不能超过255")
    @ApiModelProperty("商品封面url地址")
    private String coverUrl;

    /**
     * 商品价格
     */
    @NotNull(message = "[商品价格]不能为空")
    @ApiModelProperty("商品价格")
    private Integer price;

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
    private Integer total;

    /**
     * 收货人姓名
     */
    @Length(max = 255, message = "收货人姓名长度不能超过255")
    @ApiModelProperty("收货人姓名")
    private String consigneeName;

    /**
     * 收货人电话
     */
    @Pattern(regexp = RegexConstants.PHONE_REGEX, message = MessageConstants.PHONE_REGEX_MESSAGE)
    @ApiModelProperty("收货人电话")
    private String phone;

    /**
     * 省份
     */
    @Length(max = 255, message = "省份名称长度不能超过255")
    @ApiModelProperty("省份")
    private String province;

    /**
     * 城市
     */
    @Length(max = 255, message = "城市名称长度不能超过255")
    @ApiModelProperty("城市")
    private String city;

    /**
     * 区县
     */
    @Length(max = 255, message = "区县名称长度不能超过255")
    @ApiModelProperty("区县")
    private String county;

    /**
     * 详细地址
     */
    @Length(max = 512, message = "详细地址长度不能超过512")
    @ApiModelProperty("详细地址")
    private String address;

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



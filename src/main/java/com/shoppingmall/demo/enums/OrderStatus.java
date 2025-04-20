package com.shoppingmall.demo.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum OrderStatus {
    //订单状态（0待支付  1待发货  2待收货  3已完成  4已取消）
    PAYING(0,"待支付"),
    Shipping(1,"待发货"),
    Receiving(2,"待收货"),
    COMPLETED(3,"已完成"),
    CANCELLED(4,"已取消");


    @EnumValue
    private final Integer value;
    @JsonValue
    private final String desc;

    OrderStatus(Integer value, String desc){
        this.value=value;
        this.desc=desc;
    }
}

package com.shoppingmall.demo.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum AddressType {
    PROVINCE(0, "省份"),
    CITY(1, "城市"),
    COUNTY(2, "区县");


    @EnumValue
    private final Integer value;
    @JsonValue
    private final String desc;

    AddressType(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}

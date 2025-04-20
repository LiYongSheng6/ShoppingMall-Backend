package com.shoppingmall.demo.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum GoodStatus {
    SALE(0,"在售"),
    REMOVE(1,"下架");

    @EnumValue
    private final Integer value;
    @JsonValue
    private final String desc;

    GoodStatus(Integer value, String desc){
        this.value=value;
        this.desc=desc;
    }
}

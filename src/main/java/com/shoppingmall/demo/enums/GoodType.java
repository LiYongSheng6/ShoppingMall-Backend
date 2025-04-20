package com.shoppingmall.demo.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum GoodType {
    IP(0,"IP周边"),
    HOT(1,"热销爆品"),
    DIGITAL(2,"数码影音"),
    ALBUM(3,"数字专辑"),
    SONG(4,"数字单曲");


    @EnumValue
    private final Integer value;
    @JsonValue
    private final String desc;

    GoodType(Integer value, String desc){
        this.value=value;
        this.desc=desc;
    }
}

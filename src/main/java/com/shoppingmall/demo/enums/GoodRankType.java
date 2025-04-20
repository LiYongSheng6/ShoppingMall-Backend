package com.shoppingmall.demo.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum GoodRankType {
    DAY(0,"日榜"),
    WEEK(1,"周榜"),
    YEAR(2,"年榜"),
    OVERALL(3,"总榜");

    @EnumValue
    private final Integer value;
    @JsonValue
    private final String desc;

    GoodRankType(Integer value, String desc){
        this.value=value;
        this.desc=desc;
    }
}

package com.shoppingmall.demo.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum GenderType {
    WOMAN(0, "女"),
    MAN(1, "男");
    @EnumValue
    private Integer value;
    @JsonValue
    private String desc;
    GenderType(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}

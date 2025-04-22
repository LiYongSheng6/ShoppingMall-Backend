package com.shoppingmall.demo.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum TagType {
    NORMAL(0, "普通"),
    OTHER(1, "其他");

    @EnumValue
    private final Integer value;
    @JsonValue
    private final String desc;

    TagType(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}

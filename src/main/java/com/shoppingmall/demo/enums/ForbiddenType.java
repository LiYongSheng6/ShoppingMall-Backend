package com.shoppingmall.demo.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum ForbiddenType {
    FALSE(0, "未封禁"),
    TRUE(1, "已封禁");
    @EnumValue
    private Integer value;
    @JsonValue
    private String desc;
    ForbiddenType(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}

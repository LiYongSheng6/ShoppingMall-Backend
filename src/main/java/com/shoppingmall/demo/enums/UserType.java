package com.shoppingmall.demo.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum UserType {
    USER(0, "USER"),
    MERCHANT(1, "MERCHANT"),
    ADMIN(2, "ADMIN"),
    SUPER_ADMIN(3, "SUPER_ADMIN");

    @EnumValue
    private Integer value;
    @JsonValue
    private String desc;
    UserType(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}

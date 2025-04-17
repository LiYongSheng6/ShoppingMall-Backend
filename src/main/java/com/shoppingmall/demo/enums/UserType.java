package com.shoppingmall.demo.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum UserType {
    USER(0, "普通"),
    ADMIN(1, "管理");
    @EnumValue
    private Integer value;
    @JsonValue
    private String desc;
    UserType(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}

package com.shoppingmall.demo.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum PermissionType {
    BACKEND(0, "后端"),
    FRONT(1, "前端");

    @EnumValue
    private Integer value;
    @JsonValue
    private String desc;

    PermissionType(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}

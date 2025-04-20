package com.shoppingmall.demo.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum MessageStatus {
    UNREAD(0, "未读"),
    READ(1, "已读");

    @EnumValue
    private final Integer value;
    @JsonValue
    private final String desc;

    MessageStatus(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

}

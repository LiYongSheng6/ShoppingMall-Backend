package com.shoppingmall.demo.validator;

import com.shoppingmall.demo.annotation.GoodStatusPattern;
import com.shoppingmall.demo.annotation.OrderStatusPattern;
import com.shoppingmall.demo.constant.RegexConstants;
import com.shoppingmall.demo.enums.GoodStatus;
import com.shoppingmall.demo.enums.OrderStatus;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class OrderStatusPatternValidator implements ConstraintValidator<OrderStatusPattern, OrderStatus> {

    private static final String PATTERN = RegexConstants.ORDER_STATUS_REGEX; // 自定义正则表达式

    @Override
    public void initialize(OrderStatusPattern constraintAnnotation) {
    }

    @Override
    public boolean isValid(OrderStatus value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // 允许空值，或根据需求调整
        }
        return value.getValue().toString().matches(PATTERN);
    }
}
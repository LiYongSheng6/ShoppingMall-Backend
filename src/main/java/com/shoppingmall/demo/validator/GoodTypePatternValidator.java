package com.shoppingmall.demo.validator;

import com.shoppingmall.demo.annotation.GoodTypePattern;
import com.shoppingmall.demo.annotation.UserTypePattern;
import com.shoppingmall.demo.constant.RegexConstants;
import com.shoppingmall.demo.enums.GoodType;
import com.shoppingmall.demo.enums.UserType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class GoodTypePatternValidator implements ConstraintValidator<GoodTypePattern, GoodType> {

    private static final String PATTERN = RegexConstants.GOOD_TYPE_REGEX; // 自定义正则表达式

    @Override
    public void initialize(GoodTypePattern constraintAnnotation) {
    }

    @Override
    public boolean isValid(GoodType value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // 允许空值，或根据需求调整
        }
        return value.getValue().toString().matches(PATTERN);
    }
}
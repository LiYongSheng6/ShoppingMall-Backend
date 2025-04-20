package com.shoppingmall.demo.validator;

import com.shoppingmall.demo.annotation.GenderPattern;
import com.shoppingmall.demo.annotation.GoodStatusPattern;
import com.shoppingmall.demo.constant.RegexConstants;
import com.shoppingmall.demo.enums.GenderType;
import com.shoppingmall.demo.enums.GoodStatus;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class GoodStatusPatternValidator implements ConstraintValidator<GoodStatusPattern, GoodStatus> {

    private static final String PATTERN = RegexConstants.GOOD_STATUS_REGEX; // 自定义正则表达式

    @Override
    public void initialize(GoodStatusPattern constraintAnnotation) {
    }

    @Override
    public boolean isValid(GoodStatus value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // 允许空值，或根据需求调整
        }
        return value.getValue().toString().matches(PATTERN);
    }
}
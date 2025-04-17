package com.shoppingmall.demo.validator;

import com.shoppingmall.demo.annotation.GenderPattern;
import com.shoppingmall.demo.constant.CacheConstants;
import com.shoppingmall.demo.enums.GenderType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class GenderPatternValidator implements ConstraintValidator<GenderPattern, GenderType> {

    private static final String PATTERN = CacheConstants.GENDER_REGEX; // 自定义正则表达式

    @Override
    public void initialize(GenderPattern constraintAnnotation) {
    }

    @Override
    public boolean isValid(GenderType value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // 允许空值，或根据需求调整
        }
        return value.getValue().toString().matches(PATTERN);
    }
}
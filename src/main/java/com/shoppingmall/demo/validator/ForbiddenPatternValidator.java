package com.shoppingmall.demo.validator;

import com.shoppingmall.demo.annotation.ForbiddenPattern;
import com.shoppingmall.demo.constant.RegexConstants;
import com.shoppingmall.demo.enums.ForbiddenType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ForbiddenPatternValidator implements ConstraintValidator<ForbiddenPattern, ForbiddenType> {

    private static final String PATTERN = RegexConstants.FORBIDDEN_REGEX; // 自定义正则表达式

    @Override
    public void initialize(ForbiddenPattern constraintAnnotation) {
    }

    @Override
    public boolean isValid(ForbiddenType value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) {
            return true; // 允许空值，或根据需求调整
        }
        return value.getValue().toString().matches(PATTERN);
    }

}
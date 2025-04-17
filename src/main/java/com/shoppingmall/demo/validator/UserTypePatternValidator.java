package com.shoppingmall.demo.validator;

import com.shoppingmall.demo.annotation.UserTypePattern;
import com.shoppingmall.demo.constant.RegexConstants;
import com.shoppingmall.demo.enums.UserType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UserTypePatternValidator implements ConstraintValidator<UserTypePattern, UserType> {

    private static final String PATTERN = RegexConstants.USER_TYPE_REGEX; // 自定义正则表达式

    @Override
    public void initialize(UserTypePattern constraintAnnotation) {
    }

    @Override
    public boolean isValid(UserType value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // 允许空值，或根据需求调整
        }
        return value.getValue().toString().matches(PATTERN);
    }
}
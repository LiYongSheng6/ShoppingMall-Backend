package com.shoppingmall.demo.validator;

import com.shoppingmall.demo.annotation.PermissionTypePattern;
import com.shoppingmall.demo.constant.RegexConstants;
import com.shoppingmall.demo.enums.PermissionType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PermissionTypePatternValidator implements ConstraintValidator<PermissionTypePattern, PermissionType> {

    private static final String PATTERN = RegexConstants.PERMISSION_TYPE_REGEX; // 自定义正则表达式

    @Override
    public void initialize(PermissionTypePattern constraintAnnotation) {
    }

    @Override
    public boolean isValid(PermissionType value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // 允许空值，或根据需求调整
        }
        return value.getValue().toString().matches(PATTERN);
    }
}
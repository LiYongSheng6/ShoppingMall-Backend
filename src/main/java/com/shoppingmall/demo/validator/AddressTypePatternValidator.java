package com.shoppingmall.demo.validator;

import com.shoppingmall.demo.annotation.AddressTypePattern;
import com.shoppingmall.demo.constant.RegexConstants;
import com.shoppingmall.demo.enums.AddressType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AddressTypePatternValidator implements ConstraintValidator<AddressTypePattern, AddressType> {

    private static final String PATTERN = RegexConstants.ADDRESS_TYPE_REGEX; // 自定义正则表达式

    @Override
    public void initialize(AddressTypePattern constraintAnnotation) {
    }

    @Override
    public boolean isValid(AddressType value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // 允许空值，或根据需求调整
        }
        return value.getValue().toString().matches(PATTERN);
    }
}
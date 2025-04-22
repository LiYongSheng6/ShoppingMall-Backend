package com.shoppingmall.demo.validator;

import com.shoppingmall.demo.annotation.CategoryTypePattern;
import com.shoppingmall.demo.constant.RegexConstants;
import com.shoppingmall.demo.enums.CategoryType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CategoryTypePatternValidator implements ConstraintValidator<CategoryTypePattern, CategoryType> {

    private static final String PATTERN = RegexConstants.ADDRESS_TYPE_REGEX; // 自定义正则表达式

    @Override
    public void initialize(CategoryTypePattern constraintAnnotation) {
    }

    @Override
    public boolean isValid(CategoryType value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // 允许空值，或根据需求调整
        }
        return value.getValue().toString().matches(PATTERN);
    }
}
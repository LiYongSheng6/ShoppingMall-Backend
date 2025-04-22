package com.shoppingmall.demo.validator;

import com.shoppingmall.demo.annotation.TagTypePattern;
import com.shoppingmall.demo.constant.RegexConstants;
import com.shoppingmall.demo.enums.TagType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TagTypePatternValidator implements ConstraintValidator<TagTypePattern, TagType> {

    private static final String PATTERN = RegexConstants.ADDRESS_TYPE_REGEX; // 自定义正则表达式

    @Override
    public void initialize(TagTypePattern constraintAnnotation) {
    }

    @Override
    public boolean isValid(TagType value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // 允许空值，或根据需求调整
        }
        return value.getValue().toString().matches(PATTERN);
    }
}
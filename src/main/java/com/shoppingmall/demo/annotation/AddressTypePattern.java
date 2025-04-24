package com.shoppingmall.demo.annotation;

import com.shoppingmall.demo.constant.MessageConstants;
import com.shoppingmall.demo.validator.AddressTypePatternValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = AddressTypePatternValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AddressTypePattern {
    String message() default MessageConstants.ADDRESS_TYPE_REGEX_MESSAGE;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

package com.shoppingmall.demo.annotation;

import com.shoppingmall.demo.constant.MessageConstants;
import com.shoppingmall.demo.validator.GenderPatternValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = GenderPatternValidator.class)
@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface GenderPattern {
    String message() default MessageConstants.GENDER_REGEX_MESSAGE;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

package com.shoppingmall.demo.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 后端接口权限资源鉴权
 * @author redmi k50 ultra
 * * @date 2024/8/3
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PreAuthorize {
    /**
     * 权限标识符
     * @return
     */
    public String value() default "";
}

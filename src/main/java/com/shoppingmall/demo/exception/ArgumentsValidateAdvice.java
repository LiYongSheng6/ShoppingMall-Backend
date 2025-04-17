package com.shoppingmall.demo.exception;


import com.shoppingmall.demo.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Objects;

/**
 * 全局参数异常处理器
 * @author redmi k50 ultra
 * * @date 2024/7/20
 */
@ControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE - 2)
@Slf4j
public class ArgumentsValidateAdvice {
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<String> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        log.error("发生参数异常！ msg : -> "+e.getMessage(), e);
        return Result.error(com.shoppingmall.demo.constant.HttpStatus.BAD_REQUEST, Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage());
    }
}

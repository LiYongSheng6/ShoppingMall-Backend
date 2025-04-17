package com.shoppingmall.demo.exception;

import com.shoppingmall.demo.utils.Result;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 * @author redmi k50 ultra
 * * @date 2024/7/19
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 业务异常
     */
    @ExceptionHandler(ServiceException.class)
    public Result<String> handleServiceException(ServiceException e, HttpServletRequest request) {
        log.info("请求地址：{}，业务异常信息：{}", request.getRequestURI(), e.getMessage());
        return Result.error(e.getCode() , e.getMessage());
    }

    /**
     *  参数校验异常
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Result<String> missParamException(MissingServletRequestParameterException e, HttpServletRequest request) {
        log.info("请求地址：{}，校验异常信息：{}", request.getRequestURI(), e.getMessage());
        return Result.error(HttpStatus.BAD_REQUEST.value(), e.getMessage().isEmpty()?e.getMessage():"参数格式有误");
    }

    /**
     * 服务器异常(兜底方案)
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<String> handleServerException(Exception e) {
        log.error("服务器异常！ msg: -> "+e.getMessage(), e);
        return Result.error(StringUtils.hasLength(e.getMessage())? e.getMessage() : "服务器异常！");
    }


}

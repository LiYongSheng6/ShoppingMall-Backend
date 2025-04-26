package com.shoppingmall.demo.utils;

import com.shoppingmall.demo.constant.HttpStatus;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Result<T> {
    private Integer code;
    private String msg;
    private T data;

    public static <T> Result<T> success() {
        Result<T> result = new Result<>();
        result.code = HttpStatus.SUCCESS;
        result.msg = "success";
        return result;
    }

    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.code = HttpStatus.SUCCESS;
        result.msg = "success";
        result.data = data;
        return result;
    }

    public static <T> Result<T> success(String msg, T data) {
        Result<T> result = new Result<>();
        result.code = HttpStatus.SUCCESS;
        result.msg = msg;
        result.data = data;
        return result;
    }

    public static <T> Result<T> error() {
        Result<T> result = new Result<>();
        result.code = HttpStatus.ERROR;
        result.msg = "error";
        return result;
    }

    public static <T> Result<T> error(String msg) {
        Result<T> result = new Result<>();
        result.code = HttpStatus.ERROR;
        result.msg = msg;
        return result;
    }

    public static <T> Result<T> error(Integer code, String msg) {
        Result<T> result = new Result<>();
        result.code = code;
        result.msg = msg;
        return result;
    }

    public static <T> Result<T> error(Integer code, String msg, T data) {
        Result<T> result = new Result<>();
        result.code = code;
        result.msg = msg;
        result.data = data;
        return result;
    }

    public static <T> Result<T> unauthorized() {
        Result<T> result = new Result<>();
        result.code = HttpStatus.UNAUTHORIZED;
        result.msg = "unauthorized";
        return result;
    }

}

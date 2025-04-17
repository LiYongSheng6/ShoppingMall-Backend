package com.shoppingmall.demo.exception;


import com.shoppingmall.demo.constant.HttpStatus;

/**
 * @author：Hikko
 * @date: 2023/05/23
 * @time: 22:52
 */

public class ServiceException extends RuntimeException {

    private int code;
    private String message;

    public ServiceException() {
    }

    public ServiceException(String message) {
        this.code = HttpStatus.ERROR;
        this.message = message;
    }

    public ServiceException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public ServiceException(String message, Integer code) {
        this.message = message;
        this.code = code;
    }

    public static void fail(String message) {
        throw new ServiceException(message);
    }

    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

}

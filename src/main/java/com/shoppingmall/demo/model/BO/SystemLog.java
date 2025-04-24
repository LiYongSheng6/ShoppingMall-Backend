package com.shoppingmall.demo.model.BO;

import lombok.Data;

@Data
public class SystemLog {

    private Long userId;

    private String url;

    private String classMethod;

    private String requestMethod;

    private String ip;

    private Long responseTime;

    private String nickName;

    private Object[] params;

    private Object result;

    private Integer status;

    private String errMsg;

    private String node;

}

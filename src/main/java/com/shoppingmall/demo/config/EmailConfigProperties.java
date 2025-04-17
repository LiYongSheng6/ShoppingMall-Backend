package com.shoppingmall.demo.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author redmi k50 ultra
 * * @date 2024/7/17
 */
@ConfigurationProperties(prefix = "email")
@Component
@Data
public class EmailConfigProperties {
    /**
     * 发件人邮箱
     */
    @Value("${email.user}")
    public String user ;

    /**
     * 发件人邮箱授权码
     */
    @Value("${email.code}")
    public String code ;

    /**
     * 发件人邮箱对应服务器域名，如163邮箱：smtp.163.com   QQ邮箱：smtp.qq.com
     */
    @Value("${email.host}")
    public String host ;

    /**
     * 身份验证开关
     */
    @Value("${email.auth}")
    public String auth ;

}




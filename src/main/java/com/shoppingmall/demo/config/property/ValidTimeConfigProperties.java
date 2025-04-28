package com.shoppingmall.demo.config.property;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 验证码有效时间(秒)
 *
 * @author redmi k50 ultra
 * * @date 2024/7/20
 */

@ConfigurationProperties(prefix = "time")
@Component
@Data
public class ValidTimeConfigProperties {
    /**
     * 图片验证码有效时间
     */
    @Value("${time.imageCode}")
    public String imageCode;

    /**
     * 邮箱验证码有效时间
     */
    @Value("${time.emailCode}")
    public String emailCode;

    /**
     * 手机验证码有效时间
     */
    @Value("${time.phoneCode}")
    public String phoneCode;


}

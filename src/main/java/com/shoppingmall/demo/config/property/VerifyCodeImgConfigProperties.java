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

@ConfigurationProperties(prefix = "code-image")
@Component
@Data
public class VerifyCodeImgConfigProperties {
    /**
     * 验证码图片宽度
     */
    @Value("${code-image.width}")
    public String width;

    /**
     * 验证码图片高度
     */
    @Value("${code-image.height}")
    public String height;

    /**
     * 验证码字符长度
     */
    @Value("${code-image.length}")
    public String length;


}

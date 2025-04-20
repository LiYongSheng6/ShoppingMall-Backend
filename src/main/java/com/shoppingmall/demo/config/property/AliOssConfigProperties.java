package com.shoppingmall.demo.config.property;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author redmi k50 ultra
 * * @date 2024/11/3
 */

@ConfigurationProperties(prefix = "oss")
@Component
@Data
public class AliOssConfigProperties {
    /**
     * 节点域名
     */
    @Value("${oss.endpoint}")
    public String endpoint;

    /**
     * 通行ID
     */
    @Value("${oss.access-key-id}")
    public String accessKeyId;

    /**
     * 通行密钥
     */
    @Value("${oss.access-key-secret}")
    public String accessKeySecret;

    /**
     * bucket名称
     */
    @Value("${oss.bucket-name}")
    public String bucketName;
}

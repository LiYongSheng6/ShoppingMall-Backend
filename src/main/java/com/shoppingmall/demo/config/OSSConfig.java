package com.shoppingmall.demo.config;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.shoppingmall.demo.config.property.AliOssConfigProperties;
import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class OSSConfig {

    private final AliOssConfigProperties aliOssConfigProperties;

    @Bean
    public OSS ossClient() {
        //String endpoint = "oss-cn-guangzhou.aliyuncs.com"; // 替换为实际的OSS Endpoint
        //String accessKeyId = "LTAI5t93nD2P4CaZZCL5kHAe"; // 替换为实际的Access Key ID
        //String accessKeySecret = "YtZV2r0hDLT3C0KhyHWSG6scMDIdiM"; // 替换为实际的Access Key Secret

        String endpoint = aliOssConfigProperties.endpoint; // 替换为实际的OSS Endpoint
        String accessKeyId = aliOssConfigProperties.accessKeyId; // 替换为实际的Access Key ID
        String accessKeySecret = aliOssConfigProperties.accessKeySecret; // 替换为实际的Access Key Secret

        return new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
    }
}

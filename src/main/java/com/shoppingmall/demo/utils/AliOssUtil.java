package com.shoppingmall.demo.utils;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.shoppingmall.demo.config.property.AliOssConfigProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Slf4j
@RequiredArgsConstructor
@Component
public class AliOssUtil {

    private final AliOssConfigProperties aliOssConfigProperties;

    public String uploadFile(String objectName, InputStream in) throws Exception {


        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(aliOssConfigProperties.endpoint, aliOssConfigProperties.accessKeyId , aliOssConfigProperties.accessKeySecret);
        String url="";

        try {
            // 创建PutObjectRequest对象。
            PutObjectRequest putObjectRequest = new PutObjectRequest(aliOssConfigProperties.bucketName, objectName, in);

            // 上传字符串。
            PutObjectResult result = ossClient.putObject(putObjectRequest);
            url = "https://"+aliOssConfigProperties.bucketName +"."+aliOssConfigProperties.endpoint.substring(aliOssConfigProperties.endpoint.lastIndexOf("/")+1)+"/"+objectName;
        } catch (OSSException oe) {
            log.error("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            log.error("Error Message:{}" , oe.getErrorMessage());
            log.error("Error Code:{}",  oe.getErrorCode());
            log.error("Request ID:{}" , oe.getRequestId());
            log.error("Host ID:{}" , oe.getHostId());
        } catch (ClientException ce) {
            log.error("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            log.error("Error Message:{}",  ce.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        return url;
    }
}


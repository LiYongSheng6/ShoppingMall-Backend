package com.shoppingmall.demo.controller;


import com.aliyun.oss.OSS;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import com.shoppingmall.demo.annotation.PreAuthorize;
import com.shoppingmall.demo.config.property.AliOssConfigProperties;
import com.shoppingmall.demo.constant.MessageConstants;
import com.shoppingmall.demo.utils.AliOssUtil;
import com.shoppingmall.demo.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

@Api(tags = "文件上传存储接口")
@Validated
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/upload")
public class FileUploadController {

    private final OSS ossClient;
    private final AliOssUtil aliOssUtil;
    private final AliOssConfigProperties aliOssConfigProperties;

    @Operation(summary = "获取前端图片上传签名接口")
    @RequestMapping("/picture/front")
    @PreAuthorize("smb:upload:pictureFront")
    public Result oss() throws UnsupportedEncodingException {
        HashMap<String, String> map = new HashMap<>();
        String format = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String dir = format + "/";
        String host = "https://" + aliOssConfigProperties.bucketName + "." + aliOssConfigProperties.endpoint;
        try {
            //开启oss客户端
            long expireTime = 30;
            long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
            Date expiration = new Date(expireEndTime);


            //生成的到期时间转换位s，并转换为String
            String expire = String.valueOf(expireEndTime / 1000);
            PolicyConditions policyConditions = new PolicyConditions();

            //构造用户表单域Policy条件
            PolicyConditions policyConds = new PolicyConditions();
            //设置上传文件大小的范围
            policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
            //设置上传的路径的前缀:就是上传到指定文件夹
            policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);

            //根据到期时间生成policy策略
            String postPolicy = ossClient.generatePostPolicy(expiration, policyConds);
            //对policy进行UTF-8编码后转base64
            byte[] binaryData = postPolicy.getBytes(StandardCharsets.UTF_8);
            String endPolicy = BinaryUtil.toBase64String(binaryData);
            //生成签名，policy表单域作为填入的值，将该值作为将要签名的字符串。
            String signature = ossClient.calculatePostSignature(postPolicy);

            //封装参数参数返回
            map.put("accessid", aliOssConfigProperties.accessKeyId);
            map.put("host", host);
            map.put("policy", endPolicy);
            map.put("signature", signature);
            map.put("expire", expire);
            map.put("dir", dir);
        } catch (Exception e) {
            log.info("签名生成失败", e);
            throw e;
        }
        return Result.success(map);
    }


    @Operation(summary = "获取后端图片访问地址接口")
    @PostMapping("/picture/backend")
    @PreAuthorize("smb:upload:pictureBackend")
    public Result uploadPicture(@NotNull MultipartFile file) throws Exception {
        long maxSize = 10 * 1024 * 1024; // 10MB
        if (file.getSize() > maxSize) {
            return Result.error(MessageConstants.FILE_SIZE_EXCEEDED);
        }
        // 文件处理逻辑
        String originalFilename = file.getOriginalFilename();
        String suffix = null;
        if (originalFilename != null) {
            suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        if (!(".png".equals(suffix) || ".jpg".equals(suffix))) {
            return Result.error(MessageConstants.PICTURE_FORMAT_ERROR);
        }
        String filename = UUID.randomUUID() + suffix;
        String url = aliOssUtil.uploadFile(filename, file.getInputStream());
        return Result.success(url);
    }
}

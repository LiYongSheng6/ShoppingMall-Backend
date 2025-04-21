package com.shoppingmall.demo.controller;



import com.shoppingmall.demo.utils.AliOssUtil;
import com.shoppingmall.demo.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Tag(name = "图片上传存储接口")
@RestController
@CrossOrigin
@Validated
@RequiredArgsConstructor
public class FileUploadController {
    private final AliOssUtil aliOssUtil;
    @Operation(summary = "上传图片")
    @PostMapping("/upload")
    public Result upload(@NotNull MultipartFile file) throws Exception {
        String originalFilename=file.getOriginalFilename();
        String suffix = null;
        if (originalFilename != null) {
            suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        if(".image".equals(suffix)||".jpeg".equals(suffix)){
            return Result.error("图片格式错误");
        }
        String filename= UUID.randomUUID().toString()+suffix;
        String url = aliOssUtil.uploadFile(filename, file.getInputStream());
        return Result.success(url);
    }
}

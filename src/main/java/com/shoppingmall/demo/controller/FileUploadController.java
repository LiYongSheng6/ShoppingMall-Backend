package com.shoppingmall.demo.controller;


import com.shoppingmall.demo.constant.MessageConstants;
import com.shoppingmall.demo.utils.AliOssUtil;
import com.shoppingmall.demo.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

//@Tag(name = "图片上传存储接口")
@Api(tags = "文件上传存储接口")
@Validated
@RequiredArgsConstructor
//@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/upload")
public class FileUploadController {
    private final AliOssUtil aliOssUtil;

    @Operation(summary = "上传图片获取url")
    @PostMapping("/picture")
    public Result uploadPicture(@NotNull MultipartFile file) throws Exception {
        long maxSize = 10 * 1024 * 1024; // 10MB
        if (file.getSize() > maxSize) {
            return Result.error(MessageConstants.FILE_SIZE_EXCEEDED);
        }
        // 文件处理逻辑
        String originalFilename=file.getOriginalFilename();
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

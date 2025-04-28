package com.shoppingmall.demo.controller;

import com.shoppingmall.demo.model.VO.VerifyCodeVO;
import com.shoppingmall.demo.service.ICheckCodeService;
import com.shoppingmall.demo.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author redmi k50 ultra
 * * @date 2024/7/24
 */
@Slf4j
@Api(tags = "图形验证码请求接口")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/code")
public class CheckCodeController {

    private final ICheckCodeService checkCodeService;

    @Operation(summary = "获取图形验证码")
    @GetMapping("/image")
    //@PreAuthorize("smb:code:image")
    public Result generateVerifyCode() {
        VerifyCodeVO verifyCode = checkCodeService.generateVerifyCode();
        return Result.success(verifyCode);
    }


}


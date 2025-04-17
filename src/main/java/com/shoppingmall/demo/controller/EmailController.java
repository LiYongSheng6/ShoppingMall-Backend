package com.shoppingmall.demo.controller;


import com.shoppingmall.demo.annotation.Log;
import com.shoppingmall.demo.constant.CacheConstants;
import com.shoppingmall.demo.constant.MessageConstants;
import com.shoppingmall.demo.constant.RegexConstants;
import com.shoppingmall.demo.service.IEmailService;
import com.shoppingmall.demo.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @author redmi k50 ultra
 * * @date 2024/7/17
 */
@Api(tags = "邮箱发送接口")
@Validated
@RestController
@RequiredArgsConstructor
@CrossOrigin //支持跨域
@RequestMapping("/email")
public class EmailController {
    private final IEmailService emailService;

    /**
     * 发送注册邮箱验证码
     *
     * @param email-收件人邮箱
     * @return
     * @throws IOException
     */
    @Log
    @Operation(summary = "注册验证")
    @GetMapping("/register")
    public Result registerEmailCheckCode(@RequestParam @Pattern(regexp = RegexConstants.EMAIL_REGEX, message = MessageConstants.EMAIL_REGEX_MESSAGE) String email) throws IOException {
        return emailService.sendCode(email, CacheConstants.REGISTER_EMAIL_CODE_KEY);
    }

    /**
     * 发送登录邮箱验证码
     *
     * @param email-收件人邮箱
     * @return
     * @throws IOException
     */
    @Log
    @Operation(summary = "登录验证")
    @GetMapping("/login")
    public Result loginEmailCheckCode(@RequestParam @Pattern(regexp = RegexConstants.EMAIL_REGEX, message = MessageConstants.EMAIL_REGEX_MESSAGE) String email) throws IOException {
        return emailService.sendCode(email, CacheConstants.LOGIN_EMAIL_CODE_KEY);
    }

    /**
     * 发送重置密码邮箱验证码
     *
     * @return
     * @throws IOException
     */
    @Log
    @Operation(summary = "重置密码验证")
    @GetMapping("/reset")
    public Result resetEmailCheckCode(@RequestParam @Pattern(regexp = RegexConstants.EMAIL_REGEX, message = MessageConstants.EMAIL_REGEX_MESSAGE) String email) throws IOException {
        return emailService.sendCode(email, CacheConstants.RESET_EMAIL_CODE_KEY);
    }

    /**
     * 发送修改邮箱验证码
     *
     * @param email-收件人邮箱
     * @return
     * @throws IOException
     */
    @Log
    @Operation(summary = "修改邮箱验证")
    @GetMapping("/update")
    public Result updateEmailCheckCode(@RequestParam @Pattern(regexp = RegexConstants.EMAIL_REGEX, message = MessageConstants.EMAIL_REGEX_MESSAGE) String email) throws IOException {
        return emailService.sendCode(email, CacheConstants.UPDATE_EMAIL_CODE_KEY);
    }


}

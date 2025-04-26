package com.shoppingmall.demo.controller;

import com.shoppingmall.demo.annotation.Log;
import com.shoppingmall.demo.model.DTO.AuthenticationBatchDTO;
import com.shoppingmall.demo.service.IAuthenticationService;
import com.shoppingmall.demo.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author redmi k50 ultra
 * * @date 2024/10/9
 */

@Api(tags = "实名认证信息管理接口")
@Validated
@RequiredArgsConstructor
//@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/authentic")
public class AuthenticationController {

    private final IAuthenticationService authenticationService;

    @Log
    @Operation(summary = "批量添加修改实名认证信息")
    @PostMapping("/saveOrUpdate/batch")
    public Result saveOrUpdateBatch(@RequestBody @Validated AuthenticationBatchDTO batchDTO) {
        return authenticationService.saveOrUpdateAuthenticationBatch(batchDTO);
    }

    @Log
    @Operation(summary = "批量删除实名认证信息接口")
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody @Validated AuthenticationBatchDTO batchDTO) {
        return authenticationService.deleteAuthenticationBatch(batchDTO);
    }

    @Log
    @Operation(summary = "申请实名信息认证接口")
    @DeleteMapping("/apply")
    public Result applyAuthentication(@RequestParam @NotNull String studentId, @RequestParam @NotNull String realName) {
        return authenticationService.applyAuthentication(studentId, realName);
    }

    @Log
    @Operation(summary = "取消实名信息认证接口")
    @DeleteMapping("/cancel")
    public Result cancelAuthentication(@RequestParam @NotNull String studentId) {
        return authenticationService.cancelAuthentication(studentId);
    }


}

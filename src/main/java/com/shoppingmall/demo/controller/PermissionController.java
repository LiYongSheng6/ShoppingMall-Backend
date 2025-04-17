package com.shoppingmall.demo.controller;

import com.shoppingmall.demo.annotation.Log;
import com.shoppingmall.demo.annotation.PreAuthorize;
import com.shoppingmall.demo.model.DTO.PermissionSaveDTO;
import com.shoppingmall.demo.model.DTO.PermissionUpdateDTO;
import com.shoppingmall.demo.service.IPermissionService;
import com.shoppingmall.demo.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author redmi k50 ultra
 * * @date 2024/10/9
 */
@Api(tags = "权限资源管理接口")
@Validated
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/permission")
public class PermissionController {

    private final IPermissionService permissionService;

    @Log
    @Operation(summary = "添加权限资源接口")
    @PostMapping("/addPermission")
    @PreAuthorize("sys:permission:addPermission")
    public Result addPermission(@RequestBody @Validated PermissionSaveDTO PermissionSaveDTO) {
        return permissionService.addPermission(PermissionSaveDTO);
    }

    @Log
    @Operation(summary = "修改权限资源接口")
    @PutMapping("/updatePermission")
    @PreAuthorize("sys:permission:updatePermission")
    public Result updatePermission(@RequestBody @Validated PermissionUpdateDTO permissionUpdateDTO) {
        return permissionService.updatePermission(permissionUpdateDTO);
    }

    @Log
    @Operation(summary = "获取权限资源接口")
    @GetMapping("/getPermission")
    @PreAuthorize("sys:permission:getPermission")
    public Result getPermission(@RequestParam @NotNull Integer type) {
        return permissionService.getPermissionList(type);
    }

    @Log
    @Operation(summary = "删除权限资源接口")
    @DeleteMapping("/deletePermission")
    @PreAuthorize("sys:permission:deletePermission")
    public Result deletePermission(@RequestParam @NotNull Long id) {
        return permissionService.deletePermission(id);
    }


}

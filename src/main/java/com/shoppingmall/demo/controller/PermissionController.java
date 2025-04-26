package com.shoppingmall.demo.controller;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.shoppingmall.demo.annotation.Log;
import com.shoppingmall.demo.annotation.PreAuthorize;
import com.shoppingmall.demo.config.deserializer.StringToLongDeserializer;
import com.shoppingmall.demo.model.DTO.PermissionDeleteBatchDTO;
import com.shoppingmall.demo.model.DTO.PermissionSaveDTO;
import com.shoppingmall.demo.model.DTO.PermissionUpdateDTO;
import com.shoppingmall.demo.service.IPermissionService;
import com.shoppingmall.demo.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
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
@Api(tags = "权限信息管理接口")
@Validated
@RestController
@RequiredArgsConstructor
//@CrossOrigin(origins = "*")
@RequestMapping("/permission")
public class PermissionController {

    private final IPermissionService permissionService;

    @Log
    @Operation(summary = "添加权限信息接口")
    @PostMapping("/save")
    @PreAuthorize("sys:permission:save")
    public Result save(@RequestBody @Validated PermissionSaveDTO permissionSaveDTO) {
        return permissionService.savePermission(permissionSaveDTO);
    }

    @Log
    @Operation(summary = "修改权限信息接口")
    @PutMapping("/update")
    @PreAuthorize("sys:permission:update")
    public Result update(@RequestBody @Validated PermissionUpdateDTO permissionUpdateDTO) {
        return permissionService.updatePermission(permissionUpdateDTO);
    }

    @Log
    @Operation(summary = "删除权限信息接口")
    @DeleteMapping("/delete")
    @PreAuthorize("sys:permission:delete")
    public Result delete(@RequestParam @NotNull @JsonDeserialize(using = StringToLongDeserializer.class) Long id) {
        return permissionService.deletePermission(id);
    }

    @Log
    @Operation(summary = "批量删除权限信息接口")
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody @Validated PermissionDeleteBatchDTO deleteBatchDTO) {
        return permissionService.deletePermissionBatch(deleteBatchDTO);
    }

    @Log
    @Operation(summary = "获取单一权限信息详情接口")
    @GetMapping("/detail")
    public Result getPermissionById(@RequestParam @NotNull @JsonDeserialize(using = StringToLongDeserializer.class) Long id) {
        return permissionService.getPermissionById(id);
    }

    @Log
    @Operation(summary = "获取用户权限信息列表接口")
    @GetMapping("/list/own")
    public Result getPermissionListByUserId(@RequestParam @NotNull @JsonDeserialize(using = StringToLongDeserializer.class) Long userId) {
        return permissionService.getPermissionListByUserId(userId);
    }

    @Log
    @Operation(summary = "获取用户权限编码资源列表接口")
    @GetMapping("/list/code/own")
    public Result getPermissionCodeListByUserId(@RequestParam @NotNull @JsonDeserialize(using = StringToLongDeserializer.class) Long userId) {
        return permissionService.getPermissionCodeListByUserId(userId);
    }

    @Log
    @Operation(summary = "获取当前角色权限ID列表接口")
    @GetMapping("/list/id/own")
    @PreAuthorize("sys:permission:list")
    public Result getPermissionIdList(@RequestParam @NotNull @JsonDeserialize(using = StringToLongDeserializer.class) Long roleId,
                                    @RequestParam Integer type) {
        return permissionService.getPermissionIdListByUserId(roleId, type);
    }

    @Log
    @Operation(summary = "获取当前角色标记有的权限列表接口")
    @GetMapping("/list/all/own")
    @PreAuthorize("sys:role:list")
    public Result getHaveSignPermissionList(@RequestParam @NotNull @JsonDeserialize(using = StringToLongDeserializer.class) Long userId,
                                            @RequestParam Integer type) {
        return permissionService.getHaveSignPermissionList(userId, type);
    }

    @Log
    @Operation(summary = "获取所有权限信息列表接口")
    @GetMapping("/list/all")
    @PreAuthorize("sys:permission:list")
    public Result getPermissionList(@RequestParam Integer type) {
        return permissionService.getAllPermissionList(type);
    }

    @Log
    @Operation(summary = "获取树形权限资源信息")
    @GetMapping("/tree")
    public Result getRolePermissionTree() {
        return permissionService.getPermissionTree();
    }

}

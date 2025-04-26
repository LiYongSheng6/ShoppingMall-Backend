package com.shoppingmall.demo.controller;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.shoppingmall.demo.annotation.Log;
import com.shoppingmall.demo.annotation.PreAuthorize;
import com.shoppingmall.demo.config.deserializer.StringToLongDeserializer;
import com.shoppingmall.demo.model.DTO.RoleDeleteBatchDTO;
import com.shoppingmall.demo.model.DTO.RolePermissionDTO;
import com.shoppingmall.demo.model.DTO.RoleSaveDTO;
import com.shoppingmall.demo.model.DTO.RoleUpdateDTO;
import com.shoppingmall.demo.model.DTO.UserRoleDTO;
import com.shoppingmall.demo.service.IRoleService;
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
 * * @date 2024/7/20
 */
@Api(tags = "角色管理操作接口")
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/role")
public class RoleController {

    private final IRoleService roleService;

    @Log
    @Operation(summary = "添加角色信息接口")
    @PostMapping("/save")
    @PreAuthorize("sys:role:save")
    public Result save(@RequestBody @Validated RoleSaveDTO saveDTO) {
        return roleService.saveRole(saveDTO);
    }

    @Log
    @Operation(summary = "修改角色信息接口")
    @PutMapping("/update")
    @PreAuthorize("sys:role:update")
    public Result update(@RequestBody @Validated RoleUpdateDTO updateDTO) {
        return roleService.updateRole(updateDTO);
    }

    @Log
    @Operation(summary = "删除角色信息接口")
    @DeleteMapping("/delete")
    @PreAuthorize("sys:role:delete")
    public Result delete(@RequestParam @NotNull @JsonDeserialize(using = StringToLongDeserializer.class) Long id) {
        return roleService.deleteRole(id);
    }

    @Log
    @Operation(summary = "批量删除角色信息接口")
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody @Validated RoleDeleteBatchDTO deleteBatchDTO) {
        return roleService.deleteRoleBatch(deleteBatchDTO);
    }

    @Log
    @Operation(summary = "获取单一角色信息详情接口")
    @GetMapping("/detail")
    public Result getDelivery(@RequestParam @NotNull @JsonDeserialize(using = StringToLongDeserializer.class) Long id) {
        return roleService.getRoleById(id);
    }

    @Log
    @Operation(summary = "获取当前用户角色ID列表接口")
    @GetMapping("/list/id/own")
    @PreAuthorize("sys:role:list")
    public Result getRoleIdListByUserId(@RequestParam @NotNull @JsonDeserialize(using = StringToLongDeserializer.class) Long userId) {
        return roleService.getRoleIdListByUserId(userId);
    }

    @Log
    @Operation(summary = "获取当前用户标记有的角色列表接口")
    @GetMapping("/list/own")
    @PreAuthorize("sys:role:list")
    public Result getHaveSignRoleList(@RequestParam @NotNull @JsonDeserialize(using = StringToLongDeserializer.class) Long userId) {
        return roleService.getHaveSignRoleList(userId);
    }

    @Log
    @Operation(summary = "获取所有角色信息列表接口")
    @GetMapping("/list")
    @PreAuthorize("sys:role:list")
    public Result getAllRoleList() {
        return roleService.getAllRoleList();
    }

    @Log
    @Operation(summary = "获取树形角色权限信息")
    @GetMapping("/tree")
    public Result getRoleTree() {
        return roleService.getRoleTree();
    }

    @Log
    @Operation(summary = "分配用户角色接口")
    @PostMapping("/assign/roles")
    public Result assignRoles(@RequestBody @Validated UserRoleDTO userRoleDTO) {
        return roleService.assignRoleToUser(userRoleDTO);
    }

    @Log
    @Operation(summary = "分配角色权限接口")
    @PostMapping("/assign/perms")
    public Result assignPerms(@RequestBody @Validated RolePermissionDTO rolePermissionDTO) {
        return roleService.assignPermissionToRole(rolePermissionDTO);
    }


}

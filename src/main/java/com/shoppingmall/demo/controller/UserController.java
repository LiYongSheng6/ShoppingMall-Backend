package com.shoppingmall.demo.controller;

import com.shoppingmall.demo.annotation.Log;
import com.shoppingmall.demo.annotation.PreAuthorize;
import com.shoppingmall.demo.model.DTO.UserInfoDTO;
import com.shoppingmall.demo.model.DTO.UserLoginDTO;
import com.shoppingmall.demo.model.DTO.UserResetDTO;
import com.shoppingmall.demo.model.DTO.UserRgsDTO;
import com.shoppingmall.demo.model.DTO.UserUpdateDTO;
import com.shoppingmall.demo.model.Query.UserQuery;
import com.shoppingmall.demo.service.IUserService;
import com.shoppingmall.demo.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
@Api(tags = "用户基本操作接口")
@Validated
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/user")
public class UserController {

    private final IUserService userService;

    @Log
    @Operation(summary = "访问测试接口")
    @GetMapping("/hello")
    public Result hello() {
        return userService.hello();
    }

    @Log
    @Operation(summary = "获取前端路由接口")
    @GetMapping("/path/front")
    public Result getFrontPath() {
        return userService.getFrontPermission();
    }

    @Log
    @Operation(summary = "用户更新token接口")
    @GetMapping("/update/token")
    public Result updateToken() {
        return userService.updateToken();
    }

    @Log
    @Operation(summary = "用户注册接口")
    @PostMapping("/register")
    public Result register(@RequestBody @Validated UserRgsDTO userRgsDTO) {
        return userService.register(userRgsDTO);
    }

    @Log
    @Operation(summary = "用户登录接口")
    @PostMapping("/login")
    public Result login(@RequestBody @Validated UserLoginDTO userLoginDTO) {
        return userService.login(userLoginDTO);
    }

    @Log
    @Operation(summary = "获取当前登录用户信息")
    @GetMapping("/detail/loginUser")
    public Result getLoginUserInfo() {
        return userService.getLoginUserInfo();
    }

    @Log
    @Operation(summary = "根据ID获取用户信息")
    @ApiImplicitParam(name = "id", value = "用户id", required = true)
    @GetMapping("/detail/userInfo")
    @PreAuthorize("sys:user:getUserInfoById")
    public Result getUserInfoById(@RequestParam @NotNull Long id) {
        return userService.getUserInfoById(id);
    }

    @Log
    @Operation(summary = "分页查询用户信息列表")
    @PostMapping("/list/page")
    @PreAuthorize("sys:user:listByPage")
    public Result listByCondition(@RequestBody @Validated UserQuery userQuery) {
        return userService.pageUserListByCondition(userQuery);
    }

    @Log
    @Operation(summary = "修改用户密码")
    @PatchMapping("/update/password")
    public Result updateUserPassword(@RequestBody @Validated UserResetDTO userResetDTO) {
        return userService.updateUserPassword(userResetDTO);
    }

    @Log
    @Operation(summary = "修改用户邮箱")
    @PatchMapping("/update/email")
    public Result updateUserEmail(@RequestBody @Validated UserResetDTO userResetDTO) {
        return userService.updateUserEmail(userResetDTO);
    }

    @Log
    @Operation(summary = "修改当前登录用户信息")
    @PutMapping("/update/loginUser")
    public Result updateLoginUserInfo(@RequestBody @Validated UserUpdateDTO userUpdateDTO) {
        return userService.updateLoginUserInfo(userUpdateDTO);
    }

    @Log
    @Operation(summary = "根据ID修改用户信息")
    @PutMapping("/update/userInfo")
    @PreAuthorize("sys:user:updateUserInfoById")
    public Result updateUserInfoById(@RequestBody @Validated UserInfoDTO userInfoDTO) {
        return userService.updateUserInfoById(userInfoDTO);
    }

    @Log
    @Operation(summary = "根据ID封禁用户账号")
    @PatchMapping("/forbidden")
    @PreAuthorize("sys:user:forbidden")
    public Result forbiddenUserById(@RequestParam @NotNull Long id) {
        return userService.forbiddenUserById(id);
    }

    @Log
    @Operation(summary = "根据ID解封用户账号")
    @PatchMapping("/unblocking")
    @PreAuthorize("sys:user:unblocking")
    public Result unblockingUserById(@RequestParam @NotNull Long id) {
        return userService.unblockingUserById(id);
    }

    @Log
    @Operation(summary = "根据ID删除用户信息")
    @DeleteMapping("/delete")
    @PreAuthorize("sys:user:delete")
    public Result deleteUserById(@RequestParam @NotNull Long id) {
        return userService.deleteUserById(id);
    }


}

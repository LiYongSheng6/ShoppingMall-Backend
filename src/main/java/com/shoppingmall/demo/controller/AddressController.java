package com.shoppingmall.demo.controller;

import com.shoppingmall.demo.annotation.Log;
import com.shoppingmall.demo.annotation.PreAuthorize;
import com.shoppingmall.demo.model.DTO.AddressSaveDTO;
import com.shoppingmall.demo.model.DTO.AddressUpdateDTO;
import com.shoppingmall.demo.service.IAddressService;
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

@Api(tags = "地名信息管理接口")
@Validated
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/address")
public class AddressController {

    private final IAddressService AddressService;

    @Log
    @Operation(summary = "添加地名信息接口")
    @PostMapping("/save")
    public Result save(@RequestBody @Validated AddressSaveDTO addressSaveDTO) {
        return AddressService.saveAddress(addressSaveDTO);
    }

    @Log
    @Operation(summary = "修改地名信息接口")
    @PutMapping("/update")
    public Result update(@RequestBody @Validated AddressUpdateDTO addressUpdateDTO) {
        return AddressService.updateAddress(addressUpdateDTO);
    }

    @Log
    @Operation(summary = "删除地名信息接口")
    @DeleteMapping("/delete")
    public Result delete(@RequestParam @NotNull Long id) {
        return AddressService.deleteAddressById(id);
    }

    @Log
    @Operation(summary = "获取收货地名信息接口")
    @GetMapping("/list/parentId")
    public Result getAddressListByParentId(@RequestParam @NotNull Long parentId,
                                           @RequestParam @NotNull Integer type) {
        return AddressService.getAddressListByIdAndType(parentId, type);
    }

    @Log
    @Operation(summary = "根据类型获取所属地名信息接口")
    @GetMapping("/list/type")
    @PreAuthorize("sys:address:getAddressListByIdAndType")
    public Result getAddressListByIdAndType(Long parentId, Integer type) {
        return AddressService.getAddressListByIdAndType(parentId, type);
    }


}

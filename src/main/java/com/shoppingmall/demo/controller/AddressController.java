package com.shoppingmall.demo.controller;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.shoppingmall.demo.annotation.Log;
import com.shoppingmall.demo.annotation.PreAuthorize;
import com.shoppingmall.demo.config.deserializer.StringToLongDeserializer;
import com.shoppingmall.demo.model.DTO.AddressDeleteBatchDTO;
import com.shoppingmall.demo.model.DTO.AddressSaveBatchDTO;
import com.shoppingmall.demo.model.DTO.AddressSaveDTO;
import com.shoppingmall.demo.model.DTO.AddressUpdateDTO;
import com.shoppingmall.demo.model.Query.AddressQuery;
import com.shoppingmall.demo.service.IAddressService;
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

@Api(tags = "地名信息管理接口")
@Validated
@RequiredArgsConstructor
//@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/address")
public class AddressController {

    private final IAddressService addressService;

    @Log
    @Operation(summary = "添加地名信息接口")
    @PostMapping("/save")
    public Result save(@RequestBody @Validated AddressSaveDTO addressSaveDTO) {
        return addressService.saveAddress(addressSaveDTO);
    }

    @Log
    @Operation(summary = "修改地名信息接口")
    @PutMapping("/update")
    public Result update(@RequestBody @Validated AddressUpdateDTO addressUpdateDTO) {
        return addressService.updateAddress(addressUpdateDTO);
    }

    @Log
    @Operation(summary = "批量添加修改地名信息")
    @PostMapping("/saveOrUpdate/batch")
    public Result saveOrUpdateBatch(@RequestBody @Validated AddressSaveBatchDTO addressSaveBatchDTO) {
        return addressService.saveOrUpdateAddressBatch(addressSaveBatchDTO);
    }

    @Log
    @Operation(summary = "获取收货地名信息接口")
    @GetMapping("/list/parentId")
    public Result getAddressListByParentId(@JsonDeserialize(using = StringToLongDeserializer.class) Long parentId,
                                           @RequestParam @NotNull Integer type) {
        return addressService.getAddressListByIdAndType(parentId, type);
    }

    @Log
    @Operation(summary = "根据类型获取所属地名信息接口")
    @GetMapping("/list/type")
    @PreAuthorize("sys:address:getAddressListByIdAndType")
    public Result getAddressListByIdAndType(@JsonDeserialize(using = StringToLongDeserializer.class) Long parentId, Integer type) {
        return addressService.getAddressListByIdAndType(parentId, type);
    }

    @Log
    @Operation(summary = "获取树形地名信息接口")
    @GetMapping("/tree")
    public Result getAddressTreeInfo() {
        return addressService.getAddressTreeInfo();
    }

    @Log
    @Operation(summary = "分页查询地名信息列表")
    @PostMapping("/list/page")
    public Result listByCondition(@RequestBody @Validated AddressQuery query) {
        return addressService.pageAddressListByCondition(query);
    }

    @Log
    @Operation(summary = "删除地名信息接口")
    @DeleteMapping("/delete")
    public Result delete(@RequestParam @NotNull @JsonDeserialize(using = StringToLongDeserializer.class) Long id) {
        return addressService.deleteAddressById(id);
    }

    @Log
    @Operation(summary = "批量删除地名信息接口")
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody @Validated AddressDeleteBatchDTO addressDeleteBatchDTO) {
        return addressService.deleteAddressBatch(addressDeleteBatchDTO);
    }


}

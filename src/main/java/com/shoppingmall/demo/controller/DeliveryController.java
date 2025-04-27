package com.shoppingmall.demo.controller;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.shoppingmall.demo.annotation.Log;
import com.shoppingmall.demo.annotation.PreAuthorize;
import com.shoppingmall.demo.config.deserializer.StringToLongDeserializer;
import com.shoppingmall.demo.model.DTO.DeliveryDeleteBatchDTO;
import com.shoppingmall.demo.model.DTO.DeliverySaveDTO;
import com.shoppingmall.demo.model.DTO.DeliverySaveIdDTO;
import com.shoppingmall.demo.model.DTO.DeliveryUpdateDTO;
import com.shoppingmall.demo.model.DTO.DeliveryUpdateIdDTO;
import com.shoppingmall.demo.model.Query.DeliveryQuery;
import com.shoppingmall.demo.service.IDeliveryService;
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

@Api(tags = "收货信息管理接口")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/delivery")
public class DeliveryController {

    private final IDeliveryService deliveryService;

    @Log
    @Operation(summary = "添加收货信息接口")
    @PostMapping("/save")
    public Result save(@RequestBody @Validated DeliverySaveDTO deliverySaveDTO) {
        return deliveryService.saveDelivery(deliverySaveDTO);
    }
    @Log
    @Operation(summary = "修改收货信息接口")
    @PutMapping("/update")
    public Result update(@RequestBody @Validated DeliveryUpdateDTO deliveryUpdateDTO) {
        return deliveryService.updateDelivery(deliveryUpdateDTO);
    }

    @Log
    @Operation(summary = "添加收货信息Id接口")
    @PostMapping("/save/id")
    public Result save(@RequestBody @Validated DeliverySaveIdDTO deliverySaveDTO) {
        return deliveryService.saveIdDelivery(deliverySaveDTO);
    }

    @Log
    @Operation(summary = "修改收货信息Id接口")
    @PutMapping("/update/id")
    public Result update(@RequestBody @Validated DeliveryUpdateIdDTO deliveryUpdateDTO) {
        return deliveryService.updateIdDelivery(deliveryUpdateDTO);
    }

    @Log
    @Operation(summary = "删除收货信息接口")
    @DeleteMapping("/delete")
    public Result delete(@RequestParam @NotNull @JsonDeserialize(using = StringToLongDeserializer.class) Long id) {
        return deliveryService.deleteDeliveryById(id);
    }

    @Log
    @Operation(summary = "获取单一收货信息详情接口")
    @GetMapping("/detail")
    public Result getDelivery(@RequestParam @NotNull @JsonDeserialize(using = StringToLongDeserializer.class) Long id) {
        return deliveryService.getDeliveryById(id);
    }

    @Log
    @Operation(summary = "获取当前用户收货信息列表接口")
    @GetMapping("/list")
    public Result getDeliveryList() {
        return deliveryService.getDeliveryList();
    }

    @Log
    @Operation(summary = "获取用户收货信息列表接口")
    @GetMapping("/list/admin")
    @PreAuthorize("smb:delivery:listByAdmin")
    public Result listByAdmin(@RequestParam @NotNull @JsonDeserialize(using = StringToLongDeserializer.class) Long userId) {
        return deliveryService.getDeliveryListByUserId(userId);
    }

    @Log
    @Operation(summary = "分页查询当前用户收货信息列表")
    @PostMapping("/list/page")
    public Result listByLoginUser(@RequestBody @Validated DeliveryQuery query) {
        return deliveryService.pageDeliveryList(query);
    }

    @Log
    @Operation(summary = "分页查询收货信息列表")
    @PostMapping("/list/page/admin")
    @PreAuthorize("smb:delivery:listPageByAdmin")
    public Result listPageByAdmin(@RequestBody @Validated DeliveryQuery query) {
        return deliveryService.pageDeliveryListByCondition(query);
    }

    @Log
    @Operation(summary = "批量删除收货信息接口")
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody @Validated DeliveryDeleteBatchDTO deliveryDeleteBatchDTO) {
        return deliveryService.deleteDeliveryBatch(deliveryDeleteBatchDTO);
    }

}

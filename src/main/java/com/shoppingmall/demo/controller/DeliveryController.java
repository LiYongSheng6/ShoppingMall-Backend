package com.shoppingmall.demo.controller;

import com.shoppingmall.demo.annotation.Log;

import com.shoppingmall.demo.annotation.PreAuthorize;
import com.shoppingmall.demo.model.DTO.DeliverySaveDTO;
import com.shoppingmall.demo.model.DTO.DeliveryUpdateDTO;
import com.shoppingmall.demo.service.IDeliveryService;
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

@Api(tags = "收货地址接口")
@Validated
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/delivery")
public class DeliveryController {

    private final IDeliveryService deliveryService;

    @Log
    @Operation(summary = "添加收货地址接口")
    @PostMapping("/saveDelivery")
    public Result saveDelivery(@RequestBody @Validated DeliverySaveDTO deliverySaveDTO) {
        return deliveryService.saveDelivery(deliverySaveDTO);
    }

    @Log
    @Operation(summary = "修改收货地址接口")
    @PutMapping("/updateDelivery")
    public Result updateDelivery(@RequestBody @Validated DeliveryUpdateDTO deliveryUpdateDTO) {
        return deliveryService.updateDelivery(deliveryUpdateDTO);
    }

    @Log
    @Operation(summary = "根据id获取收货地址信息接口")
    @GetMapping("/getDelivery")
    public Result getDelivery(@RequestParam @NotNull Long id) {
        return deliveryService.getDeliveryById(id);
    }

    @Log
    @Operation(summary = "获取当前用户收货地址列表接口")
    @GetMapping("/getDeliveryList")
    public Result getDeliveryList() {
        return deliveryService.getDeliveryList();
    }

    @Log
    @Operation(summary = "根据用户id获取其收货地址列表接口")
    @GetMapping("/getDeliveryListByUserId")
    @PreAuthorize("sys:Delivery:getDeliveryListByUserId")
    public Result getDeliveryListByUserId(@RequestParam @NotNull Long userId) {
        return deliveryService.getDeliveryListByUserId(userId);
    }

    @Log
    @Operation(summary = "删除收货地址接口")
    @DeleteMapping("/deleteDelivery")
    public Result deleteDelivery(@RequestParam @NotNull Long id) {
        return deliveryService.deleteDeliveryById(id);
    }


}

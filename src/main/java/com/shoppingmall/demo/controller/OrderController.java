package com.shoppingmall.demo.controller;
import com.shoppingmall.demo.annotation.Log;
import com.shoppingmall.demo.model.DTO.OrderSaveDTO;
import com.shoppingmall.demo.model.DTO.OrderUpdateDTO;
import com.shoppingmall.demo.model.Query.OrderQuery;
import com.shoppingmall.demo.model.VO.OrderVO;
import com.shoppingmall.demo.service.IOrderService;
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
@Api(tags = "订单信息管理接口")
@Validated
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/order")
public class OrderController {

    private final IOrderService orderService;

    @Log
    @Operation(summary = "添加订单信息接口")
    @PostMapping("/save")
    public Result save(@RequestBody @Validated OrderSaveDTO orderSaveDTO) {
        return orderService.saveOrder(orderSaveDTO);

    }

    @Log
    @Operation(summary = "修改订单状态为待发货")
    @PutMapping("/update/shipping")
    public Result updateToBeShipping(@RequestBody @Validated OrderUpdateDTO orderUpdateDTO) {
        return orderService.updateToBeShipping(orderUpdateDTO);
    }

    @Log
    @Operation(summary = "修改订单状态为已完成")
    @PutMapping("/update/completed")
    public Result updateToBeCompleted(@RequestBody @Validated OrderUpdateDTO orderUpdateDTO) {
        return orderService.updateToBeCompleted(orderUpdateDTO);
    }

    @Log
    @Operation(summary = "修改订单状态为已取消")
    @PutMapping("/update/canceled")
    public Result updateToBeCanceled(@RequestBody @Validated OrderUpdateDTO orderUpdateDTO) {
        return orderService.updateToBeCancelled(orderUpdateDTO);
    }

    @Log
    @Operation(summary = "删除订单信息接口")
    @DeleteMapping("/delete")
    public Result delete(@RequestParam @NotNull Long orderId) {
        return orderService.deleteOrderById(orderId);
    }

    @Log
    @Operation(summary = "查询单个订单信息")
    @GetMapping("/detail")
    public Result<OrderVO> detail(@RequestParam @NotNull Long orderId) {
        return orderService.selectOrderById(orderId);
    }

    @Log
    @Operation(summary = "查询订单信息列表")
    @GetMapping("/list/page")
    public Result listByPlaylistId(@Validated OrderQuery orderQuery) {
        return orderService.pageOrderListByUserId(orderQuery);
    }

}

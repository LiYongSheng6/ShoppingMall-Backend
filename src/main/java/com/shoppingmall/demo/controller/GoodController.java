package com.shoppingmall.demo.controller;

import com.shoppingmall.demo.annotation.Log;
import com.shoppingmall.demo.model.DTO.GoodDeleteBatchDTO;
import com.shoppingmall.demo.model.DTO.GoodSaveDTO;
import com.shoppingmall.demo.model.DTO.GoodUpdateDTO;
import com.shoppingmall.demo.model.DTO.GoodUpdateStockNumDTO;
import com.shoppingmall.demo.model.Query.GoodQuery;
import com.shoppingmall.demo.service.IGoodService;
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
@Api(tags = "商品信息管理接口")
@Validated
@RestController
@RequiredArgsConstructor
//@CrossOrigin(origins = "*")
@RequestMapping("/good")
public class GoodController {

    private final IGoodService goodService;

    @Log
    @Operation(summary = "添加商品信息接口")
    @PostMapping("/save")
    public Result save(@RequestBody @Validated GoodSaveDTO saveDTO) {
        return goodService.saveGood(saveDTO);
    }

    @Log
    @Operation(summary = "修改商品信息接口")
    @PutMapping("/update")
    public Result update(@RequestBody @Validated GoodUpdateDTO updateDTO) {
        return goodService.updateGood(updateDTO);
    }

    @Log
    @Operation(summary = "修改商品库存接口")
    @PutMapping("/update/stock")
    public Result updateStockNum(@RequestBody @Validated GoodUpdateStockNumDTO updateDTO) {
        return goodService.updateGoodStockNum(updateDTO);
    }

    @Log
    @Operation(summary = "删除商品信息接口")
    @DeleteMapping("/delete")
    public Result delete(@RequestParam @NotNull Long GoodId) {
        return goodService.deleteGoodById(GoodId);
    }

    @Log
    @Operation(summary = "批量删除商品信息接口")
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody @Validated GoodDeleteBatchDTO deleteBatchDTO) {
        return goodService.deleteGoodBatch(deleteBatchDTO);
    }

    @Log
    @Operation(summary = "获取单个商品信息接口")
    @GetMapping("/detail")
    public Result getGoodById(@RequestParam @NotNull Long id) {
        return goodService.getGoodInfoById(id);
    }

    @Log
    @Operation(summary = "获取当前用户商品信息分页列表")
    @GetMapping("/list/own")
    public Result getDeliveryList(@RequestBody @Validated GoodQuery goodQuery) {
        return goodService.getMyGoodListPage(goodQuery);
    }

    @Log
    @Operation(summary = "分页查询商品信息列表")
    @PostMapping("/list/page")
    public Result listByCondition(@RequestBody @Validated GoodQuery goodQuery) {
        return goodService.pageGoodListByCondition(goodQuery);
    }

    @Log
    @Operation(summary = "获取商品榜单列表接口")
    //@PostMapping("/list/rank")
    public Result getRankList(@RequestBody @Validated GoodQuery goodQuery) {
        return goodService.getGoodRankList(goodQuery);
    }

}

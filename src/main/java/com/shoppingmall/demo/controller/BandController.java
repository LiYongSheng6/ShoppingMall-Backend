package com.shoppingmall.demo.controller;

import com.shoppingmall.demo.annotation.Log;
import com.shoppingmall.demo.annotation.PreAuthorize;
import com.shoppingmall.demo.model.DTO.BandSaveDTO;
import com.shoppingmall.demo.model.DTO.BandUpdateDTO;
import com.shoppingmall.demo.service.IBandService;
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

@Api(tags = "品牌信息接口")
@Validated
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/band")
public class BandController {

    private final IBandService bandService;

    @Log
    @Operation(summary = "添加品牌信息接口")
    @PostMapping("/saveBand")
    @PreAuthorize("sys:Band:saveBand")
    public Result saveBand(@RequestBody @Validated BandSaveDTO bandSaveDTO) {
        return bandService.saveBand(bandSaveDTO);
    }

    @Log
    @Operation(summary = "修改品牌信息接口")
    @PutMapping("/updateBand")
    @PreAuthorize("sys:Band:updateBand")
    public Result updateBand(@RequestBody @Validated BandUpdateDTO bandUpdateDTO) {
        return bandService.updateBand(bandUpdateDTO);
    }

    @Log
    @Operation(summary = "获取品牌信息接口")
    @GetMapping("/getBandList")
    public Result getBandList(@RequestParam @NotNull Integer type) {
        return bandService.getBandListByType(type);
    }

    @Log
    @Operation(summary = "删除品牌信息接口")
    @DeleteMapping("/deleteBand")
    @PreAuthorize("sys:Band:deleteBand")
    public Result deleteBand(@RequestParam @NotNull Long id) {
        return bandService.deleteBandById(id);
    }

}



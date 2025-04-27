package com.shoppingmall.demo.controller;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.shoppingmall.demo.annotation.Log;
import com.shoppingmall.demo.annotation.PreAuthorize;
import com.shoppingmall.demo.config.deserializer.StringToLongDeserializer;
import com.shoppingmall.demo.model.DTO.CategoryDeleteBatchDTO;
import com.shoppingmall.demo.model.DTO.CategorySaveBatchDTO;
import com.shoppingmall.demo.model.DTO.CategorySaveDTO;
import com.shoppingmall.demo.model.DTO.CategoryUpdateDTO;
import com.shoppingmall.demo.model.Query.CategoryQuery;
import com.shoppingmall.demo.service.ICategoryService;
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

@Api(tags = "分类信息管理接口")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/category")
public class CategoryController {

    private final ICategoryService categoryService;

    @Log
    @Operation(summary = "添加分类信息接口")
    @PostMapping("/save")
    @PreAuthorize("smb:category:save")
    public Result save(@RequestBody @Validated CategorySaveDTO categorySaveDTO) {
        return categoryService.saveCategory(categorySaveDTO);
    }

    @Log
    @Operation(summary = "修改分类信息接口")
    @PutMapping("/update")
    @PreAuthorize("smb:category:update")
    public Result update(@RequestBody @Validated CategoryUpdateDTO categoryUpdateDTO) {
        return categoryService.updateCategory(categoryUpdateDTO);
    }

    @Log
    @Operation(summary = "批量添加修改分类信息")
    @PostMapping("/saveOrUpdate/batch")
    @PreAuthorize("smb:category:saveOrUpdateBatch")
    public Result saveOrUpdateBatch(@RequestBody @Validated CategorySaveBatchDTO categorySaveBatchDTO) {
        return categoryService.saveOrUpdateCategoryBatch(categorySaveBatchDTO);
    }

    @Log
    @Operation(summary = "获取分类名称列表接口")
    @GetMapping("/list")
    public Result getCategoryList(@RequestParam @NotNull Integer type) {
        return categoryService.getCategoryListByType(type);
    }

    @Log
    @Operation(summary = "获取树形分类信息接口")
    @GetMapping("/tree")
    public Result getCategoryTreeInfo() {
        return categoryService.getCategoryTreeInfo();
    }

    @Log
    @Operation(summary = "分页查询分类信息列表")
    @PostMapping("/list/page")
    public Result listByCondition(@RequestBody @Validated CategoryQuery query) {
        return categoryService.pageCategoryListByCondition(query);
    }

    @Log
    @Operation(summary = "删除分类信息接口")
    @DeleteMapping("/delete")
    @PreAuthorize("smb:category:delete")
    public Result delete(@RequestParam @NotNull @JsonDeserialize(using = StringToLongDeserializer.class) Long id) {
        return categoryService.deleteCategoryById(id);
    }

    @Log
    @Operation(summary = "批量删除分类信息接口")
    @DeleteMapping("/delete/batch")
    @PreAuthorize("smb:category:deleteBatch")
    public Result deleteBatch(@RequestBody @Validated CategoryDeleteBatchDTO categoryDeleteBatchDTO) {
        return categoryService.deleteCategoryBatch(categoryDeleteBatchDTO);
    }

}




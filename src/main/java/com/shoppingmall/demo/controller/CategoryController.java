package com.shoppingmall.demo.controller;

import com.shoppingmall.demo.annotation.Log;
import com.shoppingmall.demo.annotation.PreAuthorize;
import com.shoppingmall.demo.model.DTO.CategorySaveDTO;
import com.shoppingmall.demo.model.DTO.CategoryUpdateDTO;
import com.shoppingmall.demo.service.ICategoryService;
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

@Api(tags = "分类信息接口")
@Validated
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/category")
public class CategoryController {

    private final ICategoryService categoryService;

    @Log
    @Operation(summary = "添加分类信息接口")
    @PostMapping("/saveCategory")
    @PreAuthorize("sys:Category:saveCategory")
    public Result saveCategory(@RequestBody @Validated CategorySaveDTO categorySaveDTO) {
        return categoryService.saveCategory(categorySaveDTO);
    }

    @Log
    @Operation(summary = "修改分类信息接口")
    @PutMapping("/updateCategory")
    @PreAuthorize("sys:Category:updateCategory")
    public Result updateCategory(@RequestBody @Validated CategoryUpdateDTO categoryUpdateDTO) {
        return categoryService.updateCategory(categoryUpdateDTO);
    }

    @Log
    @Operation(summary = "获取分类信息接口")
    @GetMapping("/getCategoryList")
    public Result getCategoryList(@RequestParam @NotNull Integer type) {
        return categoryService.getCategoryListByType(type);
    }

    @Log
    @Operation(summary = "删除分类信息接口")
    @DeleteMapping("/deleteCategory")
    @PreAuthorize("sys:Category:deleteCategory")
    public Result deleteCategory(@RequestParam @NotNull Long id) {
        return categoryService.deleteCategoryById(id);
    }

}




package com.shoppingmall.demo.controller;

import com.shoppingmall.demo.annotation.Log;
import com.shoppingmall.demo.annotation.PreAuthorize;
import com.shoppingmall.demo.model.DTO.TagSaveDTO;
import com.shoppingmall.demo.model.DTO.TagUpdateDTO;
import com.shoppingmall.demo.service.ITagService;
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

@Api(tags = "标签信息管理接口")
@Validated
@RequiredArgsConstructor
//@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/tag")
public class TagController {

    private final ITagService tagService;

    @Log
    @Operation(summary = "添加标签信息接口")
    @PostMapping("/save")
    @PreAuthorize("sys:tag:save")
    public Result saveTag(@RequestBody @Validated TagSaveDTO tagSaveDTO) {
        return tagService.saveTag(tagSaveDTO);
    }

    @Log
    @Operation(summary = "修改标签信息接口")
    @PutMapping("/update")
    @PreAuthorize("sys:tag:update")
    public Result update(@RequestBody @Validated TagUpdateDTO tagUpdateDTO) {
        return tagService.updateTag(tagUpdateDTO);
    }

    @Log
    @Operation(summary = "删除标签信息接口")
    @DeleteMapping("/delete")
    @PreAuthorize("sys:tag:delete")
    public Result delete(@RequestParam @NotNull Long id) {
        return tagService.deleteTagById(id);
    }

    @Log
    @Operation(summary = "获取标签名称列表接口")
    @GetMapping("/list")
    public Result getTagList(@RequestParam @NotNull Integer type) {
        return tagService.getTagListByType(type);
    }

}



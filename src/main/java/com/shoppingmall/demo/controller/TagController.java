package com.shoppingmall.demo.controller;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.shoppingmall.demo.annotation.Log;
import com.shoppingmall.demo.annotation.PreAuthorize;
import com.shoppingmall.demo.config.deserializer.StringToLongDeserializer;
import com.shoppingmall.demo.model.DTO.TagDeleteBatchDTO;
import com.shoppingmall.demo.model.DTO.TagSaveBatchDTO;
import com.shoppingmall.demo.model.DTO.TagSaveDTO;
import com.shoppingmall.demo.model.DTO.TagUpdateDTO;
import com.shoppingmall.demo.model.Query.TagQuery;
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
@RestController
@RequestMapping("/tag")
public class TagController {

    private final ITagService tagService;

    @Log
    @Operation(summary = "添加标签信息接口")
    @PostMapping("/save")
    @PreAuthorize("smb:tag:save")
    public Result saveTag(@RequestBody @Validated TagSaveDTO tagSaveDTO) {
        return tagService.saveTag(tagSaveDTO);
    }

    @Log
    @Operation(summary = "修改标签信息接口")
    @PutMapping("/update")
    @PreAuthorize("smb:tag:update")
    public Result update(@RequestBody @Validated TagUpdateDTO tagUpdateDTO) {
        return tagService.updateTag(tagUpdateDTO);
    }

    @Log
    @Operation(summary = "批量添加修改标签信息")
    @PostMapping("/saveOrUpdate/batch")
    public Result saveOrUpdateBatch(@RequestBody @Validated TagSaveBatchDTO tagSaveBatchDTO) {
        return tagService.saveOrUpdateTagBatch(tagSaveBatchDTO);
    }

    @Log
    @Operation(summary = "获取标签名称列表接口")
    @GetMapping("/list")
    public Result list(@RequestParam @NotNull Integer type) {
        return tagService.getTagListByType(type);
    }

    @Log
    @Operation(summary = "分页查询标签信息列表")
    @PostMapping("/list/page")
    public Result listByPage(@RequestBody @Validated TagQuery query) {
        return tagService.pageTagListByCondition(query);
    }

    @Log
    @Operation(summary = "删除标签信息接口")
    @DeleteMapping("/delete")
    @PreAuthorize("smb:tag:delete")
    public Result delete(@RequestParam @NotNull @JsonDeserialize(using = StringToLongDeserializer.class) Long id) {
        return tagService.deleteTagById(id);
    }

    @Log
    @Operation(summary = "批量删除标签信息接口")
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody @Validated TagDeleteBatchDTO tagDeleteBatchDTO) {
        return tagService.deleteTagBatch(tagDeleteBatchDTO);
    }

}



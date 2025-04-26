package com.shoppingmall.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shoppingmall.demo.model.DO.TagDO;
import com.shoppingmall.demo.model.DTO.TagDeleteBatchDTO;
import com.shoppingmall.demo.model.DTO.TagSaveBatchDTO;
import com.shoppingmall.demo.model.DTO.TagSaveDTO;
import com.shoppingmall.demo.model.DTO.TagUpdateDTO;
import com.shoppingmall.demo.model.Query.TagQuery;
import com.shoppingmall.demo.utils.Result;

public interface ITagService extends IService<TagDO> {

    Result saveTag(TagSaveDTO tagSaveDTO);

    Result updateTag(TagUpdateDTO tagUpdateDTO);

    Result saveOrUpdateTagBatch(TagSaveBatchDTO tagSaveBatchDTO);

    String getTagNameById(Long id);

    Result getTagListByType(Integer type);

    Result pageTagListByCondition(TagQuery tagQuery);

    Result deleteTagById(Long id);

    Result deleteTagBatch(TagDeleteBatchDTO tagDeleteBatchDTO);
}

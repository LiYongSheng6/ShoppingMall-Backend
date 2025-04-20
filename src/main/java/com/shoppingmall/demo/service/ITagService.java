package com.shoppingmall.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shoppingmall.demo.model.DO.TagDO;
import com.shoppingmall.demo.model.DTO.TagSaveDTO;
import com.shoppingmall.demo.model.DTO.TagUpdateDTO;
import com.shoppingmall.demo.utils.Result;

public interface ITagService extends IService<TagDO> {

    Result saveTag(TagSaveDTO tagSaveDTO);

    Result updateTag(TagUpdateDTO tagUpdateDTO);

    Result getTagListByType(Integer type);

    Result deleteTagById(Long id);
}

package com.shoppingmall.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shoppingmall.demo.model.DO.CategoryDO;
import com.shoppingmall.demo.model.DTO.CategoryDeleteBatchDTO;
import com.shoppingmall.demo.model.DTO.CategorySaveBatchDTO;
import com.shoppingmall.demo.model.DTO.CategorySaveDTO;
import com.shoppingmall.demo.model.DTO.CategoryUpdateDTO;
import com.shoppingmall.demo.utils.Result;

public interface ICategoryService extends IService<CategoryDO> {

    Result saveCategory(CategorySaveDTO categorySaveDTO);

    Result updateCategory(CategoryUpdateDTO categoryUpdateDTO);

    Result getCategoryListByType(Integer type);

    Result deleteCategoryById(Long id);

    Result saveOrUpdateCategoryBatch(CategorySaveBatchDTO categorySaveBatchDTO);

    Result deleteCategoryBatch(CategoryDeleteBatchDTO categoryDeleteBatchDTO);
}

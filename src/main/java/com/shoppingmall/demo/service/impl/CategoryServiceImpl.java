package com.shoppingmall.demo.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.shoppingmall.demo.constant.CacheConstants;
import com.shoppingmall.demo.constant.MessageConstants;
import com.shoppingmall.demo.exception.ServiceException;
import com.shoppingmall.demo.mapper.CategoryMapper;
import com.shoppingmall.demo.model.DO.CategoryDO;
import com.shoppingmall.demo.model.DTO.CategoryDeleteBatchDTO;
import com.shoppingmall.demo.model.DTO.CategorySaveBatchDTO;
import com.shoppingmall.demo.model.DTO.CategorySaveDTO;
import com.shoppingmall.demo.model.DTO.CategoryUpdateDTO;
import com.shoppingmall.demo.model.VO.CategoryVO;
import com.shoppingmall.demo.service.ICategoryService;
import com.shoppingmall.demo.utils.RedisIdWorker;
import com.shoppingmall.demo.utils.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, CategoryDO> implements ICategoryService {

    private final RedisIdWorker redisIdWorker;

    @Override
    public Result saveCategory(CategorySaveDTO categorySaveDTO) {
        checkDuplicationColumn(null, categorySaveDTO.getCategoryName());
        return save(BeanUtil.copyProperties(categorySaveDTO, CategoryDO.class).setId(redisIdWorker.nextId(CacheConstants.CATEGORY_ID_PREFIX))) ?
                Result.success(MessageConstants.SAVE_SUCCESS) : Result.error(MessageConstants.SAVE_ERROR);
    }

    @Override
    public Result updateCategory(CategoryUpdateDTO categoryUpdateDTO) {
        checkDuplicationColumn(categoryUpdateDTO.getId(), categoryUpdateDTO.getCategoryName());
        return updateById(BeanUtil.copyProperties(categoryUpdateDTO, CategoryDO.class).setUpdateTime(LocalDateTime.now())) ?
                Result.success(MessageConstants.UPDATE_SUCCESS) : Result.error(MessageConstants.UPDATE_ERROR);
    }

    private void checkDuplicationColumn(Long id, String categoryName) {
        Optional.ofNullable(lambdaQuery().ne(id != null, CategoryDO::getId, id).eq(CategoryDO::getCategoryName, categoryName).one())
                .ifPresent(category -> {
            throw new ServiceException(MessageConstants.CATEGORY_NAME_EXIST);
        });
    }

    @Override
    public Result getCategoryListByType(Integer type) {
        List<CategoryDO> CategoryDOList = lambdaQuery().eq(CategoryDO::getType, type).list();

        if (CollectionUtils.isEmpty(CategoryDOList)) throw new ServiceException(MessageConstants.NO_FOUND_CATEGORY_ERROR);

        return Result.success(CategoryDOList.stream().map(categoryDO -> CompletableFuture.supplyAsync(() -> new CategoryVO(categoryDO))).toList()
                .stream().map(CompletableFuture::join).toList());
    }

    @Override
    public Result deleteCategoryById(Long id) {
        return removeById(id) ? Result.success(MessageConstants.DELETE_SUCCESS) : Result.error(MessageConstants.DELETE_ERROR);
    }

    @Override
    public Result saveOrUpdateCategoryBatch(CategorySaveBatchDTO categorySaveBatchDTO) {
        Integer type = categorySaveBatchDTO.getType();

        List<String> addressNameList = categorySaveBatchDTO.getCategoryNameList();
        if (CollectionUtils.isEmpty(addressNameList))
            throw new ServiceException(MessageConstants.NO_FOUND_CATEGORY_NAME_ERROR);

        List<CategoryDO> addressDOList = addressNameList.stream().map(addressName -> {
            CategoryDO categoryDO = lambdaQuery().eq(CategoryDO::getCategoryName, addressName).one();
            if (categoryDO == null)
                categoryDO = new CategoryDO().setId(redisIdWorker.nextId(CacheConstants.CATEGORY_ID_PREFIX)).setCategoryName(addressName);

            return categoryDO.setType(type);
        }).toList();

        return Db.saveOrUpdateBatch(addressDOList, addressDOList.size()) ?
                Result.success(MessageConstants.OPERATION_SUCCESS) : Result.error(MessageConstants.OPERATION_ERROR);
    }

    @Override
    public Result deleteCategoryBatch(CategoryDeleteBatchDTO categoryDeleteBatchDTO) {
        if (CollectionUtils.isEmpty(categoryDeleteBatchDTO.getCategoryNameList()))
            throw new ServiceException(MessageConstants.NO_FOUND_CATEGORY_NAME_ERROR);

        List<CategoryDO> categoryDOList = categoryDeleteBatchDTO.getCategoryNameList()
                .stream().map(addressName -> lambdaQuery().eq(CategoryDO::getCategoryName, addressName).one()).toList();

        return Db.removeByIds(categoryDOList, CategoryDO.class) ?
                Result.success(MessageConstants.OPERATION_SUCCESS) : Result.error(MessageConstants.OPERATION_ERROR);
    }
}

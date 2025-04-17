package com.shoppingmall.demo.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shoppingmall.demo.constant.CacheConstants;
import com.shoppingmall.demo.constant.MessageConstants;
import com.shoppingmall.demo.exception.ServiceException;
import com.shoppingmall.demo.mapper.CategoryMapper;
import com.shoppingmall.demo.model.DO.CategoryDO;
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
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, CategoryDO> implements ICategoryService {

    private final RedisIdWorker redisIdWorker;

    @Override
    public Result saveCategory(CategorySaveDTO categorySaveDTO) {
        return save(BeanUtil.copyProperties(categorySaveDTO, CategoryDO.class).setId(redisIdWorker.nextId(CacheConstants.CATEGORY_ID_PREFIX))) ?
                Result.success(MessageConstants.SAVE_SUCCESS) : Result.error(MessageConstants.SAVE_ERROR);
    }

    @Override
    public Result updateCategory(CategoryUpdateDTO categoryUpdateDTO) {
        return updateById(BeanUtil.copyProperties(categoryUpdateDTO, CategoryDO.class).setUpdateTime(LocalDateTime.now())) ?
                Result.success(MessageConstants.UPDATE_SUCCESS) : Result.error(MessageConstants.UPDATE_ERROR);
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
    
}

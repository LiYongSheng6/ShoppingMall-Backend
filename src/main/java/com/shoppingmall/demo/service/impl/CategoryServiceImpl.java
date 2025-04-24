package com.shoppingmall.demo.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.shoppingmall.demo.constant.CacheConstants;
import com.shoppingmall.demo.constant.MessageConstants;
import com.shoppingmall.demo.enums.CategoryType;
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
import java.util.ArrayList;
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

    private static List<CategoryVO> makeCategoryTree(List<CategoryVO> categoryList, Long pid) {
        //创建集合保存分类数据
        List<CategoryVO> categoryVOList = new ArrayList<CategoryVO>();
        //判断分类列表是否为空，如果不为空则使用分类列表，否则创建集合对象
        Optional.ofNullable(categoryList).orElse(new ArrayList<CategoryVO>())
                .stream().filter(item -> item != null && item.getParentId().equals(pid))
                .forEach(item -> {
                    //获取每一个item对象的子分类，递归生成分类树
                    List<CategoryVO> children = makeCategoryTree(categoryList, item.getId());
                    //设置子分类
                    item.setChildren(children);
                    //将分类对象添加到集合
                    categoryVOList.add(item);
                });
        //返回分类信息
        return categoryVOList;
    }

    private void checkDuplicationColumn(Long id, String categoryName) {
        Optional.ofNullable(lambdaQuery().ne(id != null, CategoryDO::getId, id).eq(CategoryDO::getCategoryName, categoryName).one())
                .ifPresent(category -> {
                    throw new ServiceException(MessageConstants.CATEGORY_NAME_EXIST);
                });
    }

    @Override
    public String getCategoryNameById(Long id) {
        CategoryDO categoryDO = getById(id);
        return categoryDO != null ? categoryDO.getCategoryName() : "NULL";
    }

    @Override
    public Result getCategoryListByType(Integer type) {
        List<CategoryDO> CategoryDOList = lambdaQuery().eq(CategoryDO::getType, type).list();

        if (CollectionUtils.isEmpty(CategoryDOList))
            throw new ServiceException(MessageConstants.NO_FOUND_CATEGORY_ERROR);

        return Result.success(CategoryDOList.stream().map(categoryDO -> CompletableFuture.supplyAsync(() -> new CategoryVO(categoryDO))).toList()
                .stream().map(CompletableFuture::join).toList());
    }

    @Override
    public Result getCategoryTreeInfo() {
        List<CategoryDO> categoryDOList = lambdaQuery().list();
        if (CollectionUtils.isEmpty(categoryDOList))
            throw new ServiceException(MessageConstants.NO_FOUND_CATEGORY_ERROR);

        List<CategoryVO> categoryVOList = categoryDOList.stream()
                .map(item -> CompletableFuture.supplyAsync(() -> new CategoryVO(item).setParentName(getCategoryNameById(item.getParentId())))).toList()
                .stream().map(CompletableFuture::join).toList();
        return Result.success(makeCategoryTree(categoryVOList, 0L));
    }

    @Override
    public Result deleteCategoryById(Long id) {
        return removeById(id) ? Result.success(MessageConstants.DELETE_SUCCESS) : Result.error(MessageConstants.DELETE_ERROR);
    }

    @Override
    public Result saveOrUpdateCategoryBatch(CategorySaveBatchDTO categorySaveBatchDTO) {
        CategoryType type = categorySaveBatchDTO.getType();

        List<String> categoryNameList = categorySaveBatchDTO.getCategoryNameList();
        if (CollectionUtils.isEmpty(categoryNameList))
            throw new ServiceException(MessageConstants.NO_FOUND_CATEGORY_NAME_ERROR);

        List<CategoryDO> addressDOList = categoryNameList.stream().map(categoryName -> {
            CategoryDO categoryDO = lambdaQuery().eq(CategoryDO::getCategoryName, categoryName).one();
            if (categoryDO == null)
                categoryDO = new CategoryDO().setId(redisIdWorker.nextId(CacheConstants.CATEGORY_ID_PREFIX)).setCategoryName(categoryName);
            return categoryDO.setType(type).setUpdateTime(LocalDateTime.now());
        }).toList();

        return Db.saveOrUpdateBatch(addressDOList, addressDOList.size()) ?
                Result.success(MessageConstants.OPERATION_SUCCESS) : Result.error(MessageConstants.OPERATION_ERROR);
    }

    @Override
    public Result deleteCategoryBatch(CategoryDeleteBatchDTO categoryDeleteBatchDTO) {
        if (CollectionUtils.isEmpty(categoryDeleteBatchDTO.getCategoryNameList()))
            throw new ServiceException(MessageConstants.NO_FOUND_CATEGORY_NAME_ERROR);

        List<CategoryDO> categoryDOList = categoryDeleteBatchDTO.getCategoryNameList()
                .stream().map(categoryName -> lambdaQuery().eq(CategoryDO::getCategoryName, categoryName).one()).toList();

        return Db.removeByIds(categoryDOList, CategoryDO.class) ?
                Result.success(MessageConstants.OPERATION_SUCCESS) : Result.error(MessageConstants.OPERATION_ERROR);
    }

}

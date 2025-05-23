package com.shoppingmall.demo.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.shoppingmall.demo.constant.CacheConstants;
import com.shoppingmall.demo.constant.MessageConstants;
import com.shoppingmall.demo.enums.TagType;
import com.shoppingmall.demo.exception.ServiceException;
import com.shoppingmall.demo.mapper.TagMapper;
import com.shoppingmall.demo.model.DO.TagDO;
import com.shoppingmall.demo.model.DTO.TagDeleteBatchDTO;
import com.shoppingmall.demo.model.DTO.TagSaveBatchDTO;
import com.shoppingmall.demo.model.DTO.TagSaveDTO;
import com.shoppingmall.demo.model.DTO.TagUpdateDTO;
import com.shoppingmall.demo.model.Query.TagQuery;
import com.shoppingmall.demo.model.VO.PageVO;
import com.shoppingmall.demo.model.VO.TagVO;
import com.shoppingmall.demo.service.ITagService;
import com.shoppingmall.demo.utils.RedisIdWorker;
import com.shoppingmall.demo.utils.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class TagServiceImpl extends ServiceImpl<TagMapper, TagDO> implements ITagService {

    private final RedisIdWorker redisIdWorker;

    @Override
    public Result saveTag(TagSaveDTO tagSaveDTO) {
        checkDuplicationColumn(null, tagSaveDTO.getTagName());
        return save(BeanUtil.copyProperties(tagSaveDTO, TagDO.class).setId(redisIdWorker.nextId(CacheConstants.TAG_ID_PREFIX))) ?
                Result.success(MessageConstants.SAVE_SUCCESS) : Result.error(MessageConstants.SAVE_ERROR);
    }

    @Override
    public Result updateTag(TagUpdateDTO tagUpdateDTO) {
        checkDuplicationColumn(tagUpdateDTO.getId(), tagUpdateDTO.getTagName());
        return updateById(BeanUtil.copyProperties(tagUpdateDTO, TagDO.class).setUpdateTime(LocalDateTime.now())) ?
                Result.success(MessageConstants.UPDATE_SUCCESS) : Result.error(MessageConstants.UPDATE_ERROR);
    }

    @Override
    public Result saveOrUpdateTagBatch(TagSaveBatchDTO TagSaveBatchDTO) {
        TagType type = TagSaveBatchDTO.getType();

        List<String> tagNameList = TagSaveBatchDTO.getTagNameList();
        if (CollectionUtils.isEmpty(tagNameList))
            throw new ServiceException(MessageConstants.NO_FOUND_TAG_NAME_ERROR);

        List<TagDO> addressDOList = tagNameList.stream().map(tagName -> {
            TagDO tagDO = lambdaQuery().eq(TagDO::getTagName, tagName).one();
            if (tagDO == null)
                tagDO = new TagDO().setId(redisIdWorker.nextId(CacheConstants.TAG_ID_PREFIX)).setTagName(tagName);
            return tagDO.setType(type).setUpdateTime(LocalDateTime.now());
        }).toList();

        return Db.saveOrUpdateBatch(addressDOList, addressDOList.size()) ?
                Result.success(MessageConstants.OPERATION_SUCCESS) : Result.error(MessageConstants.OPERATION_ERROR);
    }

    private void checkDuplicationColumn(Long id, String tagName) {
        Optional.ofNullable(lambdaQuery().ne(id != null, TagDO::getId, id).eq(TagDO::getTagName, tagName).one()).ifPresent(tag -> {
            throw new ServiceException(MessageConstants.TAG_NAME_EXIST);
        });
    }

    @Override
    public String getTagNameById(Long id) {
        TagDO tagDO = getById(id);
        return tagDO != null ? tagDO.getTagName() : "NULL";
    }

    @Override
    public Result getTagListByType(Integer type) {
        List<TagDO> tagDOList = lambdaQuery().eq(TagDO::getType, type).list();
        if (CollectionUtils.isEmpty(tagDOList)) throw new ServiceException(MessageConstants.NO_FOUND_TAG_ERROR);

        return Result.success(tagDOList.stream().map(tagDO -> CompletableFuture.supplyAsync(() -> new TagVO(tagDO))).toList()
                .stream().map(CompletableFuture::join).toList());
    }

    @Override
    public Result pageTagListByCondition(TagQuery tagQuery) {
        Page<TagDO> page = tagQuery.toMpPageDefaultSortByUpdateTime();
        String tagName = tagQuery.getTagName();
        TagType type = tagQuery.getType();

        Page<TagDO> pageDO = lambdaQuery()
                .like(StringUtils.hasLength(tagName), TagDO::getTagName, tagName)
                .eq(type != null, TagDO::getType, type)
                .page(page);

        if (CollectionUtils.isEmpty(pageDO.getRecords()))
            return Result.success(new PageVO<>(0L, 0L, 0L, new ArrayList<TagVO>()));

        return Result.success(PageVO.of(pageDO, TagVO::new));
    }

    @Override
    public Result deleteTagById(Long id) {
        return removeById(id) ? Result.success(MessageConstants.DELETE_SUCCESS) : Result.error(MessageConstants.DELETE_ERROR);
    }

    @Override
    public Result deleteTagBatch(TagDeleteBatchDTO tagDeleteBatchDTO) {
        if (CollectionUtils.isEmpty(tagDeleteBatchDTO.getTagNameList()))
            throw new ServiceException(MessageConstants.NO_FOUND_TAG_NAME_ERROR);

        List<TagDO> TagDOList = tagDeleteBatchDTO.getTagNameList()
                .stream().map(tagName -> lambdaQuery().eq(TagDO::getTagName, tagName).one()).toList();

        return Db.removeByIds(TagDOList, TagDO.class) ?
                Result.success(MessageConstants.OPERATION_SUCCESS) : Result.error(MessageConstants.OPERATION_ERROR);
    }

}

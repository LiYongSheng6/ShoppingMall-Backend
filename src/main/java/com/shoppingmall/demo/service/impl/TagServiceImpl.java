package com.shoppingmall.demo.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shoppingmall.demo.constant.CacheConstants;
import com.shoppingmall.demo.constant.MessageConstants;
import com.shoppingmall.demo.exception.ServiceException;
import com.shoppingmall.demo.mapper.TagMapper;
import com.shoppingmall.demo.model.DO.TagDO;
import com.shoppingmall.demo.model.DTO.TagSaveDTO;
import com.shoppingmall.demo.model.DTO.TagUpdateDTO;
import com.shoppingmall.demo.model.VO.TagVO;
import com.shoppingmall.demo.service.ITagService;
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

    private void checkDuplicationColumn(Long id, String tagName) {
        Optional.ofNullable(lambdaQuery().ne(id != null, TagDO::getId, id).eq(TagDO::getTagName, tagName).one()).ifPresent(tag -> {
            throw new ServiceException(MessageConstants.TAG_NAME_EXIST);
        });
    }

    @Override
    public Result getTagListByType(Integer type) {
        List<TagDO> tagDOList = lambdaQuery().eq(TagDO::getType, type).list();
        if (CollectionUtils.isEmpty(tagDOList)) throw new ServiceException(MessageConstants.NO_FOUND_TAG_ERROR);

        return Result.success(tagDOList.stream().map(tagDO -> CompletableFuture.supplyAsync(() -> new TagVO(tagDO))).toList()
                .stream().map(CompletableFuture::join).toList());
    }

    @Override
    public Result deleteTagById(Long id) {
        return removeById(id) ? Result.success(MessageConstants.DELETE_SUCCESS) : Result.error(MessageConstants.DELETE_ERROR);
    }

}

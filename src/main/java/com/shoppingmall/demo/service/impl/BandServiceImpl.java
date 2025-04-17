package com.shoppingmall.demo.service.impl;

import cn.hutool.core.bean.BeanUtil;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shoppingmall.demo.constant.CacheConstants;
import com.shoppingmall.demo.constant.MessageConstants;
import com.shoppingmall.demo.exception.ServiceException;
import com.shoppingmall.demo.mapper.BandMapper;
import com.shoppingmall.demo.model.DO.BandDO;
import com.shoppingmall.demo.model.DTO.BandSaveDTO;
import com.shoppingmall.demo.model.DTO.BandUpdateDTO;
import com.shoppingmall.demo.model.VO.BandVO;
import com.shoppingmall.demo.service.IBandService;
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
public class BandServiceImpl extends ServiceImpl<BandMapper, BandDO> implements IBandService {

    private final RedisIdWorker redisIdWorker;

    @Override
    public Result saveBand(BandSaveDTO bandSaveDTO) {
        return save(BeanUtil.copyProperties(bandSaveDTO, BandDO.class).setId(redisIdWorker.nextId(CacheConstants.BAND_ID_PREFIX))) ?
                Result.success(MessageConstants.SAVE_SUCCESS) : Result.error(MessageConstants.SAVE_ERROR);
    }

    @Override
    public Result updateBand(BandUpdateDTO bandUpdateDTO) {
        return updateById(BeanUtil.copyProperties(bandUpdateDTO, BandDO.class).setUpdateTime(LocalDateTime.now())) ?
                Result.success(MessageConstants.UPDATE_SUCCESS) : Result.error(MessageConstants.UPDATE_ERROR);
    }

    @Override
    public Result getBandListByType(Integer type) {
        List<BandDO> BandDOList = lambdaQuery().eq(BandDO::getType, type).list();

        if (CollectionUtils.isEmpty(BandDOList)) throw new ServiceException(MessageConstants.NO_FOUND_BAND_ERROR);

        return Result.success(BandDOList.stream().map(bandDO -> CompletableFuture.supplyAsync(() -> new BandVO(bandDO))).toList()
                .stream().map(CompletableFuture::join).toList());
    }

    @Override
    public Result deleteBandById(Long id) {
        return removeById(id) ? Result.success(MessageConstants.DELETE_SUCCESS) : Result.error(MessageConstants.DELETE_ERROR);
    }
    
}

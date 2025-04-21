package com.shoppingmall.demo.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shoppingmall.demo.constant.CacheConstants;
import com.shoppingmall.demo.constant.MessageConstants;
import com.shoppingmall.demo.exception.ServiceException;
import com.shoppingmall.demo.mapper.DeliveryMapper;
import com.shoppingmall.demo.model.DO.DeliveryDO;
import com.shoppingmall.demo.model.DTO.DeliverySaveDTO;
import com.shoppingmall.demo.model.DTO.DeliveryUpdateDTO;
import com.shoppingmall.demo.model.VO.DeliveryVO;
import com.shoppingmall.demo.service.IAddressService;
import com.shoppingmall.demo.service.IDeliveryService;
import com.shoppingmall.demo.service.common.LoginInfoService;
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
public class DeliveryServiceImpl extends ServiceImpl<DeliveryMapper, DeliveryDO> implements IDeliveryService {

    private final RedisIdWorker redisIdWorker;
    private final LoginInfoService loginInfoService;
    private final IAddressService addressService;

    @Override
    public Result saveDelivery(DeliverySaveDTO DeliverySaveDTO) {
        return save(BeanUtil.copyProperties(DeliverySaveDTO, DeliveryDO.class).setId(redisIdWorker.nextId(CacheConstants.DELIVERY_ID_PREFIX))) ?
                Result.success(MessageConstants.SAVE_SUCCESS) : Result.error(MessageConstants.SAVE_ERROR);
    }

    @Override
    public Result updateDelivery(DeliveryUpdateDTO DeliveryUpdateDTO) {
        return updateById(BeanUtil.copyProperties(DeliveryUpdateDTO, DeliveryDO.class).setUpdateTime(LocalDateTime.now())) ?
                Result.success(MessageConstants.UPDATE_SUCCESS) : Result.error(MessageConstants.UPDATE_ERROR);
    }

    @Override
    public Result getDeliveryById(Long id) {
        DeliveryDO deliveryDO = getById(id);
        Optional.ofNullable(deliveryDO).orElseThrow(() -> new ServiceException(MessageConstants.NO_FOUND_DELIVERY_ERROR));

        return Result.success(BeanUtil.copyProperties(deliveryDO, DeliveryVO.class)
                .setProvince(addressService.getById(deliveryDO.getProvinceId()).getAddressName())
                .setCity(addressService.getById(deliveryDO.getCityId()).getAddressName())
                .setCounty(addressService.getById(deliveryDO.getCountyId()).getAddressName()));
    }

    @Override
    public Result getDeliveryList() {
       return getDeliveryListByUserId(loginInfoService.getLoginId());
    }

    @Override
    public Result getDeliveryListByUserId(Long userId) {
        List<DeliveryDO> deliveryDOList = lambdaQuery().eq(DeliveryDO::getId, userId).list();

        if (CollectionUtils.isEmpty(deliveryDOList)) throw new ServiceException(MessageConstants.NO_FOUND_DELIVERY_ERROR);

        return Result.success(deliveryDOList.stream().map(deliveryDO -> CompletableFuture.supplyAsync(() -> new DeliveryVO(deliveryDO))).toList()
                .stream().map(CompletableFuture::join).toList());
    }

    @Override
    public Result deleteDeliveryById(Long id) {
        return removeById(id) ? Result.success(MessageConstants.DELETE_SUCCESS) : Result.error(MessageConstants.DELETE_ERROR);
    }
    
}

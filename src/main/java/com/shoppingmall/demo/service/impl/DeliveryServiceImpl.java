package com.shoppingmall.demo.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.shoppingmall.demo.constant.CacheConstants;
import com.shoppingmall.demo.constant.MessageConstants;
import com.shoppingmall.demo.enums.AddressType;
import com.shoppingmall.demo.exception.ServiceException;
import com.shoppingmall.demo.mapper.DeliveryMapper;
import com.shoppingmall.demo.model.DO.DeliveryDO;
import com.shoppingmall.demo.model.DTO.DeliveryDeleteBatchDTO;
import com.shoppingmall.demo.model.DTO.DeliverySaveDTO;
import com.shoppingmall.demo.model.DTO.DeliverySaveIdDTO;
import com.shoppingmall.demo.model.DTO.DeliveryUpdateDTO;
import com.shoppingmall.demo.model.DTO.DeliveryUpdateIdDTO;
import com.shoppingmall.demo.model.Query.DeliveryQuery;
import com.shoppingmall.demo.model.VO.DeliveryVO;
import com.shoppingmall.demo.model.VO.PageVO;
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
import java.util.ArrayList;
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
    public Result saveDelivery(DeliverySaveDTO deliverySaveDTO) {
        return save(BeanUtil.copyProperties(deliverySaveDTO, DeliveryDO.class)
                .setId(redisIdWorker.nextId(CacheConstants.DELIVERY_ID_PREFIX))
                .setUserId((loginInfoService.getLoginId()))
                .setProvinceId((addressService.getAddressIdByName(deliverySaveDTO.getProvince(), AddressType.PROVINCE)))
                .setCityId((addressService.getAddressIdByName(deliverySaveDTO.getCity(), AddressType.CITY)))
                .setCountyId((addressService.getAddressIdByName(deliverySaveDTO.getCounty(), AddressType.COUNTY)))) ?
                Result.success(MessageConstants.SAVE_SUCCESS) : Result.error(MessageConstants.SAVE_ERROR);
    }

    @Override
    public Result updateDelivery(DeliveryUpdateDTO deliveryUpdateDTO) {
        loginInfoService.CheckLoginUserObject(getUserIdByDeliveryId(deliveryUpdateDTO.getId()));
        return updateById(BeanUtil.copyProperties(deliveryUpdateDTO, DeliveryDO.class)
                .setProvinceId((addressService.getAddressIdByName(deliveryUpdateDTO.getProvince(), AddressType.PROVINCE)))
                .setCityId((addressService.getAddressIdByName(deliveryUpdateDTO.getCity(), AddressType.CITY)))
                .setCountyId((addressService.getAddressIdByName(deliveryUpdateDTO.getCounty(), AddressType.COUNTY)))
                .setUpdateTime(LocalDateTime.now())) ?
                Result.success(MessageConstants.UPDATE_SUCCESS) : Result.error(MessageConstants.UPDATE_ERROR);
    }

    @Override
    public Result saveIdDelivery(DeliverySaveIdDTO deliverySaveDTO) {
        return save(BeanUtil.copyProperties(deliverySaveDTO, DeliveryDO.class)
                .setId(redisIdWorker.nextId(CacheConstants.DELIVERY_ID_PREFIX))
                .setUserId((loginInfoService.getLoginId()))) ?
                Result.success(MessageConstants.SAVE_SUCCESS) : Result.error(MessageConstants.SAVE_ERROR);
    }

    @Override
    public Result updateIdDelivery(DeliveryUpdateIdDTO deliveryUpdateDTO) {
        loginInfoService.CheckLoginUserObject(getUserIdByDeliveryId(deliveryUpdateDTO.getId()));
        return updateById(BeanUtil.copyProperties(deliveryUpdateDTO, DeliveryDO.class).setUpdateTime(LocalDateTime.now())) ?
                Result.success(MessageConstants.UPDATE_SUCCESS) : Result.error(MessageConstants.UPDATE_ERROR);
    }

    @Override
    public Result deleteDeliveryById(Long id) {
        loginInfoService.CheckLoginUserObject(getUserIdByDeliveryId(id));
        return removeById(id) ? Result.success(MessageConstants.DELETE_SUCCESS) : Result.error(MessageConstants.DELETE_ERROR);
    }

    @Override
    public Result deleteDeliveryBatch(DeliveryDeleteBatchDTO deleteBatchDTO) {
        List<DeliveryDO> deliveryDOList = lambdaQuery().eq(DeliveryDO::getUserId, loginInfoService.getLoginId())
                .in(DeliveryDO::getId, deleteBatchDTO.getDeliveryIds()).list();
        if (CollectionUtils.isEmpty(deliveryDOList))
            throw new ServiceException(MessageConstants.NO_FOUND_DELIVERY_ERROR);

        return Db.removeByIds(deliveryDOList, DeliveryDO.class) ?
                Result.success(MessageConstants.OPERATION_SUCCESS) : Result.error(MessageConstants.OPERATION_ERROR);
    }

    private Long getUserIdByDeliveryId(Long id) {
        DeliveryDO deliveryDO = getById(id);
        return deliveryDO != null ? deliveryDO.getUserId() : null;
    }

    @Override
    public Result getDeliveryById(Long id) {
        DeliveryDO deliveryDO = getById(id);
        Optional.ofNullable(deliveryDO).orElseThrow(() -> new ServiceException(MessageConstants.NO_FOUND_DELIVERY_ERROR));

        return Result.success(BeanUtil.copyProperties(deliveryDO, DeliveryVO.class)
                .setProvince(addressService.getAddressNameById((deliveryDO.getProvinceId())))
                .setCity(addressService.getAddressNameById((deliveryDO.getCityId())))
                .setCounty(addressService.getAddressNameById((deliveryDO.getCountyId()))));
    }

    @Override
    public Result getDeliveryList() {
        return getDeliveryListByUserId(loginInfoService.getLoginId());
    }

    @Override
    public Result getDeliveryListByUserId(Long userId) {
        List<DeliveryDO> deliveryDOList = lambdaQuery().eq(DeliveryDO::getUserId, userId).list();
        if (CollectionUtils.isEmpty(deliveryDOList)) throw new ServiceException(MessageConstants.NO_FOUND_DELIVERY_ERROR);

        return Result.success(deliveryDOList.stream()
                .map(deliveryDO -> CompletableFuture.supplyAsync(() -> new DeliveryVO(deliveryDO)
                        .setProvince(addressService.getAddressNameById((deliveryDO.getProvinceId())))
                        .setCity(addressService.getAddressNameById((deliveryDO.getCityId())))
                        .setCounty(addressService.getAddressNameById((deliveryDO.getCountyId())))))
                .toList().stream().map(CompletableFuture::join).toList());
    }

    @Override
    public Result pageDeliveryList(DeliveryQuery query) {
        query.setUserId(loginInfoService.getLoginId());
        return pageDeliveryListByCondition(query);
    }

    @Override
    public Result pageDeliveryListByCondition(DeliveryQuery query) {
        Page<DeliveryDO> page = query.toMpPageDefaultSortByUpdateTime();
        Long userId = query.getUserId();

        Page<DeliveryDO> pageDO = lambdaQuery()
                .eq(userId != null, DeliveryDO::getUserId, userId)
                .page(page);

        if (CollectionUtils.isEmpty(pageDO.getRecords()))
            return Result.success(new PageVO<>(0L, 0L, 0L, new ArrayList<DeliveryVO>()));

        return Result.success(PageVO.of(pageDO, DeliveryVO::new));
    }

}

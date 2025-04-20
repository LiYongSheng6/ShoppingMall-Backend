package com.shoppingmall.demo.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shoppingmall.demo.constant.CacheConstants;
import com.shoppingmall.demo.constant.MessageConstants;
import com.shoppingmall.demo.exception.ServiceException;
import com.shoppingmall.demo.mapper.AddressMapper;
import com.shoppingmall.demo.mapper.AddressMapper;
import com.shoppingmall.demo.model.DO.AddressDO;
import com.shoppingmall.demo.model.DO.AddressDO;
import com.shoppingmall.demo.model.DO.DeliveryDO;
import com.shoppingmall.demo.model.DTO.AddressSaveDTO;
import com.shoppingmall.demo.model.DTO.AddressUpdateDTO;
import com.shoppingmall.demo.model.VO.AddressVO;
import com.shoppingmall.demo.model.VO.DeliveryVO;
import com.shoppingmall.demo.service.IAddressService;
import com.shoppingmall.demo.service.IAddressService;
import com.shoppingmall.demo.service.common.LoginInfoService;
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
public class AddressServiceImpl extends ServiceImpl<AddressMapper, AddressDO> implements IAddressService {

    private final RedisIdWorker redisIdWorker;

    @Override
    public Result saveAddress(AddressSaveDTO addressSaveDTO) {
        return save(BeanUtil.copyProperties(addressSaveDTO, AddressDO.class).setId(redisIdWorker.nextId(CacheConstants.ADDRESS_ID_PREFIX))) ?
                Result.success(MessageConstants.SAVE_SUCCESS) : Result.error(MessageConstants.SAVE_ERROR);
    }

    @Override
    public Result updateAddress(AddressUpdateDTO addressUpdateDTO) {
        return updateById(BeanUtil.copyProperties(addressUpdateDTO, AddressDO.class).setUpdateTime(LocalDateTime.now())) ?
                Result.success(MessageConstants.UPDATE_SUCCESS) : Result.error(MessageConstants.UPDATE_ERROR);
    }

    @Override
    public Result getAddressListByIdAndType(Long parentId, Integer type) {
        List<AddressDO> addressDOList = lambdaQuery()
                .eq(parentId != null, AddressDO::getParentId, parentId)
                .eq(type != null, AddressDO::getType, type).list();

        if(CollectionUtils.isEmpty(addressDOList)) throw new ServiceException(MessageConstants.NO_FOUND_ADDRESS_ERROR);

        return Result.success(addressDOList.stream().map(addressDO -> CompletableFuture.supplyAsync(() -> new AddressVO(addressDO))).toList()
                .stream().map(CompletableFuture::join).toList());
     }

    @Override
    public Result deleteAddressById(Long id) {
        return removeById(id) ? Result.success(MessageConstants.DELETE_SUCCESS) : Result.error(MessageConstants.DELETE_ERROR);
    }
    
}

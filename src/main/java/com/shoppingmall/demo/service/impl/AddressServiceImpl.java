package com.shoppingmall.demo.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.shoppingmall.demo.constant.CacheConstants;
import com.shoppingmall.demo.constant.MessageConstants;
import com.shoppingmall.demo.enums.AddressType;
import com.shoppingmall.demo.exception.ServiceException;
import com.shoppingmall.demo.mapper.AddressMapper;
import com.shoppingmall.demo.model.DO.AddressDO;
import com.shoppingmall.demo.model.DTO.AddressDeleteBatchDTO;
import com.shoppingmall.demo.model.DTO.AddressSaveBatchDTO;
import com.shoppingmall.demo.model.DTO.AddressSaveDTO;
import com.shoppingmall.demo.model.DTO.AddressUpdateDTO;
import com.shoppingmall.demo.model.VO.AddressVO;
import com.shoppingmall.demo.service.IAddressService;
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
public class AddressServiceImpl extends ServiceImpl<AddressMapper, AddressDO> implements IAddressService {

    private final RedisIdWorker redisIdWorker;

    @Override
    public Result saveAddress(AddressSaveDTO addressSaveDTO) {
        checkDuplicationColumn(null, addressSaveDTO.getAddressName());
        return save(BeanUtil.copyProperties(addressSaveDTO, AddressDO.class).setId(redisIdWorker.nextId(CacheConstants.ADDRESS_ID_PREFIX))) ?
                Result.success(MessageConstants.SAVE_SUCCESS) : Result.error(MessageConstants.SAVE_ERROR);
    }

    @Override
    public Result updateAddress(AddressUpdateDTO addressUpdateDTO) {
        checkDuplicationColumn(addressUpdateDTO.getId(), addressUpdateDTO.getAddressName());
        return updateById(BeanUtil.copyProperties(addressUpdateDTO, AddressDO.class).setUpdateTime(LocalDateTime.now())) ?
                Result.success(MessageConstants.UPDATE_SUCCESS) : Result.error(MessageConstants.UPDATE_ERROR);
    }

    private static List<AddressVO> makeAddressTree(List<AddressVO> addressList, Long pid) {
        //创建集合保存分类数据
        List<AddressVO> addressVOList = new ArrayList<AddressVO>();
        //判断分类列表是否为空，如果不为空则使用分类列表，否则创建集合对象
        Optional.ofNullable(addressList).orElse(new ArrayList<AddressVO>())
                .stream().filter(item -> item != null && item.getParentId().equals(pid))
                .forEach(item -> {
                    //获取每一个item对象的子分类，递归生成分类树
                    List<AddressVO> children = makeAddressTree(addressList, item.getId());
                    //设置子分类
                    item.setChildren(children);
                    //将分类对象添加到集合
                    addressVOList.add(item);
                });
        //返回分类信息
        return addressVOList;
    }

    private void checkDuplicationColumn(Long id, String addressName) {
        Optional.ofNullable(lambdaQuery().ne(id != null, AddressDO::getId, id).eq(AddressDO::getAddressName, addressName).one())
                .ifPresent(addressDO -> {
            throw new ServiceException(MessageConstants.ADDRESS_NAME_EXIST);
        });
    }

    @Override
    public String getAddressNameById(Long id) {
        AddressDO addressDO = getById(id);
        return addressDO != null ? addressDO.getAddressName() : "NULL";
    }

    @Override
    public Long getAddressIdByName(String name, AddressType type) {
        AddressDO addressDO = lambdaQuery().eq(AddressDO::getAddressName, name).eq(AddressDO::getType, type).one();
        if (addressDO == null)
            throw new ServiceException(MessageConstants.NO_FOUND_ADDRESS_ERROR + ": " + name + "-" + type.getDesc());
        return addressDO.getId();
    }

    @Override
    public Result deleteAddressById(Long id) {
        return removeById(id) ? Result.success(MessageConstants.DELETE_SUCCESS) : Result.error(MessageConstants.DELETE_ERROR);
    }

    public Result getAddressListByIdAndType(Long parentId, Integer type) {
        List<AddressDO> addressDOList = lambdaQuery()
                .eq(parentId != null, AddressDO::getParentId, parentId)
                .eq(type != null, AddressDO::getType, type).list();

        if (CollectionUtils.isEmpty(addressDOList)) throw new ServiceException(MessageConstants.NO_FOUND_ADDRESS_ERROR);

        return Result.success(addressDOList.stream().map(addressDO -> CompletableFuture.supplyAsync(() -> new AddressVO(addressDO))).toList()
                .stream().map(CompletableFuture::join).toList());
    }

    @Override
    public Result deleteAddressBatch(AddressDeleteBatchDTO addressDeleteBatchDTO) {
        if (CollectionUtils.isEmpty(addressDeleteBatchDTO.getAddressNameList()))
            throw new ServiceException(MessageConstants.NO_FOUND_ADDRESS_NAME_ERROR);

        List<AddressDO> addressDOList = addressDeleteBatchDTO.getAddressNameList()
                .stream().map(addressName -> lambdaQuery().eq(AddressDO::getAddressName, addressName).one()).toList();

        return Db.removeByIds(addressDOList, AddressDO.class) ?
                Result.success(MessageConstants.OPERATION_SUCCESS) : Result.error(MessageConstants.OPERATION_ERROR);
    }

    @Override
    public Result saveOrUpdateAddressBatch(AddressSaveBatchDTO addressSaveBatchDTO) {
        Long parentId = addressSaveBatchDTO.getParentId();
        AddressType type = addressSaveBatchDTO.getType();

        List<String> addressNameList = addressSaveBatchDTO.getAddressNameList();
        if (CollectionUtils.isEmpty(addressNameList))
            throw new ServiceException(MessageConstants.NO_FOUND_ADDRESS_NAME_ERROR);

        List<AddressDO> addressDOList = addressNameList.stream().map(addressName -> {
            AddressDO addressDO = lambdaQuery().eq(AddressDO::getAddressName, addressName).one();
            if (addressDO == null)
                addressDO = new AddressDO().setId(redisIdWorker.nextId(CacheConstants.ADDRESS_ID_PREFIX)).setAddressName(addressName);
            addressDO.setParentId(parentId).setType(type).setUpdateTime(LocalDateTime.now());
            return addressDO;
        }).toList();

        return Db.saveOrUpdateBatch(addressDOList, addressDOList.size()) ?
                Result.success(MessageConstants.OPERATION_SUCCESS) : Result.error(MessageConstants.OPERATION_ERROR);
    }

    @Override
    public Result getAddressTreeInfo() {
        List<AddressDO> addressDOList = lambdaQuery().list();
        if (CollectionUtils.isEmpty(addressDOList))
            throw new ServiceException(MessageConstants.NO_FOUND_ADDRESS_ERROR);

        List<AddressVO> addressVOList = addressDOList.stream()
                .map(item -> CompletableFuture.supplyAsync(() -> new AddressVO(item).setParentName(getAddressNameById(item.getParentId())))).toList()
                .stream().map(CompletableFuture::join).toList();
        return Result.success(makeAddressTree(addressVOList, 0L));
    }

}

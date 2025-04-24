package com.shoppingmall.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shoppingmall.demo.enums.AddressType;
import com.shoppingmall.demo.model.DO.AddressDO;
import com.shoppingmall.demo.model.DTO.AddressDeleteBatchDTO;
import com.shoppingmall.demo.model.DTO.AddressSaveBatchDTO;
import com.shoppingmall.demo.model.DTO.AddressSaveDTO;
import com.shoppingmall.demo.model.DTO.AddressUpdateDTO;
import com.shoppingmall.demo.utils.Result;

public interface IAddressService extends IService<AddressDO> {

    Result saveAddress(AddressSaveDTO addressSaveDTO);

    Result updateAddress(AddressUpdateDTO addressUpdateDTO);

    Result getAddressListByIdAndType(Long parentId, Integer type);

    String getAddressNameById(Long id);

    Long getAddressIdByName(String province, AddressType type);

    Result deleteAddressById(Long id);

    Result saveOrUpdateAddressBatch(AddressSaveBatchDTO addressSaveBatchDTO);

    Result deleteAddressBatch(AddressDeleteBatchDTO addressDeleteBatchDTO);

    Result getAddressTreeInfo();

}

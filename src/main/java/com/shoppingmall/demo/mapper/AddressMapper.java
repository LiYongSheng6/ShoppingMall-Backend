package com.shoppingmall.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shoppingmall.demo.model.DO.AddressDO;
import com.shoppingmall.demo.model.DO.DeliveryDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AddressMapper extends BaseMapper<AddressDO> {
}

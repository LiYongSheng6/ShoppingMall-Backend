package com.shoppingmall.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shoppingmall.demo.model.DO.DeliveryDO;
import com.shoppingmall.demo.model.DTO.DeliverySaveDTO;
import com.shoppingmall.demo.model.DTO.DeliveryUpdateDTO;
import com.shoppingmall.demo.utils.Result;

public interface IDeliveryService extends IService<DeliveryDO> {

    Result saveDelivery(DeliverySaveDTO DeliverySaveDTO);

    Result updateDelivery(DeliveryUpdateDTO DeliveryUpdateDTO);

    Result getDeliveryById(Long id);

    Result getDeliveryList();

    Result getDeliveryListByUserId(Long userId);

    Result deleteDeliveryById(Long id);
}

package com.shoppingmall.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shoppingmall.demo.model.DO.DeliveryDO;
import com.shoppingmall.demo.model.DTO.DeliveryDeleteBatchDTO;
import com.shoppingmall.demo.model.DTO.DeliverySaveDTO;
import com.shoppingmall.demo.model.DTO.DeliverySaveIdDTO;
import com.shoppingmall.demo.model.DTO.DeliveryUpdateDTO;
import com.shoppingmall.demo.model.DTO.DeliveryUpdateIdDTO;
import com.shoppingmall.demo.utils.Result;

public interface IDeliveryService extends IService<DeliveryDO> {

    Result saveDelivery(DeliverySaveDTO DeliverySaveDTO);

    Result updateDelivery(DeliveryUpdateDTO DeliveryUpdateDTO);

    Result saveIdDelivery(DeliverySaveIdDTO deliverySaveDTO);

    Result updateIdDelivery(DeliveryUpdateIdDTO deliveryUpdateDTO);

    Result getDeliveryById(Long id);

    Result getDeliveryList();

    Result getDeliveryListByUserId(Long userId);

    Result deleteDeliveryById(Long id);

    Result deleteDeliveryBatch(DeliveryDeleteBatchDTO deleteBatchDTO);
}

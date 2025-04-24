package com.shoppingmall.demo.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.shoppingmall.demo.enums.OrderStatus;
import com.shoppingmall.demo.model.DO.OrderDO;
import com.shoppingmall.demo.model.DTO.OrderDeleteBatchDTO;
import com.shoppingmall.demo.model.DTO.OrderSaveDTO;
import com.shoppingmall.demo.model.DTO.OrderUpdateDTO;
import com.shoppingmall.demo.model.Query.OrderQuery;
import com.shoppingmall.demo.model.VO.OrderVO;
import com.shoppingmall.demo.utils.Result;

/**
 * @author redmi k50 ultra
 * * @date 2024/10/10
 */
public interface IOrderService extends IService<OrderDO> {

    Result<OrderVO> getOrderById(Long orderId);

    Result saveOrder(OrderSaveDTO orderSaveDTO);

    Result updateOrder(OrderUpdateDTO orderUpdateDTO, OrderStatus type);

    Result deleteOrderById(Long orderId);

    Result deleteOrderBatch(OrderDeleteBatchDTO deleteBatchDTO);

    Result getMyOrderListPage(OrderQuery orderQuery);

    Result pageOrderListByUserId(OrderQuery orderQuery);

    Result updateToBeShipping(Long orderId);

    Result updateToBeReceiving(Long orderId);

    Result updateToBeCompleted(Long orderId);

    Result updateToBeCancelled(Long orderId);

    Result updateDelivery(Long orderId, Long deliveryId);
}

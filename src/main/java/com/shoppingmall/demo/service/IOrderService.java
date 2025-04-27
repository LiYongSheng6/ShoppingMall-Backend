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

    Result<OrderVO> getOrderById(Long id);

    Result saveOrder(OrderSaveDTO orderSaveDTO);

    Result updateToBeShipping(Long id);

    Result updateToBeReceiving(Long id);

    Result updateToBeCompleted(Long id);

    Result updateToBeCancelled(Long id);

    Result updateDelivery(Long id, Long deliveryId);

    Result updateOrder(OrderUpdateDTO orderUpdateDTO, OrderStatus type);

    Result getMyOrderListPage(OrderQuery orderQuery);

    Result getClientOrderListPage(OrderQuery orderQuery);

    Result pageOrderListByUserId(OrderQuery orderQuery);

    Result deleteOrderById(Long id);

    Result deleteOrderBatch(OrderDeleteBatchDTO deleteBatchDTO);
}

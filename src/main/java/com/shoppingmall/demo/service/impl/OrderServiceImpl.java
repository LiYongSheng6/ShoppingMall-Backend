package com.shoppingmall.demo.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.shoppingmall.demo.constant.CacheConstants;
import com.shoppingmall.demo.constant.HttpStatus;
import com.shoppingmall.demo.constant.MessageConstants;
import com.shoppingmall.demo.enums.OrderStatus;
import com.shoppingmall.demo.exception.ServiceException;
import com.shoppingmall.demo.mapper.OrderMapper;
import com.shoppingmall.demo.model.DO.GoodDO;
import com.shoppingmall.demo.model.DO.OrderDO;
import com.shoppingmall.demo.model.DO.UserDO;
import com.shoppingmall.demo.model.DTO.OrderDeleteBatchDTO;
import com.shoppingmall.demo.model.DTO.OrderSaveDTO;
import com.shoppingmall.demo.model.DTO.OrderUpdateDTO;
import com.shoppingmall.demo.model.Query.OrderQuery;
import com.shoppingmall.demo.model.VO.DeliveryVO;
import com.shoppingmall.demo.model.VO.GoodVO;
import com.shoppingmall.demo.model.VO.OrderVO;
import com.shoppingmall.demo.model.VO.PageVO;
import com.shoppingmall.demo.service.IDeliveryService;
import com.shoppingmall.demo.service.IGoodService;
import com.shoppingmall.demo.service.IOrderService;
import com.shoppingmall.demo.service.common.LoginInfoService;
import com.shoppingmall.demo.utils.RedisIdWorker;
import com.shoppingmall.demo.utils.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @author redmi k50 ultra
 * * @date 2024/10/10
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl extends ServiceImpl<OrderMapper, OrderDO> implements IOrderService {

    private final RedisIdWorker redisIdWorker;
    private final LoginInfoService loginInfoService;
    private final OrderMapper orderMapper;
    private final RedissonClient redissonClient;
    private final IDeliveryService deliveryService;
    private final IGoodService goodService;

    @Override
    public Result<OrderVO> selectOrderById(Long orderId) {
        OrderDO orderDO = orderMapper.selectById(orderId);
        return orderDO == null ? Result.error(MessageConstants.NO_FOUND_ORDER_ERROR) : Result.success(new OrderVO(orderDO));
    }

    @Override
    public Result saveOrder(OrderSaveDTO orderSaveDTO) {
        Long userId = loginInfoService.getLoginId();
        Long goodId = orderSaveDTO.getGoodId();

        RLock lock = redissonClient.getLock(CacheConstants.ORDER_SAVE_LOCK + JSON.toJSONString(userId));
        boolean isLock = lock.tryLock();

        try {
            if (!isLock) throw new ServiceException(MessageConstants.OPERATION_ERROR);

            GoodDO goodDO = Db.lambdaQuery(GoodDO.class).eq(GoodDO::getId, goodId).one();
            if (goodDO == null) throw new ServiceException(MessageConstants.NO_FOUND_GOOD_ERROR);

            Integer purchaseNum = orderSaveDTO.getPurchaseNum();
            if (purchaseNum > goodDO.getStockNum()) throw new ServiceException(MessageConstants.NO_ENOUGH_GOOD_ERROR);

            OrderDO orderDO = BeanUtil.copyProperties(orderSaveDTO, OrderDO.class);

            orderDO.setUserId(userId)
                    .setId(redisIdWorker.nextId(CacheConstants.ORDER_ID_PREFIX))
                    .setCreateTime(LocalDateTime.now())
                    .setUpdateTime(LocalDateTime.now());

            boolean isSuccess = Db.lambdaUpdate(GoodDO.class).set(GoodDO::getStockNum, goodDO.getStockNum() - purchaseNum).eq(GoodDO::getId, goodId).update();
            if (!isSuccess) throw new ServiceException(MessageConstants.OPERATION_ERROR);
            return orderMapper.insert(orderDO) > 0 ? Result.success(MessageConstants.OPERATION_SUCCESS) : Result.error(MessageConstants.OPERATION_ERROR);
        } finally {
            if (isLock) { // 是否还是锁定状态
                if (lock.isHeldByCurrentThread()) { // 是否是当前执行线程的锁
                    lock.unlock(); // 释放锁
                }
            }
        }
    }


    @Override
    public Result updateToBeShipping(OrderUpdateDTO orderUpdateDTO) {
        return updateOrder(orderUpdateDTO.setStatus(OrderStatus.Shipping), OrderStatus.PAYING);
    }

    @Override
    public Result updateToBeCompleted(OrderUpdateDTO orderUpdateDTO) {
        return updateOrder(orderUpdateDTO.setStatus(OrderStatus.COMPLETED), OrderStatus.Receiving);
    }

    @Override
    public Result updateToBeCancelled(OrderUpdateDTO orderUpdateDTO) {
        return updateOrder(orderUpdateDTO.setStatus(OrderStatus.CANCELLED), OrderStatus.CANCELLED);
    }


    @Override
    public Result updateOrder(OrderUpdateDTO orderUpdateDTO, OrderStatus originType) {
        Optional<OrderDO> optionalOrderDO = Optional.ofNullable(getById(orderUpdateDTO.getId()));
        optionalOrderDO.ifPresentOrElse(orderDO -> {
             OrderStatus orderDOStatus = orderDO.getStatus();
            if (originType.equals(OrderStatus.CANCELLED)) {
                if (OrderStatus.COMPLETED.equals(orderDOStatus) || OrderStatus.CANCELLED.equals(orderDOStatus)) {
                    throw new ServiceException(MessageConstants.OPERATION_ERROR);
                }
            } else if (!originType.equals(orderDOStatus)) throw new ServiceException(MessageConstants.OPERATION_ERROR);

            Long userId = loginInfoService.getLoginId();
            //查询是否为创建者
            if (!orderDO.getUserId().equals(userId)) {
                throw new ServiceException(HttpStatus.ACCESS_RESTRICTED, MessageConstants.NO_PERMISSION_ERROR);
            }
            //更新订单信息
            OrderDO OrderDO = BeanUtil.copyProperties(orderUpdateDTO, OrderDO.class).setUpdateTime(LocalDateTime.now());
            updateById(OrderDO);
        }, () -> {
            throw new ServiceException(MessageConstants.NO_FOUND_ORDER_ERROR);
        });
        return Result.success(MessageConstants.OPERATION_SUCCESS);
    }

    @Override
    public Result deleteOrderById(Long orderId) {
        Optional.ofNullable(getById(orderId)).ifPresentOrElse(OrderDO -> {
            Long userId = loginInfoService.getLoginId();
            //查询是否为创建者
            if (!OrderDO.getUserId().equals(userId)) {
                throw new ServiceException(HttpStatus.ACCESS_RESTRICTED, MessageConstants.NO_PERMISSION_ERROR);
            }
            removeById(orderId);
        }, () -> {
            throw new ServiceException(MessageConstants.NO_FOUND_ORDER_ERROR);
        });
        return Result.success(MessageConstants.DELETE_SUCCESS);
    }

    @Override
    public Result deleteOrderBatch(OrderDeleteBatchDTO deleteBatchDTO) {
        List<OrderDO> list = lambdaQuery().in(OrderDO::getId, deleteBatchDTO.getOrderIds()).list();
        if (CollectionUtils.isEmpty(list))
            throw new ServiceException(MessageConstants.NO_FOUND_ORDER_ERROR);

        return Db.removeByIds(list, OrderDO.class) ?
                Result.success(MessageConstants.OPERATION_SUCCESS) : Result.error(MessageConstants.OPERATION_ERROR);
    }

    @Override
    public Result pageOrderListByUserId(OrderQuery orderQuery) {
        Long userId = orderQuery.getUserId();
        Optional<UserDO> userDoOptional = Optional.ofNullable(Db.getById(userId, UserDO.class));
        Page<OrderDO> page = orderQuery.toMpPageDefaultSortByUpdateTime();

        if (userDoOptional.isEmpty()) {
            return Result.error(MessageConstants.NO_FOUND_ORDER_ERROR);
        }

        userDoOptional.ifPresent(aLong -> log.info("userId:{}", aLong));

        Page<OrderDO> pageDO = lambdaQuery().eq(OrderDO::getUserId, userId)
                .eq(orderQuery.getStatus() != null, OrderDO::getStatus, orderQuery.getStatus()).page(page);

        if (CollectionUtils.isEmpty(pageDO.getRecords())) {
            return Result.error(MessageConstants.NO_FOUND_ORDER_ERROR);
        }

        return Result.success(PageVO.of(pageDO, OrderDO -> {
            GoodVO goodVO = (GoodVO) goodService.getGoodById(OrderDO.getGoodId()).getData();
            DeliveryVO deliveryVO = (DeliveryVO) deliveryService.getDeliveryById(OrderDO.getDeliveryId()).getData();
            return BeanUtil.copyProperties(OrderDO, OrderVO.class)
                    .setUsername(userDoOptional.get().getUsername())
                    .setGoodName(goodVO.getGoodName()).setPrice(goodVO.getPrice())
                    .setConsigneeName(deliveryVO.getConsigneeName()).setPhone(deliveryVO.getPhone())
                    .setProvince(deliveryVO.getProvince()).setCity(deliveryVO.getCity()).setCounty(deliveryVO.getCounty())
                    .setDetailAddress(deliveryVO.getAddress());
        }));
    }


}

package com.shoppingmall.demo.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.shoppingmall.demo.constant.CacheConstants;
import com.shoppingmall.demo.constant.HttpStatus;
import com.shoppingmall.demo.constant.MessageConstants;
import com.shoppingmall.demo.enums.GoodStatus;
import com.shoppingmall.demo.enums.OrderStatus;
import com.shoppingmall.demo.exception.ServiceException;
import com.shoppingmall.demo.mapper.OrderMapper;
import com.shoppingmall.demo.model.DO.DeliveryDO;
import com.shoppingmall.demo.model.DO.GoodDO;
import com.shoppingmall.demo.model.DO.OrderDO;
import com.shoppingmall.demo.model.DO.UserDO;
import com.shoppingmall.demo.model.DTO.OrderDeleteBatchDTO;
import com.shoppingmall.demo.model.DTO.OrderSaveDTO;
import com.shoppingmall.demo.model.DTO.OrderUpdateDTO;
import com.shoppingmall.demo.model.Query.OrderQuery;
import com.shoppingmall.demo.model.VO.OrderVO;
import com.shoppingmall.demo.model.VO.PageVO;
import com.shoppingmall.demo.service.IAddressService;
import com.shoppingmall.demo.service.IDeliveryService;
import com.shoppingmall.demo.service.IGoodService;
import com.shoppingmall.demo.service.IOrderService;
import com.shoppingmall.demo.service.IUserService;
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
    private final RedissonClient redissonClient;
    private final IDeliveryService deliveryService;
    private final IGoodService goodService;
    private final IUserService userService;
    private final IAddressService addressService;

    @Override
    public Result saveOrder(OrderSaveDTO orderSaveDTO) {
        Long userId = loginInfoService.getLoginId();
        Long goodId = orderSaveDTO.getGoodId();
        Long deliveryId = orderSaveDTO.getDeliveryId();

        RLock lock = redissonClient.getLock(CacheConstants.ORDER_SAVE_LOCK + goodId);
        boolean isLock = lock.tryLock();

        try {
            if (!isLock) throw new ServiceException(MessageConstants.GET_LOCK_ERROR);

            GoodDO goodDO = Db.lambdaQuery(GoodDO.class).eq(GoodDO::getId, goodId).one();
            if (goodDO == null) throw new ServiceException(MessageConstants.NO_FOUND_GOOD_ERROR);

            if (userId.equals((goodDO.getCategoryId()))) {
                throw new ServiceException(MessageConstants.PURCHASE_OWN_ERROR);
            }
            if (GoodStatus.REMOVE.equals(goodDO.getStatus())) {
                throw new ServiceException(MessageConstants.GOOD_REMOVE_ERROR);
            }

            Integer purchaseNum = orderSaveDTO.getPurchaseNum();
            if (purchaseNum > goodDO.getStockNum()) throw new ServiceException(MessageConstants.NO_ENOUGH_GOOD_ERROR);

            DeliveryDO deliveryDO = Db.lambdaQuery(DeliveryDO.class).eq(DeliveryDO::getId, deliveryId).eq(DeliveryDO::getId, deliveryId).one();
            if (deliveryDO == null) throw new ServiceException(MessageConstants.NO_FOUND_DELIVERY_ERROR);

            OrderDO orderDO = BeanUtil.copyProperties(orderSaveDTO, OrderDO.class)
                    .setId(redisIdWorker.nextId(CacheConstants.ORDER_ID_PREFIX))
                    .setUserId((userId))
                    .setGoodName(goodDO.getGoodName())
                    .setCoverUrl(goodDO.getCoverUrl())
                    .setPrice(goodDO.getPrice())
                    .setTotal(purchaseNum * goodDO.getPrice())
                    .setConsigneeName(deliveryDO.getConsigneeName())
                    .setPhone(deliveryDO.getPhone())
                    .setProvince(addressService.getAddressNameById(deliveryDO.getProvinceId()))
                    .setCity(addressService.getAddressNameById(deliveryDO.getCityId()))
                    .setCounty(addressService.getAddressNameById(deliveryDO.getCountyId()))
                    .setAddress(deliveryDO.getAddress())
                    .setStatus(OrderStatus.PAYING)
                    .setCreateTime(LocalDateTime.now())
                    .setUpdateTime(LocalDateTime.now());

            boolean isSuccess = Db.lambdaUpdate(GoodDO.class)
                    .setSql("stock_num = stock_num - " + purchaseNum)
                    .set(GoodDO::getUpdateTime, LocalDateTime.now())
                    .eq(GoodDO::getId, goodId)
                    .update();
            if (!isSuccess) throw new ServiceException(MessageConstants.UPDATE_ERROR);

            return save(orderDO) ? Result.success(MessageConstants.OPERATION_SUCCESS) : Result.error(MessageConstants.OPERATION_ERROR);
        } finally {
            if (isLock) { // 是否还是锁定状态
                if (lock.isHeldByCurrentThread()) { // 是否是当前执行线程的锁
                    lock.unlock(); // 释放锁
                }
            }
        }
    }

    @Override
    public Result updateToBeShipping(Long id) {
        return updateOrder(new OrderUpdateDTO(id, null, OrderStatus.Shipping), OrderStatus.PAYING);
    }

    @Override
    public Result updateToBeReceiving(Long id) {
        return updateOrder(new OrderUpdateDTO(id, null, OrderStatus.Receiving), OrderStatus.Shipping);
    }

    @Override
    public Result updateToBeCompleted(Long id) {
        return updateOrder(new OrderUpdateDTO(id, null, OrderStatus.COMPLETED), OrderStatus.Receiving);
    }

    @Override
    public Result updateToBeCancelled(Long id) {
        return updateOrder(new OrderUpdateDTO(id, null, OrderStatus.CANCELLED), OrderStatus.CANCELLED);
    }

    @Override
    public Result updateDelivery(Long id, Long deliveryId) {
        OrderDO orderDO = getById(id);
        if (orderDO == null) throw new ServiceException(MessageConstants.NO_FOUND_ORDER_ERROR);
        DeliveryDO deliveryDO = Db.getById(deliveryId, DeliveryDO.class);
        if (deliveryDO == null) throw new ServiceException(MessageConstants.NO_FOUND_DELIVERY_ERROR);

        //查询是否为创建者
        Long userId = loginInfoService.getLoginId();
        if (!orderDO.getUserId().equals(userId)) {
            throw new ServiceException(HttpStatus.ACCESS_RESTRICTED, MessageConstants.PERMISSION_PROHIBITED_ERROR);
        }

        if (!(OrderStatus.PAYING.equals(orderDO.getStatus()) || OrderStatus.Shipping.equals(orderDO.getStatus()))) {
            return Result.error(MessageConstants.OPERATION_ERROR);
        }

        //更新订单信息
        OrderDO OrderDO = orderDO.setDeliveryId((deliveryId)).setUpdateTime(LocalDateTime.now());
        return updateById(OrderDO) ? Result.success(MessageConstants.UPDATE_SUCCESS) : Result.error(MessageConstants.UPDATE_ERROR);
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
                throw new ServiceException(HttpStatus.ACCESS_RESTRICTED, MessageConstants.PERMISSION_PROHIBITED_ERROR);
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
    public Result deleteOrderById(Long id) {
        Optional.ofNullable(getById(id)).ifPresentOrElse(OrderDO -> {
            Long userId = loginInfoService.getLoginId();
            //查询是否为创建者
            if (!OrderDO.getUserId().equals(userId)) {
                throw new ServiceException(HttpStatus.ACCESS_RESTRICTED, MessageConstants.PERMISSION_PROHIBITED_ERROR);
            }
            removeById(id);
        }, () -> {
            throw new ServiceException(MessageConstants.NO_FOUND_ORDER_ERROR);
        });
        return Result.success(MessageConstants.DELETE_SUCCESS);
    }

    @Override
    public Result deleteOrderBatch(OrderDeleteBatchDTO deleteBatchDTO) {
        List<OrderDO> list = lambdaQuery().in(OrderDO::getId, deleteBatchDTO.getOrderIds())
                .eq(OrderDO::getUserId, loginInfoService.getLoginId()).list();
        if (CollectionUtils.isEmpty(list))
            throw new ServiceException(MessageConstants.NO_FOUND_ORDER_ERROR);

        return Db.removeByIds(list, OrderDO.class) ?
                Result.success(MessageConstants.OPERATION_SUCCESS) : Result.error(MessageConstants.OPERATION_ERROR);
    }

    @Override
    public Result<OrderVO> getOrderById(Long id) {
        OrderDO orderDO = getById(id);
        if (orderDO == null) throw new ServiceException(MessageConstants.NO_FOUND_ORDER_ERROR);
        loginInfoService.CheckLoginUserObject((orderDO.getUserId()));
        return Result.success(new OrderVO(orderDO).setUsername(userService.getUserNameById((orderDO.getUserId()))));
    }

    @Override
    public Result getMyOrderListPage(OrderQuery orderQuery) {
        orderQuery.setUserId(loginInfoService.getLoginId());
        return pageOrderListByUserId(orderQuery);
    }

    @Override
    public Result pageOrderListByUserId(OrderQuery orderQuery) {
        Long userId = orderQuery.getUserId();
        Optional<UserDO> userDoOptional = Optional.ofNullable(Db.getById(userId, UserDO.class));
        if (userDoOptional.isEmpty()) return Result.error(MessageConstants.NO_FOUND_ORDER_ERROR);
        Page<OrderDO> page = orderQuery.toMpPageDefaultSortByUpdateTime();

        userDoOptional.ifPresent(aLong -> log.info("userId:{}", aLong));

        Page<OrderDO> pageDO = lambdaQuery().eq(OrderDO::getUserId, userId)
                .eq(orderQuery.getStatus() != null, OrderDO::getStatus, orderQuery.getStatus()).page(page);

        if (CollectionUtils.isEmpty(pageDO.getRecords())) return Result.error(MessageConstants.NO_FOUND_ORDER_ERROR);

        return Result.success(PageVO.of(pageDO, orderDO -> new OrderVO(orderDO).setUsername(userDoOptional.get().getUsername())));
    }


}

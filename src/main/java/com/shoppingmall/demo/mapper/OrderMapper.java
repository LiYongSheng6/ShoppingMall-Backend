package com.shoppingmall.demo.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shoppingmall.demo.model.DO.OrderDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author redmi k50 ultra
 * * @date 2024/10/10
 */
@Mapper
public interface OrderMapper extends BaseMapper<OrderDO> {

}

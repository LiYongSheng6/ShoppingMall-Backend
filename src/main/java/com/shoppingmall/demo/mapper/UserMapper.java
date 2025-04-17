package com.shoppingmall.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shoppingmall.demo.model.DO.UserDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author redmi k50 ultra
 * * @date 2024/10/11
 */
@Mapper
public interface UserMapper extends BaseMapper<UserDO> {
}

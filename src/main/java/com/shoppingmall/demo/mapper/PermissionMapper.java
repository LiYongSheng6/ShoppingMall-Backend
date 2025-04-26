package com.shoppingmall.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shoppingmall.demo.model.DO.PermissionDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author redmi k50 ultra
 * * @date 2024/8/3
 */
@Mapper
public interface PermissionMapper extends BaseMapper<PermissionDO> {
    /**
     * 根据用户id查询权限菜单列表
     *
     * @param userId
     * @return
     */
    List<PermissionDO> getPermissionListByUserId(Long userId);

    /**
     * 根据用户id查询权限菜单列表
     *
     * @param userId
     * @return
     */
    List<String> getPermissionCodeListByUserId(Long userId);

}

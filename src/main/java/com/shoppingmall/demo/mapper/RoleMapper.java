package com.shoppingmall.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shoppingmall.demo.model.DO.RoleDO;
import com.shoppingmall.demo.model.DO.UserRoleDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author redmi k50 ultra
 * * @date 2024/8/3
 */
@Mapper
public interface RoleMapper extends BaseMapper<RoleDO> {

    /**
     * 根据用户id查询拥有角色列表
     *
     * @param userId
     * @return
     */
    List<RoleDO> getRoleListByUserId(Long userId);

    /**
     * 根据用户id和用户类型查询用户角色关系DO
     *
     * @param userId
     * @param userType
     * @return
     */
    UserRoleDO getUserRoleByUserIdAndUserType(Long userId, String userType);

}

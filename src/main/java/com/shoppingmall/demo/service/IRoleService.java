package com.shoppingmall.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shoppingmall.demo.model.DO.RoleDO;
import com.shoppingmall.demo.model.DTO.RoleDeleteBatchDTO;
import com.shoppingmall.demo.model.DTO.RoleSaveDTO;
import com.shoppingmall.demo.model.DTO.RoleUpdateDTO;
import com.shoppingmall.demo.model.DTO.UserRoleDTO;
import com.shoppingmall.demo.utils.Result;


/**
 * @author redmi k50 ultra
 * * @date 2024/10/10
 */
public interface IRoleService extends IService<RoleDO> {
    Result saveRole(RoleSaveDTO RoleSaveDTO);

    Result updateRole(RoleUpdateDTO RoleUpdateDTO);

    Result deleteRole(Long id);

    Result deleteRoleBatch(RoleDeleteBatchDTO deleteBatchDTO);

    String getRoleNameById(Long id);

    Result getRoleById(Long id);

    Result getRoleListByUserId(Long userId);

    Result getRoleIdListByUserId(Long userId);

    Result getHaveSignRoleList(Long userId);

    Result getAllRoleList();

    Result getRoleTree();

    Result assignRoleToUser(UserRoleDTO userRoleDTO);


}

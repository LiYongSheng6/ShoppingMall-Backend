package com.shoppingmall.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shoppingmall.demo.model.DO.PermissionDO;
import com.shoppingmall.demo.model.DTO.PermissionDeleteBatchDTO;
import com.shoppingmall.demo.model.DTO.PermissionSaveDTO;
import com.shoppingmall.demo.model.DTO.PermissionUpdateDTO;
import com.shoppingmall.demo.utils.Result;


/**
 * @author redmi k50 ultra
 * * @date 2024/10/10
 */
public interface IPermissionService extends IService<PermissionDO> {
    Result savePermission(PermissionSaveDTO permissionSaveDTO);

    Result updatePermission(PermissionUpdateDTO permissionUpdateDTO);

    Result deletePermission(Long id);

    Result deletePermissionBatch(PermissionDeleteBatchDTO deleteBatchDTO);

    Result getPermissionById(Long id);

    Result getPermissionIdListByUserId(Long roleId, Integer type);

    Result getHaveSignPermissionList(Long roleId, Integer type);

    Result getAllPermissionList(Integer type);

    Result getPermissionTree();

    Result getPermissionListByUserId(Long userId);

    Result getPermissionCodeListByUserId(Long userId);


}

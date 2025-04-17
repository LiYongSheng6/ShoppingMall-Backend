package com.shoppingmall.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shoppingmall.demo.model.DO.PermissionDO;
import com.shoppingmall.demo.model.DTO.PermissionSaveDTO;
import com.shoppingmall.demo.model.DTO.PermissionUpdateDTO;
import com.shoppingmall.demo.utils.Result;


/**
 * @author redmi k50 ultra
 * * @date 2024/10/10
 */
public interface IPermissionService extends IService<PermissionDO> {
    Result addPermission(PermissionSaveDTO permissionSaveDTO);

    Result getPermissionList(Integer type);

    Result updatePermission(PermissionUpdateDTO permissionUpdateDTO);

    Result deletePermission(Long id);

}

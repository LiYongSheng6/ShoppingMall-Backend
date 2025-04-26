package com.shoppingmall.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shoppingmall.demo.model.DO.AuthenticationDO;
import com.shoppingmall.demo.model.DTO.AuthenticationBatchDTO;
import com.shoppingmall.demo.utils.Result;

/**
 * @author redmi k50 ultra
 * * @date 2024/10/10
 */
public interface IAuthenticationService extends IService<AuthenticationDO> {

    Result saveOrUpdateAuthenticationBatch(AuthenticationBatchDTO batchDTO);

    Result deleteAuthenticationBatch(AuthenticationBatchDTO deleteBatchDTO);

    Result applyAuthentication(String studentId, String realName);

    Result cancelAuthentication(String studentId);


}

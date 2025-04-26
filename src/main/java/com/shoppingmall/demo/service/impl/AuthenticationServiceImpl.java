package com.shoppingmall.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.shoppingmall.demo.constant.CacheConstants;
import com.shoppingmall.demo.constant.MessageConstants;
import com.shoppingmall.demo.enums.UserType;
import com.shoppingmall.demo.exception.ServiceException;
import com.shoppingmall.demo.mapper.AuthenticationMapper;
import com.shoppingmall.demo.model.DO.AuthenticationDO;
import com.shoppingmall.demo.model.DO.UserDO;
import com.shoppingmall.demo.model.DTO.AuthenticationBatchDTO;
import com.shoppingmall.demo.service.IAuthenticationService;
import com.shoppingmall.demo.service.common.LoginInfoService;
import com.shoppingmall.demo.utils.RedisIdWorker;
import com.shoppingmall.demo.utils.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * @author redmi k50 ultra
 * * @date 2024/10/10
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl extends ServiceImpl<AuthenticationMapper, AuthenticationDO> implements IAuthenticationService {

    private final RedisIdWorker redisIdWorker;
    private final LoginInfoService loginInfoService;

    @Override
    public Result saveOrUpdateAuthenticationBatch(AuthenticationBatchDTO batchDTO) {
        Map<String, String> map = batchDTO.getMap();
        if (MapUtils.isEmpty(map))
            throw new ServiceException(MessageConstants.NO_FOUND_STUDENT_ID_NAME_MAP_ERROR);

        List<AuthenticationDO> authenticationDOList = map.entrySet().stream().map(item -> {
            AuthenticationDO authenticationDO = lambdaQuery().eq(AuthenticationDO::getStudentId, item.getKey()).one();
            if (authenticationDO == null)
                authenticationDO = new AuthenticationDO().setId(redisIdWorker.nextId(CacheConstants.ADDRESS_ID_PREFIX));

            return authenticationDO.setRealName(item.getValue()).setUpdateTime(LocalDateTime.now());
        }).toList();

        return Db.saveOrUpdateBatch(authenticationDOList, authenticationDOList.size()) ?
                Result.success(MessageConstants.OPERATION_SUCCESS) : Result.error(MessageConstants.OPERATION_ERROR);
    }

    @Override
    public Result deleteAuthenticationBatch(AuthenticationBatchDTO deleteBatchDTO) {
        if (MapUtils.isEmpty(deleteBatchDTO.getMap()))
            throw new ServiceException(MessageConstants.NO_FOUND_STUDENT_ID_NAME_MAP_ERROR);

        List<AuthenticationDO> authenticationDOList = deleteBatchDTO.getMap().keySet()
                .stream().map(studentId -> lambdaQuery().eq(AuthenticationDO::getStudentId, studentId).one()).toList();

        return Db.removeByIds(authenticationDOList, AuthenticationDO.class) ?
                Result.success(MessageConstants.OPERATION_SUCCESS) : Result.error(MessageConstants.OPERATION_ERROR);
    }

    @Override
    @Transactional(rollbackFor = {ServiceException.class, Exception.class})
    public Result applyAuthentication(String studentId, String realName) {
        AuthenticationDO authDo = lambdaQuery().eq(AuthenticationDO::getStudentId, studentId).one();

        if (authDo == null)
            throw new ServiceException(MessageConstants.NO_FOUND_AUTHENTICATION_ERROR);

        if (!Objects.equals(authDo.getRealName(), realName))
            throw new ServiceException(MessageConstants.AUTHENTICATION_MATCH_ERROR);

        if (!Objects.equals(authDo.getUserId(), 0L))
            throw new ServiceException(MessageConstants.AUTHENTICATION_APPLIED_EXIST);

        Long userId = loginInfoService.getLoginId();
        if (!Db.lambdaUpdate(UserDO.class).set(UserDO::getType, UserType.MERCHANT)
                .set(UserDO::getUpdateTime, LocalDateTime.now())
                .eq(UserDO::getId, userId).eq(UserDO::getType, UserType.USER).update()) {
            throw new ServiceException(MessageConstants.UPDATE_ERROR);
        }

        return updateById(authDo.setUserId(userId).setUpdateTime(LocalDateTime.now())) ?
                Result.success(MessageConstants.OPERATION_SUCCESS) : Result.error(MessageConstants.OPERATION_ERROR);
    }

    @Override
    @Transactional(rollbackFor = {ServiceException.class, Exception.class})
    public Result cancelAuthentication(String studentId) {
        AuthenticationDO authDo = lambdaQuery().eq(AuthenticationDO::getStudentId, studentId).one();

        if (authDo == null)
            throw new ServiceException(MessageConstants.NO_FOUND_AUTHENTICATION_ERROR);

        if (!Objects.equals(authDo.getUserId(), loginInfoService.getLoginId()))
            throw new ServiceException(MessageConstants.PERMISSION_PROHIBITED_ERROR);

        Long userId = loginInfoService.getLoginId();
        if (!Db.lambdaUpdate(UserDO.class).set(UserDO::getType, UserType.USER)
                .set(UserDO::getUpdateTime, LocalDateTime.now())
                .eq(UserDO::getId, userId).eq(UserDO::getType, UserType.MERCHANT).update()) {
            throw new ServiceException(MessageConstants.UPDATE_ERROR);
        }

        return updateById(authDo.setUserId(0L).setUpdateTime(LocalDateTime.now())) ?
                Result.success(MessageConstants.OPERATION_SUCCESS) : Result.error(MessageConstants.OPERATION_ERROR);
    }


}
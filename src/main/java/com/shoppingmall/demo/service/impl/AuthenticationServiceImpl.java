package com.shoppingmall.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.shoppingmall.demo.constant.CacheConstants;
import com.shoppingmall.demo.constant.MessageConstants;
import com.shoppingmall.demo.enums.UserType;
import com.shoppingmall.demo.exception.ServiceException;
import com.shoppingmall.demo.mapper.AuthenticationMapper;
import com.shoppingmall.demo.mapper.RoleMapper;
import com.shoppingmall.demo.model.DO.AuthenticationDO;
import com.shoppingmall.demo.model.DO.RoleDO;
import com.shoppingmall.demo.model.DO.UserDO;
import com.shoppingmall.demo.model.DO.UserRoleDO;
import com.shoppingmall.demo.model.DTO.AuthenticationBatchDTO;
import com.shoppingmall.demo.service.IAuthenticationService;
import com.shoppingmall.demo.service.IUserService;
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
import java.util.Optional;


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
    private final RoleMapper roleMapper;
    private final IUserService userService;

    @Override
    public Result saveOrUpdateAuthenticationBatch(AuthenticationBatchDTO batchDTO) {
        Map<String, String> map = batchDTO.getMap();
        if (MapUtils.isEmpty(map))
            throw new ServiceException(MessageConstants.NO_FOUND_STUDENT_ID_NAME_MAP_ERROR);

        List<AuthenticationDO> authenticationDOList = map.entrySet().stream().map(item -> {
            AuthenticationDO authenticationDO = lambdaQuery().eq(AuthenticationDO::getStudentId, item.getKey()).one();
            if (authenticationDO == null)
                authenticationDO = new AuthenticationDO().setStudentId(item.getKey())
                        .setId(redisIdWorker.nextId(CacheConstants.ADDRESS_ID_PREFIX));

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
        // 查询认证信息
        AuthenticationDO authDo = lambdaQuery().eq(AuthenticationDO::getStudentId, studentId).one();
        if (authDo == null)
            throw new ServiceException(MessageConstants.NO_FOUND_AUTHENTICATION_ERROR);
        // 判断真实姓名是否匹配
        if (!Objects.equals(authDo.getRealName(), realName))
            throw new ServiceException(MessageConstants.AUTHENTICATION_MATCH_ERROR);
        // 判断用户是否已经申请过认证
        if (!Objects.equals(authDo.getUserId(), 0L))
            throw new ServiceException(MessageConstants.AUTHENTICATION_APPLIED_EXIST);
        // 判断实名信息是否已经被占用
        Long userId = loginInfoService.getLoginId();
        Optional.ofNullable(userService.getUserByStudentId(studentId)).ifPresent(userDO -> {
            throw new ServiceException(MessageConstants.STUDENT_ID_EXIST);
        });
        // 更新用户认证信息
        if (!Db.lambdaUpdate(UserDO.class)
                .set(Objects.equals(UserType.USER, userService.getById(userId).getType()), UserDO::getType, UserType.MERCHANT)
                .set(UserDO::getStudentId, studentId)
                .set(UserDO::getUpdateTime, LocalDateTime.now())
                .eq(UserDO::getId, userId).update()) {
            throw new ServiceException(MessageConstants.UPDATE_ERROR);
        }
        // 更新用户角色信息
        Long roleId = Db.lambdaQuery(RoleDO.class).eq(RoleDO::getCode, UserType.MERCHANT.getDesc()).one().getId();
        UserRoleDO userRoleDO = Db.lambdaQuery(UserRoleDO.class).eq(UserRoleDO::getUserId, userId).eq(UserRoleDO::getRoleId, roleId).one();
        if (userRoleDO != null) Db.saveOrUpdate(userRoleDO.setUpdateTime(LocalDateTime.now()));
        else
            Db.save(new UserRoleDO().setId(redisIdWorker.nextId(CacheConstants.USER_ROLE_ID)).setUserId(userId).setRoleId(roleId));
        // 更新认证信息
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
        if (!Db.lambdaUpdate(UserDO.class)
                .set(Objects.equals(UserType.MERCHANT, userService.getById(userId).getType()), UserDO::getType, UserType.USER)
                .set(UserDO::getStudentId, null)
                .set(UserDO::getUpdateTime, LocalDateTime.now())
                .eq(UserDO::getId, userId).update()) {
            throw new ServiceException(MessageConstants.UPDATE_ERROR);
        }

        UserRoleDO userRoleDO = roleMapper.getUserRoleByUserIdAndUserType(userId, UserType.MERCHANT.getDesc());
        if (userRoleDO != null) Db.removeById(userRoleDO);

        return updateById(authDo.setUserId(0L).setUpdateTime(LocalDateTime.now())) ?
                Result.success(MessageConstants.OPERATION_SUCCESS) : Result.error(MessageConstants.OPERATION_ERROR);
    }


}
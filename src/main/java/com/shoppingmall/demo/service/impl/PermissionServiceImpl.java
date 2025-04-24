package com.shoppingmall.demo.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.shoppingmall.demo.constant.CacheConstants;
import com.shoppingmall.demo.constant.MessageConstants;
import com.shoppingmall.demo.enums.UserType;
import com.shoppingmall.demo.exception.ServiceException;
import com.shoppingmall.demo.mapper.PermissionMapper;
import com.shoppingmall.demo.model.DO.PermissionDO;
import com.shoppingmall.demo.model.DTO.PermissionDeleteBatchDTO;
import com.shoppingmall.demo.model.DTO.PermissionSaveDTO;
import com.shoppingmall.demo.model.DTO.PermissionUpdateDTO;
import com.shoppingmall.demo.model.VO.PermissionVO;
import com.shoppingmall.demo.service.IPermissionService;
import com.shoppingmall.demo.utils.RedisIdWorker;
import com.shoppingmall.demo.utils.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * @author redmi k50 ultra
 * * @date 2024/10/10
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, PermissionDO> implements IPermissionService {

    private final RedisIdWorker redisIdWorker;

    @Override
    public Result savePermission(PermissionSaveDTO permissionSaveDTO) {
        checkDuplicationColumn(null, permissionSaveDTO.getCode(), permissionSaveDTO.getPath());
        return save(BeanUtil.copyProperties(permissionSaveDTO, PermissionDO.class).setId(redisIdWorker.nextId(CacheConstants.PERMISSION_ID))) ?
                Result.success(MessageConstants.SAVE_SUCCESS) : Result.error(MessageConstants.SAVE_ERROR);
    }

    @Override
    public Result updatePermission(PermissionUpdateDTO permissionUpdateDTO) {
        checkDuplicationColumn(permissionUpdateDTO.getId(), permissionUpdateDTO.getCode(), permissionUpdateDTO.getPath());
        return updateById(BeanUtil.copyProperties(permissionUpdateDTO, PermissionDO.class).setUpdateTime(LocalDateTime.now())) ?
                Result.success(MessageConstants.UPDATE_SUCCESS) : Result.error(MessageConstants.UPDATE_ERROR);
    }

    private void checkDuplicationColumn(Long id, String code, String path) {
        Optional.ofNullable(lambdaQuery().ne(id != null, PermissionDO::getId, id).eq(PermissionDO::getCode, code).one()).ifPresent(permissionDO -> {
            throw new ServiceException(MessageConstants.PERMISSION_CODE_EXIST);
        });
        Optional.ofNullable(lambdaQuery().ne(id != null, PermissionDO::getId, id).eq(PermissionDO::getPath, path).one()).ifPresent(permissionDO -> {
            throw new ServiceException(MessageConstants.PERMISSION_PATH_EXIST);
        });
    }

    @Override
    public Result getPermissionById(Long id) {
        PermissionDO permissionDO = getById(id);
        Optional.ofNullable(permissionDO).orElseThrow(() -> new ServiceException(MessageConstants.NO_FOUND_PERMISSION_ERROR));
        return Result.success(BeanUtil.copyProperties(permissionDO, PermissionVO.class));
    }

    @Override
    public Result getPermissionList(Integer type) {
        List<PermissionDO> permissionDOList;

        if (UserType.ADMIN.getValue().equals(type)) permissionDOList = lambdaQuery().list();
        else permissionDOList = lambdaQuery().eq(PermissionDO::getType, type).list();

        if (CollectionUtils.isEmpty(permissionDOList)) throw new ServiceException(MessageConstants.NO_FOUND_PERMISSION_ERROR);

        return Result.success(permissionDOList
                .stream().map(permissionDO -> CompletableFuture.supplyAsync(() -> new PermissionVO(permissionDO))).toList()
                .stream().map(CompletableFuture::join).toList());
    }

    @Override
    public Result deletePermission(Long id) {
        return removeById(id) ? Result.success(MessageConstants.DELETE_SUCCESS) : Result.error(MessageConstants.DELETE_ERROR);
    }

    @Override
    public Result deletePermissionBatch(PermissionDeleteBatchDTO deleteBatchDTO) {
        List<PermissionDO> list = lambdaQuery().in(PermissionDO::getId, deleteBatchDTO.getPermissionIds()).list();
        if (CollectionUtils.isEmpty(list))
            throw new ServiceException(MessageConstants.NO_FOUND_PERMISSION_ERROR);

        return Db.removeByIds(list, PermissionDO.class) ?
                Result.success(MessageConstants.OPERATION_SUCCESS) : Result.error(MessageConstants.OPERATION_ERROR);
    }

}
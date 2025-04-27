package com.shoppingmall.demo.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.shoppingmall.demo.constant.CacheConstants;
import com.shoppingmall.demo.constant.MessageConstants;
import com.shoppingmall.demo.exception.ServiceException;
import com.shoppingmall.demo.mapper.PermissionMapper;
import com.shoppingmall.demo.model.DO.PermissionDO;
import com.shoppingmall.demo.model.DO.RolePermissionDO;
import com.shoppingmall.demo.model.DTO.PermissionDeleteBatchDTO;
import com.shoppingmall.demo.model.DTO.PermissionSaveDTO;
import com.shoppingmall.demo.model.DTO.PermissionUpdateDTO;
import com.shoppingmall.demo.model.DTO.RolePermissionDTO;
import com.shoppingmall.demo.model.VO.PermissionVO;
import com.shoppingmall.demo.service.IPermissionService;
import com.shoppingmall.demo.utils.RedisIdWorker;
import com.shoppingmall.demo.utils.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private final PermissionMapper permissionMapper;

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

    private static List<PermissionVO> makePermissionTree(List<PermissionVO> permissionVOS, Long pid) {
        //创建集合保存分类数据
        List<PermissionVO> PermissionVOList = new ArrayList<PermissionVO>();
        //判断分类列表是否为空，如果不为空则使用分类列表，否则创建集合对象
        Optional.ofNullable(permissionVOS).orElse(new ArrayList<PermissionVO>())
                .stream().filter(item -> item != null && item.getParentId().equals(pid))
                .forEach(item -> {
                    //获取每一个item对象的子分类，递归生成分类树
                    List<PermissionVO> children = makePermissionTree(permissionVOS, item.getId());
                    //设置子分类
                    item.setChildren(children);
                    //将分类对象添加到集合
                    PermissionVOList.add(item);
                });
        //返回分类信息
        return PermissionVOList;
    }

    @Override
    public Result getPermissionById(Long id) {
        PermissionDO permissionDO = getById(id);
        Optional.ofNullable(permissionDO).orElseThrow(() -> new ServiceException(MessageConstants.NO_FOUND_PERMISSION_ERROR));
        return Result.success(BeanUtil.copyProperties(permissionDO, PermissionVO.class)
                .setParentName(getPermissionNameById(permissionDO.getParentId())));
    }

    private String getPermissionNameById(Long id) {
        PermissionDO permissionDO = getById(id);
        return permissionDO != null ? permissionDO.getName() : "NULL";
    }

    @Override
    public Result<List<Long>> getPermissionIdListByUserId(Long roleId, Integer type) {
        List<Long> havePermissionIds = Db.lambdaQuery(RolePermissionDO.class).eq(roleId != null, RolePermissionDO::getRoleId, roleId).list()
                .stream().map(rolePermissionDO -> CompletableFuture.supplyAsync(rolePermissionDO::getPermissionId)).toList()
                .stream().map(CompletableFuture::join).toList();
        return Result.success(havePermissionIds);
    }

    @Override
    public Result getHaveSignPermissionList(Long roleId, Integer type) {
        List<PermissionDO> permissionDOList = lambdaQuery().eq(type != null, PermissionDO::getType, type).list();
        if (CollectionUtils.isEmpty(permissionDOList))
            throw new ServiceException(MessageConstants.NO_FOUND_PERMISSION_ERROR);

        List<PermissionVO> permissionVOList = permissionDOList
                .stream().map(permissionDO -> CompletableFuture.supplyAsync(() -> new PermissionVO(permissionDO)
                        .setParentName(getPermissionNameById(permissionDO.getParentId())))).toList()
                .stream().map(CompletableFuture::join).toList();

        if (roleId != null) {
            List<Long> havePermissionIds = getPermissionIdListByUserId(roleId, type).getData();
            permissionVOList = permissionVOList.stream().peek(permissionVO -> permissionVO.setIsHave(havePermissionIds.contains(permissionVO.getId()))).toList();
        }
        return Result.success(permissionVOList);
    }

    @Override
    public Result getAllPermissionList(Integer type) {
        return getHaveSignPermissionList(null, type);
    }

    @Override
    public Result getPermissionTree() {
        List<PermissionDO> permissionDOList = lambdaQuery().list();
        if (CollectionUtils.isEmpty(permissionDOList))
            throw new ServiceException(MessageConstants.NO_FOUND_PERMISSION_ERROR);

        List<PermissionVO> permissionVOList = permissionDOList.stream()
                .map(item -> CompletableFuture.supplyAsync(() -> new PermissionVO(item)
                        .setParentName(getPermissionNameById((item.getParentId()))))).toList()
                .stream().map(CompletableFuture::join).toList();
        return Result.success(makePermissionTree(permissionVOList, 0L));
    }

    public Result getPermissionListByUserId(Long userId) {
        return Result.success(permissionMapper.getPermissionListByUserId(userId));
    }

    public Result getPermissionCodeListByUserId(Long userId) {
        return Result.success(permissionMapper.getPermissionCodeListByUserId(userId));
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

    @Override
    @Transactional(rollbackFor = {ServiceException.class, Exception.class})
    public Result assignPermissionToRole(RolePermissionDTO rolePermissionDTO) {
        Long roleId = rolePermissionDTO.getRoleId();
        List<RolePermissionDO> list = Db.lambdaQuery(RolePermissionDO.class).eq(RolePermissionDO::getRoleId, roleId).list();
        if (CollectionUtils.isNotEmpty(list))
            if (!Db.lambdaUpdate(RolePermissionDO.class).eq(RolePermissionDO::getRoleId, roleId).remove())
                throw new ServiceException(MessageConstants.DELETE_ERROR);

        List<Long> permissionIds = rolePermissionDTO.getPermissionIds();
        if (CollectionUtils.isNotEmpty(permissionIds)) {
            List<RolePermissionDO> rolePermissionDOList = permissionIds
                    .stream().map(permissionId -> CompletableFuture.supplyAsync(() -> new RolePermissionDO()
                            .setId(redisIdWorker.nextId(CacheConstants.ROLE_PERMISSION_ID))
                            .setRoleId(roleId).setPermissionId(permissionId))).toList()
                    .stream().map(CompletableFuture::join).toList();

            if (!Db.saveBatch(rolePermissionDOList, rolePermissionDOList.size()))
                throw new ServiceException(MessageConstants.SAVE_ERROR);
        }

        return Result.success(MessageConstants.OPERATION_SUCCESS);
    }



}
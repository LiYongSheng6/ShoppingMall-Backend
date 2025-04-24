package com.shoppingmall.demo.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.shoppingmall.demo.constant.CacheConstants;
import com.shoppingmall.demo.constant.MessageConstants;
import com.shoppingmall.demo.exception.ServiceException;
import com.shoppingmall.demo.mapper.RoleMapper;
import com.shoppingmall.demo.model.DO.RoleDO;
import com.shoppingmall.demo.model.DTO.RoleDeleteBatchDTO;
import com.shoppingmall.demo.model.DTO.RolePermissionDTO;
import com.shoppingmall.demo.model.DTO.RoleSaveDTO;
import com.shoppingmall.demo.model.DTO.RoleUpdateDTO;
import com.shoppingmall.demo.model.VO.PermissionVO;
import com.shoppingmall.demo.model.VO.RoleVO;
import com.shoppingmall.demo.service.IRoleService;
import com.shoppingmall.demo.service.common.LoginInfoService;
import com.shoppingmall.demo.utils.RedisIdWorker;
import com.shoppingmall.demo.utils.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

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
public class RoleServiceImpl extends ServiceImpl<RoleMapper, RoleDO> implements IRoleService {

    private final RedisIdWorker redisIdWorker;
    private final LoginInfoService loginInfoService;

    private static List<RoleVO> makeRoleTree(List<RoleVO> categoryList, Long pid) {
        //创建集合保存分类数据
        List<RoleVO> roleVOList = new ArrayList<RoleVO>();
        //判断分类列表是否为空，如果不为空则使用分类列表，否则创建集合对象
        Optional.ofNullable(categoryList).orElse(new ArrayList<RoleVO>())
                .stream().filter(item -> item != null && item.getParentId().equals(pid))
                .forEach(item -> {
                    //获取每一个item对象的子分类，递归生成分类树
                    List<RoleVO> children = makeRoleTree(categoryList, item.getId());
                    //设置子分类
                    item.setChildren(children);
                    //将分类对象添加到集合
                    roleVOList.add(item);
                });
        //返回分类信息
        return roleVOList;
    }

    @Override
    public Result saveRole(RoleSaveDTO saveRoleDTO) {
        checkDuplicationColumn(null, saveRoleDTO.getCode());
        return save(BeanUtil.copyProperties(saveRoleDTO, RoleDO.class)
                .setId(redisIdWorker.nextId(CacheConstants.ROLE_ID))
                .setCreatorId(loginInfoService.getLoginId())) ?
                Result.success(MessageConstants.SAVE_SUCCESS) : Result.error(MessageConstants.SAVE_ERROR);
    }

    @Override
    public Result updateRole(RoleUpdateDTO RoleUpdateDTO) {
        checkDuplicationColumn(RoleUpdateDTO.getId(), RoleUpdateDTO.getCode());
        return updateById(BeanUtil.copyProperties(RoleUpdateDTO, RoleDO.class).setUpdateTime(LocalDateTime.now())) ?
                Result.success(MessageConstants.UPDATE_SUCCESS) : Result.error(MessageConstants.UPDATE_ERROR);
    }

    private void checkDuplicationColumn(Long id, String code) {
        Optional.ofNullable(lambdaQuery().ne(id != null, RoleDO::getId, id).eq(RoleDO::getCode, code).one()).ifPresent(RoleDO -> {
            throw new ServiceException(MessageConstants.ROLE_CODE_EXIST);
        });
    }

    @Override
    public String getRoleNameById(Long id) {
        RoleDO roleDO = getById(id);
        return roleDO != null ? roleDO.getRoleName() : "NULL";
    }

    @Override
    public Result getRoleById(Long id) {
        RoleDO roleDO = getById(id);
        Optional.ofNullable(roleDO).orElseThrow(() -> new ServiceException(MessageConstants.NO_FOUND_ROLE_ERROR));
        return Result.success(BeanUtil.copyProperties(roleDO, PermissionVO.class));
    }

    @Override
    public Result getRoleList() {
        List<RoleDO> RoleDOList = lambdaQuery().list();
        if (CollectionUtils.isEmpty(RoleDOList)) throw new ServiceException(MessageConstants.NO_FOUND_ROLE_ERROR);
        return Result.success(RoleDOList
                .stream().map(RoleDO -> CompletableFuture.supplyAsync(() -> new RoleVO(RoleDO))).toList()
                .stream().map(CompletableFuture::join).toList());
    }

    @Override
    public Result getRolePermissionTree(Long roleId) {
        List<RoleDO> roleDOList = lambdaQuery().list();
        if (CollectionUtils.isEmpty(roleDOList))
            throw new ServiceException(MessageConstants.NO_FOUND_ROLE_ERROR);

        List<RoleVO> roleVOList = roleDOList.stream()
                .map(item -> CompletableFuture.supplyAsync(() -> new RoleVO(item).setParentName(getRoleNameById(item.getParentId())))).toList()
                .stream().map(CompletableFuture::join).toList();
        return Result.success(makeRoleTree(roleVOList, 0L));
    }

    @Override
    public Result deleteRole(Long id) {
        return removeById(id) ? Result.success(MessageConstants.DELETE_SUCCESS) : Result.error(MessageConstants.DELETE_ERROR);
    }

    @Override
    public Result deleteRoleBatch(RoleDeleteBatchDTO deleteBatchDTO) {
        List<RoleDO> list = lambdaQuery().in(RoleDO::getId, deleteBatchDTO.getRoleIds()).list();
        if (CollectionUtils.isEmpty(list))
            throw new ServiceException(MessageConstants.NO_FOUND_ROLE_ERROR);

        return Db.removeByIds(list, RoleDO.class) ?
                Result.success(MessageConstants.OPERATION_SUCCESS) : Result.error(MessageConstants.OPERATION_ERROR);
    }


    @Override
    public Result assignPermissionToRole(RolePermissionDTO rolePermissionDTO) {
        return null;
    }

    @Override
    public Result removePermissionFromRole(RolePermissionDTO rolePermissionDTO) {
        return null;
    }

}
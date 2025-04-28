package com.shoppingmall.demo.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.shoppingmall.demo.constant.CacheConstants;
import com.shoppingmall.demo.constant.MessageConstants;
import com.shoppingmall.demo.enums.UserType;
import com.shoppingmall.demo.exception.ServiceException;
import com.shoppingmall.demo.mapper.RoleMapper;
import com.shoppingmall.demo.model.DO.RoleDO;
import com.shoppingmall.demo.model.DO.UserDO;
import com.shoppingmall.demo.model.DO.UserRoleDO;
import com.shoppingmall.demo.model.DTO.RoleDeleteBatchDTO;
import com.shoppingmall.demo.model.DTO.RoleSaveDTO;
import com.shoppingmall.demo.model.DTO.RoleUpdateDTO;
import com.shoppingmall.demo.model.DTO.UserRoleDTO;
import com.shoppingmall.demo.model.VO.RoleVO;
import com.shoppingmall.demo.service.IRoleService;
import com.shoppingmall.demo.service.common.LoginInfoService;
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
public class RoleServiceImpl extends ServiceImpl<RoleMapper, RoleDO> implements IRoleService {
    private final RedisIdWorker redisIdWorker;
    private final LoginInfoService loginInfoService;
    private final RoleMapper roleMapper;

    private static List<RoleVO> makeRoleTree(List<RoleVO> roleVOS, Long pid) {
        //创建集合保存分类数据
        List<RoleVO> roleVOList = new ArrayList<RoleVO>();
        //判断分类列表是否为空，如果不为空则使用分类列表，否则创建集合对象
        Optional.ofNullable(roleVOS).orElse(new ArrayList<RoleVO>())
                .stream().filter(item -> item != null && item.getParentId().equals(pid))
                .forEach(item -> {
                    //获取每一个item对象的子分类，递归生成分类树
                    List<RoleVO> children = makeRoleTree(roleVOS, item.getId());
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
                .setCreatorId((loginInfoService.getLoginId()))
                .setStatus(0)) ?
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
    public Result deleteRole(Long id) {
        List<UserRoleDO> userRoleDOList = Db.lambdaQuery(UserRoleDO.class).eq(UserRoleDO::getRoleId, id).list();
        if (CollectionUtils.isNotEmpty(userRoleDOList))
            throw new ServiceException(MessageConstants.ROLE_USED_ERROR);
        return removeById(id) ? Result.success(MessageConstants.DELETE_SUCCESS) : Result.error(MessageConstants.DELETE_ERROR);
    }

    @Override
    public Result deleteRoleBatch(RoleDeleteBatchDTO deleteBatchDTO) {
        List<Long> roleIds = deleteBatchDTO.getRoleIds();
        List<RoleDO> roleDOList = lambdaQuery().in(RoleDO::getId, roleIds).list();
        if (CollectionUtils.isEmpty(roleDOList))
            throw new ServiceException(MessageConstants.NO_FOUND_ROLE_ERROR);

        List<Long> useRoleIds = Db.lambdaQuery(UserRoleDO.class).in(UserRoleDO::getRoleId, roleIds).list()
                .stream().map(userRoleDO -> CompletableFuture.supplyAsync(userRoleDO::getRoleId)).toList()
                .stream().map(CompletableFuture::join).toList();

        List<RoleDO> roleDOS = roleDOList.stream().map(roleDO -> {
            if (!useRoleIds.contains(roleDO.getId())) return roleDO;
            return null;
        }).toList();

        return Db.removeByIds(roleDOS, RoleDO.class) ?
                Result.success(MessageConstants.OPERATION_SUCCESS) : Result.error(MessageConstants.OPERATION_ERROR);
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
        return Result.success(BeanUtil.copyProperties(roleDO, RoleVO.class).setParentName(getRoleNameById(roleDO.getParentId())));
    }

    public Result getRoleListByUserId(Long userId) {
        List<RoleVO> roleVOList = BeanUtil.copyToList(roleMapper.getRoleListByUserId(userId), RoleVO.class)
                .stream().map(item -> CompletableFuture.supplyAsync(() ->
                        item.setParentName(getRoleNameById(item.getParentId())))).toList()
                .stream().map(CompletableFuture::join).toList();
        return Result.success(roleVOList);
    }

    @Override
    public Result<List<Long>> getRoleIdListByUserId(Long userId) {
        List<Long> haveRoleIds = Db.lambdaQuery(UserRoleDO.class).eq(userId != null, UserRoleDO::getUserId, userId).list()
                .stream().map(userRoleDO -> CompletableFuture.supplyAsync(userRoleDO::getRoleId)).toList()
                .stream().map(CompletableFuture::join).toList();
        return Result.success(haveRoleIds);
    }

    @Override
    public Result getHaveSignRoleList(Long userId) {
        List<RoleDO> roleDOList = lambdaQuery().list();
        if (CollectionUtils.isEmpty(roleDOList))
            throw new ServiceException(MessageConstants.NO_FOUND_ROLE_ERROR);

        List<RoleVO> roleVOList = roleDOList
                .stream().map(RoleDO -> CompletableFuture.supplyAsync(() -> new RoleVO(RoleDO)
                        .setParentName(getRoleNameById(RoleDO.getParentId())))).toList()
                .stream().map(CompletableFuture::join).toList();

        if (userId != null) {
            List<Long> haveRoleIds = getRoleIdListByUserId(userId).getData();
            roleVOList = roleVOList.stream().peek(roleVO -> roleVO.setIsHave(haveRoleIds.contains(roleVO.getId()))).toList();
        }
        return Result.success(roleVOList);
    }

    @Override
    public Result getAllRoleList() {
        return getHaveSignRoleList(null);
    }

    @Override
    public Result getRoleTree() {
        List<RoleDO> roleDOList = lambdaQuery().list();
        if (CollectionUtils.isEmpty(roleDOList))
            throw new ServiceException(MessageConstants.NO_FOUND_ROLE_ERROR);

        List<RoleVO> roleVOList = roleDOList.stream()
                .map(item -> CompletableFuture.supplyAsync(() -> new RoleVO(item)
                        .setParentName(getRoleNameById((item.getParentId()))))).toList()
                .stream().map(CompletableFuture::join).toList();
        return Result.success(makeRoleTree(roleVOList, 0L));
    }

    @Override
    @Transactional(rollbackFor = {ServiceException.class, Exception.class})
    public Result assignRoleToUser(UserRoleDTO userRoleDTO) {
        Long userId = userRoleDTO.getUserId();
        List<UserRoleDO> list = Db.lambdaQuery(UserRoleDO.class).eq(UserRoleDO::getUserId, userId).list();
        if (CollectionUtils.isNotEmpty(list))
            if (!Db.lambdaUpdate(UserRoleDO.class).eq(UserRoleDO::getUserId, userId).remove())
                throw new ServiceException(MessageConstants.DELETE_ERROR);

        UserType type = UserType.USER;
        List<Long> roleIds = userRoleDTO.getRoleIds();
        if (CollectionUtils.isNotEmpty(roleIds)) {
            List<UserRoleDO> userRoleDOList = roleIds
                    .stream().map(roleId -> CompletableFuture.supplyAsync(() -> new UserRoleDO()
                            .setId(redisIdWorker.nextId(CacheConstants.USER_ROLE_ID))
                            .setUserId(userId).setRoleId(roleId))).toList()
                    .stream().map(CompletableFuture::join).toList();

            if (!Db.saveBatch(userRoleDOList, userRoleDOList.size()))
                throw new ServiceException(MessageConstants.SAVE_ERROR);

            List<String> codeList = roleIds.stream().map(roleId -> lambdaQuery().eq(RoleDO::getId, roleId).one()).toList()
                    .stream().map(roleDO -> CompletableFuture.supplyAsync(roleDO::getCode)).toList()
                    .stream().map(CompletableFuture::join).toList();

            if (codeList.contains(UserType.SUPER_ADMIN.getDesc())) type = UserType.ADMIN;
            else if (codeList.contains(UserType.ADMIN.getDesc())) type = UserType.ADMIN;
            else if (codeList.contains(UserType.MERCHANT.getDesc())) type = UserType.MERCHANT;
        }

        if (!Db.lambdaUpdate(UserDO.class).set(UserDO::getType, type).eq(UserDO::getId, userId).update())
            throw new ServiceException(MessageConstants.UPDATE_ERROR);

        return Result.success(MessageConstants.OPERATION_SUCCESS);
    }


}
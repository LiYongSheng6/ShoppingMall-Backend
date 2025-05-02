package com.shoppingmall.demo.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shoppingmall.demo.constant.CacheConstants;
import com.shoppingmall.demo.constant.MessageConstants;
import com.shoppingmall.demo.constant.RegexConstants;
import com.shoppingmall.demo.enums.ForbiddenType;
import com.shoppingmall.demo.enums.PermissionType;
import com.shoppingmall.demo.enums.UserType;
import com.shoppingmall.demo.exception.ServiceException;
import com.shoppingmall.demo.mapper.UserMapper;
import com.shoppingmall.demo.model.DO.UserDO;
import com.shoppingmall.demo.model.DTO.UserInfoDTO;
import com.shoppingmall.demo.model.DTO.UserLoginDTO;
import com.shoppingmall.demo.model.DTO.UserResetDTO;
import com.shoppingmall.demo.model.DTO.UserRgsDTO;
import com.shoppingmall.demo.model.DTO.UserUpdateDTO;
import com.shoppingmall.demo.model.Query.UserQuery;
import com.shoppingmall.demo.model.VO.PageVO;
import com.shoppingmall.demo.model.VO.UserVO;
import com.shoppingmall.demo.service.IPermissionService;
import com.shoppingmall.demo.service.IUserService;
import com.shoppingmall.demo.service.common.LoginInfoService;
import com.shoppingmall.demo.utils.CheckCodeUtil;
import com.shoppingmall.demo.utils.JwtUtil;
import com.shoppingmall.demo.utils.MD5Util;
import com.shoppingmall.demo.utils.RedisIdWorker;
import com.shoppingmall.demo.utils.Result;
import com.shoppingmall.demo.utils.ThreadLocalUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;


/**
 * @author redmi k50 ultra
 * * @date 2024/10/10
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements IUserService {
    private final UserMapper userMapper;
    private final RedisIdWorker redisIdWorker;
    private final StringRedisTemplate stringRedisTemplate;
    private final LoginInfoService loginInfoService;
    private final IPermissionService permissionService;

    @Override
    public Result hello() {
        return Result.success("访问测试成功!!!");
    }

    @Override
    public Result getFrontPermission() {
        UserDO userDO = getUserDO(loginInfoService.getLoginId());
        return Result.success(permissionService.getAllPermissionList(PermissionType.FRONT.getValue()));
    }

    private String getToken(UserDO userDO) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CacheConstants.TOKEN_USER_ID_KEY, userDO.getId());

        String token = JwtUtil.genToken(claims, 1000 * 60 * 60 * 24);
        stringRedisTemplate.opsForValue().set(CacheConstants.LOGIN_USER_TOKEN_KEY + userDO.getId(), token, 1, TimeUnit.DAYS);

        ThreadLocalUtil.set(claims);
        return token;
    }

    @Override
    public Result updateToken() {
        UserDO userDO = getUserDO(loginInfoService.getLoginId());

        String token = getToken(userDO);
        log.info("用户更新token信息：{}", userDO);
        return Result.success(MessageConstants.TOKEN_UPDATE_SUCCESS, token);
    }

    private void deleteRedisCache(String key) {
        stringRedisTemplate.opsForValue().getOperations().delete(key);
    }

    public void checkVerifyCode(String key, String inputCode) {
        //从redis中获取相关验证码
        String value = stringRedisTemplate.opsForValue().get(key);
        //检查值是否存在
        if (!StringUtils.hasLength(value)) {
            throw new ServiceException(MessageConstants.VERIFY_CODE_INVALIDATED);
        }
        //解析出验证码
        String verifyCode = CheckCodeUtil.parseVerifyCodeFromValue(value);
        //比对验证码
        if (!verifyCode.equalsIgnoreCase(inputCode)) {
            throw new ServiceException(MessageConstants.VERIFY_CODE_ERROR);
        }
    }

    private void checkVerifyCode(String codePrefix, String codeSuffix, String checkValue) {
        // 拼接redis验证码key
        String key = CheckCodeUtil.generateKey(codePrefix, codeSuffix);
        // 检查比对验证码
        checkVerifyCode(key, checkValue);
        //删除redis验证码
        deleteRedisCache(key);
    }

    @Override
    public Result register(UserRgsDTO userRgsDTO) {
        // 判断邮箱是否已经注册
        String email = userRgsDTO.getEmail();
        Optional.ofNullable(getUserByEmail(email)).ifPresent(userDO -> {
            throw new ServiceException(MessageConstants.EMAIL_READY_EXIT);
        });

        //redis验证码key
        String key, verifyCode = userRgsDTO.getCode();
        if (verifyCode.matches(RegexConstants.EMAIL_VERIFY_CODE_REGEX)) {
            //获取redis邮箱验证码key
            key = CheckCodeUtil.generateKey(CacheConstants.REGISTER_EMAIL_CODE_KEY, email);
        } else {
            //获取redis图片验证码key
            key = CheckCodeUtil.generateKey(CacheConstants.REGISTER_IMAGE_CODE_KEY, userRgsDTO.getVerifyCodeKey());
        }

        //比对验证码
        checkVerifyCode(key, verifyCode);

        // 加密密码
        userRgsDTO.setPassword(MD5Util.generate(userRgsDTO.getPassword()));
        // 封装用户信息
        UserDO userDO = BeanUtil.copyProperties(userRgsDTO, UserDO.class)
                .setId(redisIdWorker.nextId(CacheConstants.USER_ID_KEY))
                .setType(UserType.USER)
                .setIsForbidden(ForbiddenType.FALSE)
                .setCreateTime(LocalDateTime.now())
                .setUpdateTime(LocalDateTime.now());

        // 删除redis验证码
        deleteRedisCache(key);
        // 保存用户信息
        return save(userDO) ? Result.success(MessageConstants.REGISTER_SUCCESS) : Result.error(MessageConstants.REGISTER_ERROR);
    }

    @Override
    public Result login(UserLoginDTO userLoginDTO) {
        String account = userLoginDTO.getEmail();
        UserDO userDO = lambdaQuery().eq(UserDO::getEmail, account).one();
        Optional.ofNullable(userDO).orElseThrow(() -> new ServiceException(MessageConstants.NO_FOUND_USER_ERROR));

        // 检查用户是否被禁用
        if (ForbiddenType.TRUE.equals(userDO.getIsForbidden())) {
            throw new ServiceException(MessageConstants.USER_FORBIDDEN_ERROR);
        }
        // 检查是否提供了密码或验证码
        if (!StringUtils.hasLength(userLoginDTO.getCode()) && !StringUtils.hasLength(userLoginDTO.getPassword())) {
            throw new ServiceException(MessageConstants.PARAM_MISSING);
        }
        // 检查密码是否正确
        if (StringUtils.hasLength(userLoginDTO.getPassword())) {
            if (!MD5Util.verify(userLoginDTO.getPassword(), userDO.getPassword())) {
                throw new ServiceException(MessageConstants.PASSWORD_ERROR);
            }
        } else {
            // 检查验证码是否正确
            checkVerifyCode(CacheConstants.LOGIN_EMAIL_CODE_KEY, account, userLoginDTO.getCode());
        }

        log.info("用户登录信息：{}", userDO);

        // 登录成功，生成token
        String token = getToken(userDO);
        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        map.put("type", userDO.getType().getValue());
        return Result.success(MessageConstants.LOGIN_SUCCESS, map);
    }

    @Override
    public Result logout() {
        UserDO userDO = getUserDO(loginInfoService.getLoginId());
        deleteRedisCache(CacheConstants.LOGIN_USER_TOKEN_KEY + userDO.getId());
        return Result.success(MessageConstants.LOGOUT_SUCCESS);
    }

    @Override
    public Result updateLoginUserInfo(UserUpdateDTO userUpdateDTO) {
        userUpdateDTO.setId(loginInfoService.getLoginId());
        return updateUserInfoById(BeanUtil.copyProperties(userUpdateDTO, UserInfoDTO.class));
    }

    @Override
    public Result updateUserPassword(UserResetDTO userResetDTO) {
        UserDO userDO = getUserDO(loginInfoService.getLoginId());

        checkVerifyCode(CacheConstants.RESET_EMAIL_CODE_KEY, userDO.getEmail(), userResetDTO.getCode());

        if (lambdaUpdate().eq(UserDO::getId, userDO.getId())
                .set(UserDO::getPassword, MD5Util.generate(userResetDTO.getReset()))
                .set(UserDO::getUpdateTime, LocalDateTime.now()).update()
        ) {
            deleteRedisCache(CacheConstants.LOGIN_USER_TOKEN_KEY + userDO.getId());
            return Result.success(MessageConstants.UPDATE_SUCCESS);
        }
        return Result.error(MessageConstants.UPDATE_ERROR);
    }

    @Override
    public Result updateUserEmail(UserResetDTO userResetDTO) {
        UserDO userDO = getUserDO(loginInfoService.getLoginId());

        checkVerifyCode(CacheConstants.UPDATE_EMAIL_CODE_KEY, userResetDTO.getReset(), userResetDTO.getCode());

        return lambdaUpdate().eq(UserDO::getId, userDO.getId())
                .set(UserDO::getEmail, userResetDTO.getReset())
                .set(UserDO::getUpdateTime, LocalDateTime.now()).update() ?
                Result.success(MessageConstants.UPDATE_SUCCESS) : Result.error(MessageConstants.UPDATE_ERROR);
    }

    @Override
    public Result updateUserInfoById(UserInfoDTO userInfoDTO) {
        UserDO userDo = getUserDO(userInfoDTO.getId());

        if (userInfoDTO.getStudentId() != null) {
            if (!(userDo.getStudentId() != null && userInfoDTO.getStudentId().equals(userDo.getStudentId()))) {
                Optional.ofNullable(getUserByStudentId(userInfoDTO.getStudentId())).ifPresent(userDO -> {
                    throw new ServiceException(MessageConstants.STUDENT_ID_EXIST);
                });
            }
        }

        if (userInfoDTO.getEmail() != null) {
            if (!(userDo.getEmail() != null && userInfoDTO.getEmail().equals(userDo.getEmail()))) {
                Optional.ofNullable(getUserByEmail(userInfoDTO.getEmail())).ifPresent(userDO -> {
                    throw new ServiceException(MessageConstants.EMAIL_READY_EXIT);
                });
            }
        }

        if (userInfoDTO.getPhone() != null) {
            if (!(userDo.getPhone() != null && userInfoDTO.getPhone().equals(userDo.getPhone()))) {
                Optional.ofNullable(getUserByPhone(userInfoDTO.getPhone())).ifPresent(userDO -> {
                    throw new ServiceException(MessageConstants.USER_PHONE_EXIST);
                });
            }
        }

        userDo = BeanUtil.copyProperties(userInfoDTO, UserDO.class).setUpdateTime(LocalDateTime.now());
        return updateById(userDo) ? Result.success(MessageConstants.UPDATE_SUCCESS) : Result.error(MessageConstants.UPDATE_ERROR);
    }

    @Override
    public Result forbiddenUserById(Long id) {
        UserDO userDO = getUserDO(id);
        if (userDO.getIsForbidden() == ForbiddenType.TRUE)
            throw new ServiceException(MessageConstants.USER_FORBIDDEN_EXIST);

        if (updateById(userDO.setIsForbidden(ForbiddenType.TRUE).setUpdateTime(LocalDateTime.now()))) {
            deleteRedisCache(CacheConstants.LOGIN_USER_TOKEN_KEY + userDO.getId());
            return Result.success(MessageConstants.OPERATION_SUCCESS);
        }
        return Result.error(MessageConstants.OPERATION_ERROR);
    }

    @Override
    public Result unblockingUserById(Long id) {
        UserDO userDO = getUserDO(id);
        if (userDO.getIsForbidden() == ForbiddenType.FALSE)
            throw new ServiceException(MessageConstants.USER_UNBLOCKING_EXIST);
        return updateById(userDO.setIsForbidden(ForbiddenType.FALSE).setUpdateTime(LocalDateTime.now())) ?
                Result.success(MessageConstants.OPERATION_SUCCESS) : Result.error(MessageConstants.OPERATION_ERROR);
    }

    @Override
    public Result getLoginUserInfo() {
        return getUserInfoById(loginInfoService.getLoginId());
    }

    @Override
    public Result getUserInfoById(Long id) {
        // 检查并获取用户信息
        UserDO userDO = getUserDO(id);
        // 封装返回数据
        return Result.success(new UserVO(userDO));
    }

    /**
     * 根据id获取用户信息
     *
     * @param id
     * @return
     */
    private UserDO getUserDO(Long id) {
        // 根据id获取用户数据库信息
        UserDO userDO = getById(id);
        // 判断用户是否存在
        Optional.ofNullable(userDO).orElseThrow(() -> new ServiceException(MessageConstants.NO_FOUND_USER_ERROR));
        // 返回用户信息
        return userDO;
    }

    @Override
    public UserDO getUserByStudentId(String studentId) {
        return lambdaQuery().eq(UserDO::getStudentId, studentId).one();
    }

    private UserDO getUserByPhone(String phone) {
        LambdaQueryWrapper<UserDO> lqw = new LambdaQueryWrapper<UserDO>().eq(UserDO::getPhone, phone);
        return userMapper.selectOne(lqw);
    }

    private UserDO getUserByEmail(String email) {
        LambdaQueryWrapper<UserDO> lqw = new LambdaQueryWrapper<UserDO>().eq(UserDO::getEmail, email);
        return userMapper.selectOne(lqw);
    }

    @Override
    public Result deleteUserById(Long id) {
        if (removeById(id)) {
            deleteRedisCache(CacheConstants.LOGIN_USER_TOKEN_KEY + id);
            return Result.success(MessageConstants.DELETE_SUCCESS);
        }
        return Result.error(MessageConstants.DELETE_ERROR);
    }

    @Override
    public Result pageUserListByCondition(UserQuery userQuery) {
        Page<UserDO> page = userQuery.toMpPageDefaultSortByUpdateTime();

        UserType type = userQuery.getType();

        Page<UserDO> pageDO = lambdaQuery()
                .eq(type != null, UserDO::getType, type)
                .page(page);

        if (CollectionUtils.isEmpty(pageDO.getRecords())) {
            throw new ServiceException(MessageConstants.NO_FOUND_USER_ERROR);
        }
        return Result.success(PageVO.of(pageDO, UserVO::new));
    }

    @Override
    public String getUserNameById(Long id) {
        UserDO userDO = getById(id);
        return userDO != null ? userDO.getUsername() : "NULL";
    }

    @Override
    public String getUserAvatarById(Long id) {
        UserDO userDO = getById(id);
        return userDO != null ? userDO.getAvatar() : "NULL";
    }


}
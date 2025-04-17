package com.shoppingmall.demo.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;

import com.shoppingmall.demo.constant.CacheConstants;
import com.shoppingmall.demo.constant.MessageConstants;
import com.shoppingmall.demo.enums.ForbiddenType;
import com.shoppingmall.demo.enums.UserType;
import com.shoppingmall.demo.exception.ServiceException;
import com.shoppingmall.demo.mapper.UserMapper;
import com.shoppingmall.demo.model.DO.EmailDO;
import com.shoppingmall.demo.model.DO.UserDO;
import com.shoppingmall.demo.model.DTO.EmailDTO;
import com.shoppingmall.demo.model.DTO.UserCommentDTO;
import com.shoppingmall.demo.model.DTO.UserInfoDTO;
import com.shoppingmall.demo.model.DTO.UserLoginDTO;
import com.shoppingmall.demo.model.DTO.UserResetDTO;
import com.shoppingmall.demo.model.DTO.UserRgsDTO;
import com.shoppingmall.demo.model.DTO.UserUpdateDTO;
import com.shoppingmall.demo.model.Query.UserQuery;
import com.shoppingmall.demo.model.VO.PageVO;
import com.shoppingmall.demo.model.VO.UserPageVO;
import com.shoppingmall.demo.model.VO.UserVO;
import com.shoppingmall.demo.service.IEmailService;
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
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;


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
        return Result.success(permissionService.getPermissionList(userDO.getType().getValue()));
    }

    private String getToken(UserDO userDO) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CacheConstants.TOKEN_USER_ID_KEY, userDO.getId());
        claims.put(CacheConstants.TOKEN_USERNAME_KEY, userDO.getUsername());

        String token = JwtUtil.genToken(claims, 1000 * 60 * 60 * 24);
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        operations.set(CacheConstants.LOGIN_USER_TOKEN_KEY + userDO.getId(), token, 1, TimeUnit.DAYS);

        ThreadLocalUtil.set(claims);
        return token;
    }

    @Override
    public Result updateToken() {
        UserDO userDO = getById(loginInfoService.getLoginId());
        if (userDO == null) throw new ServiceException(MessageConstants.NO_FOUND_USER_ERROR);

        String token = getToken(userDO);
        log.info("用户更新token信息：{}", userDO);
        return Result.success(MessageConstants.TOKEN_UPDATE_SUCCESS, token);
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
        stringRedisTemplate.opsForValue().getOperations().delete(key);
    }

    @Override
    public Result register(UserRgsDTO userRgsDTO) {
        // 判断邮箱是否已经注册
        String email = userRgsDTO.getEmail();
        Optional.ofNullable(getUserByEmail(email)).ifPresent(userDO -> {
            throw new ServiceException(MessageConstants.EMAIL_READY_EXIT);
        });

        //获取redis邮箱验证码key
        String key = CheckCodeUtil.generateKey(CacheConstants.REGISTER_EMAIL_CODE_KEY, email);
        //比对验证码
        checkVerifyCode(key, userRgsDTO.getCode());

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
        stringRedisTemplate.opsForValue().getOperations().delete(key);
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

        if (!StringUtils.hasLength(userLoginDTO.getCode()) && !StringUtils.hasLength(userLoginDTO.getPassword())) {
            throw new ServiceException(MessageConstants.PARAM_MISSING);
        }

        if (StringUtils.hasLength(userLoginDTO.getPassword())) {
            if (!MD5Util.verify(userLoginDTO.getPassword(), userDO.getPassword())) {
                throw new ServiceException(MessageConstants.PASSWORD_ERROR);
            }
        } else {
            checkVerifyCode(CacheConstants.LOGIN_EMAIL_CODE_KEY, account, userLoginDTO.getCode());
        }

        // 登录成功，生成token
        String token = getToken(userDO);
        log.info("用户登录信息：{}", userDO);
        return Result.success(MessageConstants.LOGIN_SUCCESS, token);
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

        return lambdaUpdate().eq(UserDO::getId, userDO.getId())
                .set(UserDO::getPassword, MD5Util.generate(userResetDTO.getReset()))
                .set(UserDO::getUpdateTime, LocalDateTime.now()).update() ?
                Result.success(MessageConstants.UPDATE_SUCCESS) : Result.error(MessageConstants.UPDATE_ERROR);
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
        return updateById(userDO.setIsForbidden(ForbiddenType.TRUE).setUpdateTime(LocalDateTime.now())) ?
                Result.success(MessageConstants.OPERATION_SUCCESS) : Result.error(MessageConstants.OPERATION_ERROR);
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
    public Result<UserVO> getLoginUserInfo() {
        return getUserInfoById(loginInfoService.getLoginId());
    }

    @Override
    public Result<UserVO> getUserInfoById(Long id) {
        UserDO userDO = getUserDO(id);
        return Result.success(BeanUtil.copyProperties(userDO, UserVO.class));
    }

    private UserDO getUserDO(Long id) {
        UserDO userDO = getById(id);
        Optional.ofNullable(userDO).orElseThrow(() -> new ServiceException(MessageConstants.NO_FOUND_USER_ERROR));
        return userDO;
    }

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
        return removeById(id) ? Result.success(MessageConstants.DELETE_SUCCESS) : Result.error(MessageConstants.DELETE_ERROR);
    }

    @Override
    public Result<PageVO<UserPageVO>> pageUserListByCondition(UserQuery userQuery) {
        Page<UserDO> page = userQuery.toMpPageDefaultSortByUpdateTime();

        //Integer target = userQuery.getTarget();
        //UserStatusType status = userQuery.getStatus();

        Page<UserDO> pageDO = lambdaQuery()
                //.eq(target != null, UserDO::getTarget, target)
                //.eq(status != null, UserDO::getStatus, status)
                .page(page);

        if (CollectionUtils.isEmpty(pageDO.getRecords())) {
            throw new ServiceException(MessageConstants.NO_FOUND_USER_ERROR);
        }
        return Result.success(PageVO.of(pageDO, UserDO -> BeanUtil.copyProperties(UserDO, UserPageVO.class)));
    }


}
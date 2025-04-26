package com.shoppingmall.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shoppingmall.demo.model.DO.UserDO;
import com.shoppingmall.demo.model.DTO.UserInfoDTO;
import com.shoppingmall.demo.model.DTO.UserLoginDTO;
import com.shoppingmall.demo.model.DTO.UserResetDTO;
import com.shoppingmall.demo.model.DTO.UserRgsDTO;
import com.shoppingmall.demo.model.DTO.UserUpdateDTO;
import com.shoppingmall.demo.model.Query.UserQuery;
import com.shoppingmall.demo.utils.Result;

/**
 * @author redmi k50 ultra
 * * @date 2024/10/10
 */
public interface IUserService extends IService<UserDO> {

    Result hello();

    Result getFrontPermission();

    Result updateToken();

    Result register(UserRgsDTO userRgsDTO);

    Result login(UserLoginDTO userLoginDTO);

    Result logout();

    Result updateLoginUserInfo(UserUpdateDTO userUpdateDTO);

    Result updateUserPassword(UserResetDTO userResetDTO);

    Result updateUserEmail(UserResetDTO userResetDTO);

    Result updateUserInfoById(UserInfoDTO userInfoDTO);

    Result forbiddenUserById(Long id);

    Result unblockingUserById(Long id);

    Result getLoginUserInfo();

    Result getUserInfoById(Long id);

    Result deleteUserById(Long id);

    Result pageUserListByCondition(UserQuery userQuery);

    String getUserNameById(Long id);

    String getUserAvatarById(Long id);


}

package com.shoppingmall.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shoppingmall.demo.model.DO.UserDO;
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

    Result updateLoginUserInfo(UserUpdateDTO userUpdateDTO);

    Result updateUserPassword(UserResetDTO userResetDTO);

    Result updateUserEmail(UserResetDTO userResetDTO);

    Result updateUserInfoById(UserInfoDTO userInfoDTO);

    Result forbiddenUserById(Long id);

    Result unblockingUserById(Long id);

    Result<UserVO> getLoginUserInfo();

    Result<UserVO> getUserInfoById(Long id);

    Result deleteUserById(Long id);

    Result<PageVO<UserPageVO>> pageUserListByCondition(UserQuery userQuery);

}

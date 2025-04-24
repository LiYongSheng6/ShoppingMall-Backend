package com.shoppingmall.demo.service.common;

import com.shoppingmall.demo.constant.CacheConstants;
import com.shoppingmall.demo.constant.MessageConstants;
import com.shoppingmall.demo.exception.ServiceException;
import com.shoppingmall.demo.utils.ThreadLocalUtil;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

@Component
public class LoginInfoService {
    /**
     * 获取当前登录用户id
     *
     * @return
     */
    public Long getLoginId() {
        Map<String, Object> claims = ThreadLocalUtil.get();
        if (claims == null) return null;
        return (Long) claims.get(CacheConstants.TOKEN_USER_ID_KEY);
    }

    /**
     * 获取当前登录用户名
     *
     * @return
     */
    public String getLoginName() {
        Map<String, Object> claims = ThreadLocalUtil.get();
        if (claims == null) return null;
        return (String) claims.get(CacheConstants.TOKEN_USERNAME_KEY);
    }

    public Map<String, Object> getClaims() {
        return ThreadLocalUtil.get();
    }

    public void CheckLoginUserObject(Long userId) {
        if (!Objects.equals(getLoginId(), userId))
            throw new ServiceException(MessageConstants.PERMISSION_PROHIBITED_ERROR);
    }

}

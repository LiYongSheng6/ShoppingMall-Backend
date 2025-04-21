package com.shoppingmall.demo.aspect;

/**
 * 权限管理切面类
 * @author redmi k50 ultra
 * * @date 2024/8/3
 */

import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.shoppingmall.demo.constant.HttpStatus;
import com.shoppingmall.demo.constant.MessageConstants;
import com.shoppingmall.demo.enums.UserType;
import com.shoppingmall.demo.exception.ServiceException;
import com.shoppingmall.demo.model.DO.UserDO;
import com.shoppingmall.demo.service.common.LoginInfoService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
@RequiredArgsConstructor
public class AuthorizeAspect {

    private final LoginInfoService loginInfoService;

    /**
     * 配置切入点 , @annotation
     */
    @Pointcut("@annotation(com.shoppingmall.demo.annotation.PreAuthorize)")
    public void authorizePointCut(){
    }

    /**
     * 后端接口资源鉴权
     * @return
     */
    @Around("authorizePointCut()")
    public Object handle(ProceedingJoinPoint pjp) throws Throwable {
        //设置返回内容类型
        String contentType = "application/json;charset=UTF-8";
        //1.获取用户信息：
        UserDO userDO = Db.getById(loginInfoService.getLoginId(), UserDO.class);
        if (userDO == null) throw new ServiceException(HttpStatus.UNAUTHORIZED, MessageConstants.NO_LOGIN_ERROR);

        //判断是否为管理员
        boolean result = UserType.ADMIN.equals(userDO.getType());
        //5.如果当前角色没有当前接口资源的权限：无权访问
        if(!result){
            throw new ServiceException(HttpStatus.ACCESS_RESTRICTED, MessageConstants.NO_PERMISSION_ERROR);
        }

        //执行加了此注解的方法后返回
        return pjp.proceed();
    }

}

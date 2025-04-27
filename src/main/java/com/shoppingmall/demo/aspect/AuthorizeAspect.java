package com.shoppingmall.demo.aspect;

/**
 * 权限管理切面类
 * @author redmi k50 ultra
 * * @date 2024/8/3
 */

import com.shoppingmall.demo.annotation.PreAuthorize;
import com.shoppingmall.demo.constant.HttpStatus;
import com.shoppingmall.demo.constant.MessageConstants;
import com.shoppingmall.demo.exception.ServiceException;
import com.shoppingmall.demo.mapper.PermissionMapper;
import com.shoppingmall.demo.service.common.LoginInfoService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;

@Component
@Aspect
@RequiredArgsConstructor
public class AuthorizeAspect {

    private final LoginInfoService loginInfoService;
    private final PermissionMapper permissionMapper;

    /**
     * 配置切入点 , @annotation
     */
    @Pointcut("@annotation(com.shoppingmall.demo.annotation.PreAuthorize)")
    public void authorizePointCut(){
    }

    private static String getMethodPermission(ProceedingJoinPoint pjp) {
        //获取方法签名对象
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        //获取目标方法对象（后端接口资源）
        Method method = signature.getMethod();
        //获取注解权限标识符
        PreAuthorize annotation = method.getAnnotation(PreAuthorize.class);
        //返回权限标识符
        return annotation.value();
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
        Long userId = loginInfoService.getLoginId();

        /*
        UserDO userDO = Db.getById(userId, UserDO.class);
        if (userDO == null) throw new ServiceException(HttpStatus.UNAUTHORIZED, MessageConstants.NO_LOGIN_ERROR);
        //判断是否为管理员
        boolean result = UserType.ADMIN.equals(userDO.getType());
        //5.如果当前角色没有当前接口资源的权限：无权访问
        if(!result) throw new ServiceException(HttpStatus.ACCESS_RESTRICTED, MessageConstants.NO_PERMISSION_ERROR);
        return pjp.proceed();
        */

        //2.获取当前接口资源的访问权限标识符
        String methodPermission = getMethodPermission(pjp);
        //3.获取用户权限列表 ，stream流收集权限标识符code
        List<String> perms = permissionMapper.getPermissionCodeListByUserId(userId);
        //4.比较判断是否拥有相应权限
        boolean result1 = perms.contains(methodPermission);
        //5.如果当前角色没有当前接口资源的权限：403/亲,无权访问哦！
        if (!result1) throw new ServiceException(HttpStatus.ACCESS_RESTRICTED, MessageConstants.NO_PERMISSION_ERROR);
        //执行加了此注解的方法后返回
        return pjp.proceed();
    }

}

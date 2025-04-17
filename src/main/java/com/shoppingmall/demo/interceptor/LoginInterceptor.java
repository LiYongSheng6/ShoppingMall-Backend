package com.shoppingmall.demo.interceptor;


import com.shoppingmall.demo.constant.CacheConstants;
import com.shoppingmall.demo.constant.HttpStatus;
import com.shoppingmall.demo.constant.MessageConstants;
import com.shoppingmall.demo.exception.ServiceException;
import com.shoppingmall.demo.service.common.RedisCacheService;
import com.shoppingmall.demo.utils.JwtUtil;
import com.shoppingmall.demo.utils.ThreadLocalUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.List;
import java.util.Map;

/**
 * @author redmi k50 ultra
 * * @date 2024/7/20
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LoginInterceptor implements HandlerInterceptor {

    private final RedisCacheService redisCacheService;
    public static String TOKEN_HEADER = "Authorization";
    private static final List<String> list = List.of("/user/register", "/user/login", "/email/register", "/email/login");

    /**
     * 执行请求接口前的处理
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) throws Exception {
        // 记录请求路径
        String requestURI = request.getRequestURI();
        log.info("登录拦截器拦截：{}", requestURI);
        if (list.contains(requestURI)) {
            // 跳过登录验证逻辑
            return true;
        }

        //获取令牌
        String token = request.getHeader(TOKEN_HEADER);
        if (!StringUtils.hasLength(token)) {
            throw new ServiceException(HttpStatus.BAD_REQUEST, MessageConstants.TOKEN_NOT_FOUND);
        }

        //验证token
        try {
            //解析token
            Map<String, Object> claims = JwtUtil.parseToken(token);
            Long userId = (Long) claims.get(CacheConstants.TOKEN_USER_ID_KEY);

            //从redis中获取相同token
            String redisToken = redisCacheService.getCaCheString(CacheConstants.LOGIN_USER_TOKEN_KEY + userId);

            if (!token.equals(redisToken)) {
                throw new ServiceException(HttpStatus.UNAUTHORIZED, MessageConstants.TOKEN_CHECK_EXCEPTION);
            }

            //把业务数据存储到ThreadLocal中
            ThreadLocalUtil.set(claims);
            //放行
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            if (e instanceof ServiceException) throw e;
            else throw new ServiceException(HttpStatus.UNAUTHORIZED, MessageConstants.TOKEN_INVALIDATED);
        }
    }

    /**
     * 请求接口结束后的操作
     *
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler, Exception ex) throws Exception {
        //清空ThreadLocal中的数据
        ThreadLocalUtil.remove();
    }
}

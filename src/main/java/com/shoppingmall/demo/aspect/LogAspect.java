package com.shoppingmall.demo.aspect;

import com.alibaba.fastjson2.JSON;
import com.shoppingmall.demo.annotation.Log;
import com.shoppingmall.demo.service.common.SystemLog;
import com.shoppingmall.demo.utils.IPUtils;
import com.shoppingmall.demo.utils.ServletUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.NamedThreadLocal;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author：Hikko
 * @date: 2024/03/07
 * @time: 22:08
 */
@Aspect
@Component
@Slf4j
@Order(1)
public class LogAspect {
    /**
     * 计算操作消耗时间
     */
    private static final ThreadLocal<Long> TIME_THREADLOCAL = new NamedThreadLocal<>("Cost Time");

    /**
     * 处理请求前执行
     */
    @Before(value = "@annotation(controllerLog)")
    public void boBefore(JoinPoint joinPoint, Log controllerLog) {
        TIME_THREADLOCAL.set(System.currentTimeMillis());
    }

    /**
     * 处理完请求后执行
     *
     * @param joinPoint 切点
     */
    @AfterReturning(pointcut = "@annotation(log)", returning = "jsonResult")
    public void doAfterReturning(JoinPoint joinPoint, Log log, Object jsonResult) {
        handleLog(joinPoint, log, null, jsonResult);
    }

    /**
     * 拦截异常操作
     *
     * @param joinPoint 切点
     * @param e         异常
     */
    @AfterThrowing(value = "@annotation(log)", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Log log, Exception e) {
        handleLog(joinPoint, log, e, null);
    }

    protected void handleLog(final JoinPoint joinPoint, Log controllerLog, final Exception e, Object jsonResult) {
        try {
            HttpServletRequest request = ServletUtils.getRequest();
            HttpServletResponse response = ServletUtils.getResponse();
            SystemLog systemLog = new SystemLog();
            Long costTime = System.currentTimeMillis() - TIME_THREADLOCAL.get();
            String url = request.getRequestURI();
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            String ip = IPUtils.getIpAddr(request);
            systemLog.setClassMethod(className + "." + methodName + "()");
            systemLog.setStatus(response.getStatus());
            systemLog.setIp(ip);
            systemLog.setResponseTime(costTime);
            systemLog.setRequestMethod(request.getMethod());
            systemLog.setUrl(url);
//            systemLog.setNode(Constants.VERSION.getNode());
            if (ObjectUtils.isNotEmpty(e)) {
                systemLog.setErrMsg(StringUtils.substring(e.getMessage(), 0, 1000));
            }
            if (controllerLog.isSaveRequestData()) {
                Object[] args = joinPoint.getArgs();
                systemLog.setParams(args);
            }
            if (controllerLog.isSaveResponseData()) {
                systemLog.setResult(jsonResult);
            }

            log.info(JSON.toJSONString(systemLog));
        } catch (Exception exp) {
            // 记录本地异常日志
            log.error("异常信息:{}", exp.getMessage());
        } finally {
            TIME_THREADLOCAL.remove();
        }
    }

}

package com.pxxy.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * 日志打印切面类
 */
@Slf4j
@Aspect
@Component
public class ControllerLoggerAOP {
    // 环绕通知
    @Around("execution(* com.pxxy.controller.*.*(..))")
    public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("【进入方法】 " + joinPoint.toString());
        log.info("【参数列表】 " + Arrays.toString(joinPoint.getArgs()));
        Object ret;
        try {
            ret = joinPoint.proceed();
        } catch (Throwable e) {
            log.error("【出现异常】", e);
            throw e;
        }
        log.info("【返回结果】 " + ret);
        return ret;
    }

}

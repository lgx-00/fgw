package com.pxxy.advice;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Arrays;

/**
 * 日志打印切面类
 */
@Slf4j
@Aspect
@Component
public class ControllerLoggerAOP {

    @Value("${fgw.log.max-length}")
    private int maxLength = 200;

    // 环绕通知
    @Around("execution(* com.pxxy.controller.*.*(..))")
    public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable {
        LocalTime startTime = LocalTime.now();
        log.info("【进入方法】 " + joinPoint.toString());
        if (joinPoint.getArgs().length > 0) {
            log.info("【参数列表】 {}", toString(Arrays.toString(joinPoint.getArgs())));
        }
        Object ret;
        try {
            ret = joinPoint.proceed();
        } catch (Throwable e) {
            LocalTime endTime = LocalTime.now();
            if ("com.pxxy.exceptions".equals(e.getClass().getPackage().getName())) {
                log.error("【出现异常】 异常类型：{}，执行耗时 {}", e.getClass(), Duration.between(startTime, endTime));
            } else {
                log.error("【出现异常】 执行耗时 {}", Duration.between(startTime, endTime), e);
            }
            throw e;
        }
        LocalTime endTime = LocalTime.now();

        log.info("【返回结果】 {}，执行耗时 {}", toString(ret), Duration.between(startTime, endTime));
        return ret;
    }

    private String toString(Object o) {
        String s = o.toString();
        if (s.length() > maxLength) {
            s = s.substring(0, maxLength - 3) + "...";
        }
        return s;
    }

}

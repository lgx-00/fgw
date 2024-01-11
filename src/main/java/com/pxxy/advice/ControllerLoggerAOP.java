package com.pxxy.advice;

import com.pxxy.utils.ToStringUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalTime;

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
        LocalTime startTime = LocalTime.now();
        if (joinPoint.getArgs().length > 0) {
            log.info("【进入方法】 " + joinPoint + ";【参数列表】 {}", ToStringUtil.toString(joinPoint.getArgs()));
        } else {
            log.info("【进入方法】 " + joinPoint);
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

        log.info("【返回结果】 {}，执行耗时 {}", ToStringUtil.toString(ret), Duration.between(startTime, endTime));
        return ret;
    }

}

package com.pxxy.advice;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;

/**
 * <h3>操作记录切面类</h3>
 *
 * <p>Create time: 2023/7/17 14:14</p>
 *
 * @author xw
 * @version 1.0
 */
// @Slf4j
// @Aspect
// @Component
public class OperationLoggerAOP {
    // TODO: 2023/7/17 编写记录执行的操作的方法
    // 环绕通知
    @Around("execution(* com.pxxy.controller.*.*(..))")
    public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable {


        return joinPoint.proceed();
    }

}

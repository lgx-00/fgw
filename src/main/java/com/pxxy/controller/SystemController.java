package com.pxxy.controller;

import com.pxxy.advice.BaseDataCacheAOP;
import com.pxxy.entity.vo.OnlineUserVO;
import com.pxxy.utils.ResultResponse;
import com.pxxy.utils.TokenUtil;
import com.pxxy.utils.UserHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

import static com.pxxy.constant.ResponseMessage.NO_PERMISSION_FOR_SYSTEM_CONTROL;

/**
 * Class name: SystemController
 * Create time: 2023/7/20 17:00
 *
 * @author xw
 * @version 1.0
 */
@CrossOrigin
@RestController
@Api(tags = "系统")
@RequestMapping("/system-control")
public class SystemController {

    @Resource
    private BaseDataCacheAOP baseDataCacheAOP;

    @GetMapping("/flush")
    @ApiOperation("刷新缓存")
    public ResultResponse<?> flushCache() {
        baseDataCacheAOP.flushCache();
        return ResultResponse.ok();
    }

    @GetMapping("/users")
    @ApiOperation("查看当前在线人员")
    public ResultResponse<List<OnlineUserVO>> getOnlineUsers() {
        return ResultResponse.ok(TokenUtil.getOnlineUsers());
    }

    @PostMapping("/user")
    @ApiOperation("强制一位用户下线")
    public ResultResponse<?> logout(@RequestParam String token) {
        TokenUtil.invalidate(token);
        return ResultResponse.ok();
    }

    @PostMapping("/users")
    @ApiOperation("强制所有用户下线")
    public ResultResponse<?> logoutAll() {
        TokenUtil.invalidateAll();
        return ResultResponse.ok();
    }

    @Slf4j
    @Aspect
    @Component
    public static class SystemControllerAdvice {

        @Around("execution(* com.pxxy.controller.SystemController.*(..))")
        public Object before(ProceedingJoinPoint pjp) throws Throwable {
            if (UserHolder.getUser().getUId().equals(1)) {
                return pjp.proceed();
            }
            log.warn("【系统控制】 用户无权限执行系统控制的相关操作，已拦截执行'{}'操作。", pjp.toShortString());
            return ResultResponse.fail(NO_PERMISSION_FOR_SYSTEM_CONTROL);
        }

    }

}

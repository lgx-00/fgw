package com.pxxy.advice;

import cn.hutool.core.net.Ipv4Util;
import com.pxxy.dto.UserDTO;
import com.pxxy.pojo.Operation;
import com.pxxy.service.OperationService;
import com.pxxy.utils.ResultResponse;
import com.pxxy.utils.UserHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import static com.pxxy.constant.ResponseMessage.OK_CODE;

/**
 * <h3>操作记录切面类</h3>
 *
 * <p>Create time: 2023/7/17 14:14</p>
 *
 * @author xw
 * @version 1.0
 */
@Slf4j
@Aspect
@Component
public class OperationLoggerAOP {

    @Resource
    private OperationService operService;

    @AfterReturning(
            value = "execution(* com.pxxy.controller.*.*(..)) && " +
                    "!@annotation(org.springframework.web.bind.annotation.GetMapping)",
            returning = "ret")
    public Object doLog(JoinPoint joinPoint, Object ret) {
        UserDTO user = UserHolder.getUser();
        if (user == null) {
            log.warn("【操作记录切面】 用户为空。");
            return ret;
        }

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest req = ((ServletRequestAttributes) requestAttributes).getRequest();

        Operation oper = new Operation();
        oper.setOperIp(Ipv4Util.ipv4ToLong(req.getRemoteAddr()));
        oper.setOperMethod(req.getMethod());
        oper.setOperPath(req.getRequestURI());
        oper.setOperInfo(getInfo(signature));
        oper.setUId(user.getUId());

        if (ret instanceof ResultResponse) {
            ResultResponse<?> rr = ((ResultResponse<?>) ret);
            if (!new Integer(OK_CODE).equals(rr.getCode())) {  // 操作失败
                oper.setOperStatus(1);
            }
        } else {
            oper.setOperStatus(2);
            log.warn("【操作记录切面】 返回类型是 {}。", signature.getDeclaringTypeName());
        }

        if (!operService.save(oper)) {
            log.warn("【操作记录切面】 保存记录失败。内容为 {}", oper);
        }
        return ret;
    }

    private String getInfo(MethodSignature signature) {
        Api api = ((Class<?>) signature.getDeclaringType()).getAnnotation(Api.class);
        ApiOperation apiOperation = signature.getMethod().getAnnotation(ApiOperation.class);
        StringBuilder info = new StringBuilder();

        boolean flagApi = api != null && api.tags() != null;
        boolean flagApiOperation = apiOperation != null && apiOperation.value() != null;
        if (flagApi) {
            info.append(api.tags()[0]);
        }
        if (flagApi && flagApiOperation) {
            info.append("/");
        }
        if (flagApiOperation) {
            info.append(apiOperation.value());
        }

        return info.toString();
    }

}

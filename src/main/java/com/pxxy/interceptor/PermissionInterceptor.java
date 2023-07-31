package com.pxxy.interceptor;

import cn.hutool.json.JSONUtil;
import com.pxxy.dto.PermissionDTO;
import com.pxxy.dto.UserDTO;
import com.pxxy.enums.RequestMethodEnum;
import com.pxxy.utils.TokenUtil;
import com.pxxy.utils.ResultResponse;
import com.pxxy.utils.UserHolder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Set;

import static com.pxxy.constant.ResponseMessage.PERMISSION_NOT_ENOUGH;

/**
 * @Author: hesen
 * @Date: 2023-06-20-9:31
 * @Description:
 */
@Data
@Slf4j
public class PermissionInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object handler) throws Exception {
        //拦截器取到请求先进行判断，如果是OPTIONS请求，则放行
        if("OPTIONS".equalsIgnoreCase(req.getMethod())) {
            return true;
        }

        log.info("【权限拦截器】远程主机地址：{}，请求路径：{}，请求方法：{}",
                req.getRemoteAddr(), req.getServletPath(), req.getMethod());

        UserDTO user = TokenUtil.getUser(req.getHeader("X-Token"));

        // 管理员的后门
        if (user.getUId().equals(1)) {
            log.info("【权限拦截器】离开权限拦截器，管理员直接放行。");
            return true;
        }

        // 请求路径
        String url = req.getServletPath();
        RequestMethodEnum method = RequestMethodEnum.valueOf(req.getMethod());


        Map<String, PermissionDTO> permission = user.getPermission();
        Set<String> keys = permission.keySet();
        boolean perm = false;

        for (String key : keys) {
            if (url.startsWith(key)) {
                perm = (permission.get(key).getRpDetail() >> method.digit & 1) > 0;
                break;
            }
        }

        if (!perm) {
            resp.setCharacterEncoding("UTF-8");
            resp.setStatus(PERMISSION_NOT_ENOUGH);
            resp.setContentType(MediaType.APPLICATION_JSON_VALUE);
            resp.getWriter().write(JSONUtil.toJsonStr(ResultResponse.fail(PERMISSION_NOT_ENOUGH, method.msg)));
            log.info("【权限拦截器】离开权限拦截器，请求被拦截。");
        } else {
            log.info("【权限拦截器】离开权限拦截器，请求顺利通过。");
        }
        return perm;
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler, Exception ex) {
        UserHolder.removeUser();
    }
}

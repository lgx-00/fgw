package com.pxxy.interceptor;

import cn.hutool.json.JSONUtil;
import com.pxxy.entity.dto.UserDTO;
import com.pxxy.utils.TokenUtil;
import com.pxxy.utils.ResultResponse;
import com.pxxy.utils.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.pxxy.constant.ResponseMessage.INVALID_TOKEN;


/**
 * @author hesen
 * @since 2023-06-05-15:08
 * @Description:
 */

@Slf4j
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 拦截器取到请求先进行判断，如果是OPTIONS请求，则放行
        if("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        // 验证 Token 的有效性
        String xToken = request.getHeader("X-Token");
        UserDTO user = TokenUtil.getUser(xToken);
        if (user == null) {
            log.info("【身份验证拦截器】令牌无效，来自 {} 的请求 {} 已被拦截。",
                    request.getRemoteAddr(), request.getRequestURI());
            return fail(response);
        }

        // 保存用户信息到 ThreadLocal
        UserHolder.saveUser(user);

        // 放行
        return true;
    }

    private boolean fail(HttpServletResponse response) throws IOException {
        // 拦截，返回401状态码
        response.setStatus(INVALID_TOKEN);
        response.setHeader("Content-Type", "application/json");
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(JSONUtil.toJsonStr(ResultResponse.fail(INVALID_TOKEN, "无效的令牌！")));

        return false;
    }
}

package com.pxxy.intercepter;

import cn.hutool.json.JSONUtil;
import com.pxxy.dto.UserDTO;
import com.pxxy.utils.RandomTokenUtil;
import com.pxxy.utils.ResultResponse;
import com.pxxy.utils.UserHolder;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static com.pxxy.constant.ResponseConstant.INVALID_TOKEN;


/**
 * @Author: hesen
 * @Date: 2023-06-05-15:08
 * @Description:
 */
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取session
        HttpSession session = request.getSession();

        // 验证 Token 的有效性
        String xToken = request.getHeader("X-Token");
        if (!RandomTokenUtil.verifyAndRefresh(xToken, session.getMaxInactiveInterval())) {
            return fail(response);
        }

        //获取session中的用户，保存用户信息到ThreadLocal
        UserHolder.saveUser((UserDTO) session.getAttribute("user"));

        //放行
        return true;
    }

    private boolean fail(HttpServletResponse response) throws IOException {
        // 拦截，返回401状态码
        response.setStatus(INVALID_TOKEN);
        response.setHeader("Content-Type", "application/json");
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(JSONUtil.toJsonStr(ResultResponse.fail(INVALID_TOKEN, "无效的 Token！")));
        return false;
    }
}

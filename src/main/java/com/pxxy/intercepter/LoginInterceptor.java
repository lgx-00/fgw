package com.pxxy.intercepter;

import com.pxxy.dto.UserDTO;
import com.pxxy.utils.UserHolder;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


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
        //获取session中的用户
        UserDTO userDTO = (UserDTO) session.getAttribute("user");
        //判断用户是否存在
        if (userDTO == null){
            //不存在拦截，返回401状态码
            response.setStatus(401);
            return false;
        }

        //存在，保存用户信息到ThreadLocal
        UserHolder.saveUser(userDTO);

        //放行
        return true;
    }
}

package com.pxxy.service;

import com.pxxy.dto.LoginFormDTO;
import com.pxxy.dto.Result;
import com.pxxy.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;
import javax.servlet.http.HttpSession;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hs
 * @since 2023-06-13
 */
public interface UserService extends IService<User> {

    Result login(LoginFormDTO loginForm, HttpSession session);
}

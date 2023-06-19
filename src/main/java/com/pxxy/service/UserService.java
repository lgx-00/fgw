package com.pxxy.service;

import com.pxxy.dto.LoginFormDTO;
import com.pxxy.dto.Result;
import com.pxxy.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pxxy.vo.AddUserVO;
import com.pxxy.vo.UpdateUserVO;

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

    Result logout(HttpSession session);

    Result addUser(AddUserVO addUserVO);

    Result getRoleByUserId(Integer userId);

    Result deleteUser(Integer userId);

    Result modifyUser(Integer userId, UpdateUserVO updateUserVO);

    Result getAllUser(Integer pageNum);

    Result getVagueUser(int pageNum,String uName);
}

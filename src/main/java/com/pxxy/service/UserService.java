package com.pxxy.service;

import com.pxxy.dto.LoginFormDTO;
import com.pxxy.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pxxy.utils.ResultResponse;
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

    ResultResponse login(LoginFormDTO loginForm, HttpSession session);

    ResultResponse logout(HttpSession session);

    ResultResponse addUser(AddUserVO addUserVO);

    ResultResponse deleteUser(Integer userId);

    ResultResponse modifyUser(UpdateUserVO updateUserVO);

    ResultResponse getAllUser(Integer pageNum);

    ResultResponse getVagueUser(int pageNum, String uName);
}

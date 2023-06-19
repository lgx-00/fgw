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

    com.pxxy.utils.ResultResponse login(LoginFormDTO loginForm, HttpSession session);

    com.pxxy.utils.ResultResponse logout(HttpSession session);

    com.pxxy.utils.ResultResponse addUser(AddUserVO addUserVO);

    com.pxxy.utils.ResultResponse getRoleByUserId(Integer userId);

    com.pxxy.utils.ResultResponse deleteUser(Integer userId);

    com.pxxy.utils.ResultResponse modifyUser(Integer userId, UpdateUserVO updateUserVO);

    com.pxxy.utils.ResultResponse getAllUser(Integer pageNum);

    ResultResponse getVagueUser(int pageNum, String uName);
}

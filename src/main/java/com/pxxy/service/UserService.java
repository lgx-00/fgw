package com.pxxy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pxxy.dto.LoginFormDTO;
import com.pxxy.pojo.User;
import com.pxxy.utils.ResultResponse;
import com.pxxy.vo.AddUserVO;
import com.pxxy.vo.UpdateUserVO;
import org.springframework.validation.annotation.Validated;

import javax.servlet.http.HttpSession;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hs
 * @since 2023-06-13
 */
@Validated
public interface UserService extends IService<User> {

    ResultResponse login(LoginFormDTO loginForm, HttpSession session);

    ResultResponse logout(HttpSession session);

    ResultResponse addUser(@Validated AddUserVO addUserVO);

    ResultResponse deleteUser(Integer userId);

    ResultResponse modifyUser(@Validated UpdateUserVO updateUserVO);

    ResultResponse getAllUser(Integer pageNum);

    ResultResponse getVagueUser(int pageNum, String uName);
}

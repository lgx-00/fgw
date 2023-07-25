package com.pxxy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.pxxy.pojo.User;
import com.pxxy.utils.ResultResponse;
import com.pxxy.vo.*;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hs
 * @since 2023-06-13
 */
public interface UserService extends IService<User> {

    ResultResponse<String> login(LoginVO loginForm);

    ResultResponse<?> addUser(AddUserVO addUserVO);

    ResultResponse<?> deleteUser(Integer userId);

    ResultResponse<?> updateUser(UpdateUserVO updateUserVO);

    ResultResponse<PageInfo<QueryUserVO>> getAllUser(Page page);

    ResultResponse<PageInfo<QueryUserVO>> getVagueUser(Page page, String uName);

}

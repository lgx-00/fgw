package com.pxxy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.pxxy.entity.dto.UserDTO;
import com.pxxy.entity.pojo.User;
import com.pxxy.entity.vo.*;

import java.util.Map;
import java.util.Set;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hs
 * @since 2023-06-13
 */
public interface UserService extends IService<User> {

    String login(LoginVO loginForm);

    Map<Integer, UserDTO> getPerms(Set<Integer> userIdSet);

    UserDTO getPerms(User user);

    boolean addUser(AddUserVO addUserVO);

    boolean deleteUser(Integer userId);

    boolean updateUser(UpdateUserVO updateUserVO);

    PageInfo<QueryUserVO> getAllUser(Page page);

    PageInfo<QueryUserVO> getVagueUser(Page page, String uName);

    boolean updateUserPassword(String old, UpdateUserVO updateUserVO);
}

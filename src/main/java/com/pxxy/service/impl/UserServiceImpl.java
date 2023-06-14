package com.pxxy.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.pxxy.dto.LoginFormDTO;
import com.pxxy.dto.Result;
import com.pxxy.dto.UserDTO;
import com.pxxy.pojo.User;
import com.pxxy.mapper.UserMapper;
import com.pxxy.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hs
 * @since 2023-06-13
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public Result login(LoginFormDTO loginForm, HttpSession session) {

        //根据用户名查询用户
        String uName = loginForm.getUName();

        if (uName == null || uName.equals("")){
            return Result.fail("用户名不能为空!");
        }

        User user = query().eq("u_name", uName).one();

        if (user != null) {
            if (loginForm.getUPassword().equals(user.getUPassword())){
                //保存用户信息到session中
                session.setAttribute("user", BeanUtil.copyProperties(user, UserDTO.class));
            }
        }else {
            return Result.fail("用户不存在！");
        }

        return Result.ok();
    }
}

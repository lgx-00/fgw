package com.pxxy.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.pxxy.dto.LoginFormDTO;
import com.pxxy.dto.Result;
import com.pxxy.dto.UserDTO;
import com.pxxy.pojo.User;
import com.pxxy.mapper.UserMapper;
import com.pxxy.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import javax.servlet.http.HttpSession;
import com.pxxy.utils.Md5Utils;
import org.springframework.stereotype.Service;
import java.util.Date;

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

        String password = Md5Utils.code(loginForm.getUPassword());

        if (user != null) {
            if (password.equals(user.getUPassword())){
                //保存用户信息到session中
                session.setAttribute("user", BeanUtil.copyProperties(user, UserDTO.class));
            }else {
                return Result.fail("密码输入错误！");
            }
        }else {
            return Result.fail("用户不存在！");
        }

        //登录成功，更新用户登录时间
        user.setULoginTime(new Date());

        return Result.ok();
    }

    @Override
    public Result logout(HttpSession session) {
        if (session != null){
            //退出登录，如果session存在吗，则销毁
            session.invalidate();
            return Result.ok("销毁session成功！");
        }
        return Result.fail("销毁session失败！");
    }
}

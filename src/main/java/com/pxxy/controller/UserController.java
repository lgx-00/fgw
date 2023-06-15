package com.pxxy.controller;


import com.pxxy.dto.LoginFormDTO;
import com.pxxy.dto.Result;
import com.pxxy.service.UserService;
import com.pxxy.utils.UserHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author hs
 * @since 2023-06-13
 */
@RestController
@RequestMapping("/user")
@Api(tags = "用户")
public class UserController {
    @Resource
    private UserService userService;

    @PostMapping("/login")
    @ApiOperation("用户登录")
    public Result login(@RequestBody LoginFormDTO loginForm, HttpSession session) {
        //实现登录功能
        return userService.login(loginForm, session);
    }

    @PostMapping("/logout")
    @ApiOperation("退出登录")
    public Result logout(HttpSession session){
        //实现登出功能
        return userService.logout(session);
    }

    @GetMapping("/me")
    @ApiOperation("获取用户自己的信息")
    public Result me(){
        //获取当前登录的用户并返回
        return Result.ok(UserHolder.getUser());
    }

}


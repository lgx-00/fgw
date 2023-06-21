package com.pxxy.controller;

import com.pxxy.dto.LoginFormDTO;
import com.pxxy.service.UserService;
import com.pxxy.utils.ResultResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * @Author: hesen
 * @Date: 2023-06-21-16:00
 * @Description:
 */
@RestController
@Api(tags = "登录")
@RequestMapping("/log")
public class LoginController {
    @Resource
    private UserService userService;

    @ApiOperation("用户登录")
    @PostMapping("/in")
    public ResultResponse login(@RequestBody LoginFormDTO loginForm, HttpSession session) {
        //实现登录功能
        return userService.login(loginForm, session);
    }

    @ApiOperation("退出登录")
    @PostMapping("/out")
    public ResultResponse logout(HttpSession session) {
        //实现登出功能
        return userService.logout(session);
    }

}

package com.pxxy.controller;


import com.pxxy.dto.LoginFormDTO;
import com.pxxy.dto.Result;
import com.pxxy.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

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
}


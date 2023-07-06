package com.pxxy.controller;

import com.pxxy.dto.LoginFormDTO;
import com.pxxy.service.UserService;
import com.pxxy.utils.RandomTokenUtil;
import com.pxxy.utils.ResultResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @Author: hesen
 * @Date: 2023-06-21-16:00
 * @Description:
 */
@Controller
@Api(tags = "登录")
@RequestMapping("/log")
public class LoginController {
    @Resource
    private UserService userService;

    @ApiOperation("用户登录")
    @PostMapping("/in")
    @ResponseBody
    public ResultResponse<String> login(@RequestBody LoginFormDTO loginForm, HttpSession session) {
        //实现登录功能
        return userService.login(loginForm, session);
    }

    @ApiOperation("退出登录")
    @PostMapping("/out")
    @ResponseBody
    public ResultResponse<String> logout(HttpServletRequest req, HttpSession session) {
        //实现登出功能
        RandomTokenUtil.invalid(req.getHeader("X-Token"));
        return userService.logout(session);
    }

    @ApiOperation("测试导出")
    @GetMapping("/test")
    public String test(){
        return "fetch";
    }


}

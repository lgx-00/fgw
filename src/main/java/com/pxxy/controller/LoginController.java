package com.pxxy.controller;

import com.pxxy.dto.LoginFormDTO;
import com.pxxy.service.UserService;
import com.pxxy.utils.TokenUtil;
import com.pxxy.utils.ResultResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @Author: hesen
 * @Date: 2023-06-21-16:00
 * @Description:
 */
@Controller
@CrossOrigin
@Api(tags = "登录")
@RequestMapping("/log")
public class LoginController {

    @Resource
    private UserService userService;

    @ResponseBody
    @PostMapping("/in")
    @ApiOperation("用户登录")
    public ResultResponse<String> login(@RequestBody LoginFormDTO loginForm) {
        //实现登录功能
        return userService.login(loginForm);
    }

    @ResponseBody
    @PostMapping("/out")
    @ApiOperation("退出登录")
    public ResultResponse<String> logout(HttpServletRequest req) {
        //实现登出功能
        TokenUtil.invalid(req.getHeader("X-Token"));
        return ResultResponse.ok("退出成功！");
    }

    @GetMapping("/test")
    @ApiOperation("测试导出")
    public String test(){
        return "fetch";
    }

}

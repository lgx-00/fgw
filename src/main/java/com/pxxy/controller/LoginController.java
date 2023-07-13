package com.pxxy.controller;

import com.pxxy.dto.UserDTO;
import com.pxxy.vo.LoginVO;
import com.pxxy.service.UserService;
import com.pxxy.utils.TokenUtil;
import com.pxxy.utils.ResultResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import static com.pxxy.constant.ResponseMessage.FAIL_MSG;

/**
 * @author hesen
 * @since 2023-06-21-16:00
 */
@Validated
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
    public ResultResponse<String> login(@RequestBody @Validated LoginVO loginVO) {
        return userService.login(loginVO);
    }

    @ResponseBody
    @PostMapping("/out")
    @ApiOperation("退出登录")
    public ResultResponse<?> logout(@RequestHeader("X-Token") String token) {
        //实现登出功能
        UserDTO userDTO = TokenUtil.invalid(token);
        return userDTO != null ? ResultResponse.ok() : ResultResponse.fail(FAIL_MSG);
    }

    @GetMapping("/test")
    @ApiOperation("测试导出")
    public String test() {
        return "fetch";
    }

}

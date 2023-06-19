package com.pxxy.controller;
import com.pxxy.dto.LoginFormDTO;
import com.pxxy.utils.ResultResponse;
import com.pxxy.service.UserService;
import com.pxxy.utils.UserHolder;
import com.pxxy.vo.AddUserVO;
import com.pxxy.vo.UpdateUserVO;
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
    public ResultResponse login(@RequestBody LoginFormDTO loginForm, HttpSession session) {
        //实现登录功能
        return userService.login(loginForm, session);
    }

    @PostMapping("/logout")
    @ApiOperation("退出登录")
    public ResultResponse logout(HttpSession session){
        //实现登出功能
        return userService.logout(session);
    }

    @GetMapping("/me")
    @ApiOperation("获取用户自己的信息")
    public ResultResponse me(){
        //获取当前登录的用户并返回
        return ResultResponse.ok(UserHolder.getUser());
    }

    //前端先获取用户信息再发送请求来获取对应角色
    @GetMapping("/getRole/{userId}")
    @ApiOperation("获取用户角色")
    public ResultResponse getRoleByUserId(@PathVariable Integer userId){
        return userService.getRoleByUserId(userId);
    }

    @PostMapping("/add")
    @ApiOperation("新增用户")
    public ResultResponse addUser(@RequestBody AddUserVO addUserVO){
        //实现新增功能
        return userService.addUser(addUserVO);
    }

    @PostMapping("/delete/{userId}")
    @ApiOperation("删除用户")
    public ResultResponse deleteUser(@PathVariable Integer userId){
        return userService.deleteUser(userId);
    }

    @PostMapping("/modify/{userId}")
    @ApiOperation("修改用户")
    public ResultResponse modifyUser(@PathVariable Integer userId, @RequestBody UpdateUserVO updateUserVO){
        return userService.modifyUser(userId,updateUserVO);
    }

    @GetMapping("/getAll")
    @ApiOperation("分页查询所有用户")
    public ResultResponse getAllUser(@RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum){
        return userService.getAllUser(pageNum);
    }

    @GetMapping("/getVague")
    @ApiOperation("模糊查询用户")
    public ResultResponse getVagueUser(@RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum, @RequestParam String uName){
        return userService.getVagueUser(pageNum,uName);
    }
}


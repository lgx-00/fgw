package com.pxxy.controller;

import com.github.pagehelper.PageInfo;
import com.pxxy.dto.UserDTO;
import com.pxxy.service.UserService;
import com.pxxy.utils.ResultResponse;
import com.pxxy.utils.UserHolder;
import com.pxxy.vo.AddUserVO;
import com.pxxy.vo.QueryUserVO;
import com.pxxy.vo.UpdateUserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author hs
 * @since 2023-06-13
 */
@Validated
@RestController
@Api(tags = "用户")
@RequestMapping("/sys/user")
public class UserController {
    @Resource
    private UserService userService;

    @GetMapping
    @ApiOperation("获取用户自己的信息")
    public ResultResponse<UserDTO> me() {
        //获取当前登录的用户并返回
        return ResultResponse.ok(UserHolder.getUser());
    }

    @PostMapping
    @ApiOperation("新增用户")
    public ResultResponse<?> addUser(@RequestBody @Validated AddUserVO addUserVO) {
        //实现新增功能
        return userService.addUser(addUserVO);
    }

    @DeleteMapping
    @ApiOperation("删除用户")
    public ResultResponse<?> deleteUser(@RequestParam Integer userId) { return userService.deleteUser(userId);
    }

    @PutMapping
    @ApiOperation("修改用户")
    public ResultResponse<?> modifyUser(@RequestBody @Validated UpdateUserVO updateUserVO) {
        return userService.modifyUser(updateUserVO);
    }

    @GetMapping("/all")
    @ApiOperation("分页查询所有用户")
    public ResultResponse<PageInfo<QueryUserVO>> getAllUser(@RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum) {
        return userService.getAllUser(pageNum);
    }

    @GetMapping("/vague")
    @ApiOperation("模糊查询用户")
    public ResultResponse<PageInfo<QueryUserVO>> getVagueUser(@RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum, @RequestParam String uName) {
        return userService.getVagueUser(pageNum,uName);
    }
}


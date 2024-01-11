package com.pxxy.controller;

import com.github.pagehelper.PageInfo;
import com.pxxy.entity.dto.UserDTO;
import com.pxxy.entity.vo.*;
import com.pxxy.service.UserService;
import com.pxxy.utils.ResultResponse;
import com.pxxy.utils.UserHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import static com.pxxy.constant.ResponseMessage.*;
import static com.pxxy.utils.ResultResponse.fail;
import static com.pxxy.utils.ResultResponse.ok;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author hs
 * @since 2023-06-13
 */
@Validated
@CrossOrigin
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
        return userService.addUser(addUserVO) ? ok() : fail(ADD_FAILED);
    }

    @DeleteMapping
    @ApiOperation("删除用户")
    public ResultResponse<?> deleteUser(@RequestParam Integer userId) {
        return userService.deleteUser(userId) ? ok() : fail(DELETE_FAILED);
    }

    @PutMapping("/passwd")
    @ApiOperation("修改密码")
    public ResultResponse<?> updatePassword(@RequestBody @Validated UpdatePasswordVO vo) throws BindException {
        UpdateUserVO updateUserVO = new UpdateUserVO();
        updateUserVO.setUPassword(vo.getPasswd());
        return userService.updateUserPassword(vo.getOld(), updateUserVO) ? ok() : fail(FAIL_MSG);
    }

    @PutMapping
    @ApiOperation("修改用户")
    public ResultResponse<?> modifyUser(@RequestBody @Validated UpdateUserVO updateUserVO) {
        return userService.updateUser(updateUserVO) ? ok() : fail(UPDATE_FAILED);
    }

    @GetMapping("/all")
    @ApiOperation("分页查询所有用户")
    public ResultResponse<PageInfo<QueryUserVO>> getAllUser(@ModelAttribute @Validated Page page) {
        return ok(userService.getAllUser(page));
    }

    @GetMapping("/vague")
    @ApiOperation("模糊查询用户")
    public ResultResponse<PageInfo<QueryUserVO>> getVagueUser(@ModelAttribute @Validated Page page, @RequestParam String uName) {
        return ok(userService.getVagueUser(page, uName));
    }
}


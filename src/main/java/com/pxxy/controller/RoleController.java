package com.pxxy.controller;


import com.pxxy.service.RoleService;
import com.pxxy.utils.ResultResponse;
import com.pxxy.vo.AddRoleVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author hs
 * @since 2023-06-14
 */
@RestController
@RequestMapping("/role")
@Api(tags = "角色")
public class RoleController {
    @Resource
    private RoleService roleService;

    @GetMapping("/getAll")
    @ApiOperation("分页查询所有角色")
    public ResultResponse getAllRole(@RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum){
        return roleService.getAllRole(pageNum);
    }

    @GetMapping("/getVague")
    @ApiOperation("模糊查询角色名")
    public ResultResponse getVagueRole(@RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum, @RequestParam String rName){
        return roleService.getVagueRole(pageNum,rName);
    }

    @PostMapping("/add")
    @ApiOperation("新增角色")
    public ResultResponse test(@RequestBody AddRoleVO roleVO) {
        return roleService.addRole(roleVO);
    }
}


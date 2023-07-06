package com.pxxy.controller;


import com.github.pagehelper.PageInfo;
import com.pxxy.service.RoleService;
import com.pxxy.utils.ResultResponse;
import com.pxxy.vo.AddRoleVO;
import com.pxxy.vo.QueryRoleVO;
import com.pxxy.vo.UpdateRoleVO;
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
 * @since 2023-06-14
 */
@RestController
@Validated
@RequestMapping("/sys/role")
@Api(tags = "角色")
public class RoleController {
    @Resource
    private RoleService roleService;

    @GetMapping("/all")
    @ApiOperation("分页查询所有角色")
    public ResultResponse<PageInfo<QueryRoleVO>> getAllRole(@RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum){
        return roleService.getAllRole(pageNum);
    }

    @GetMapping("/vague")
    @ApiOperation("模糊查询角色名")
    public ResultResponse<PageInfo<QueryRoleVO>> getVagueRole(@RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum, @RequestParam String rName){
        return roleService.getVagueRole(pageNum,rName);
    }

    @PostMapping
    @ApiOperation("新增角色")
    public ResultResponse<?> addRole(@RequestBody @Validated AddRoleVO addRoleVO) {
        return roleService.addRole(addRoleVO);
    }

    @DeleteMapping
    @ApiOperation("删除角色")
    public ResultResponse<?> deleteRole(@RequestParam Integer roleId) {
        return roleService.deleteRole(roleId);
    }

    @PutMapping
    @ApiOperation("修改角色")
    public ResultResponse<?> updateRole(@RequestBody @Validated UpdateRoleVO updateRoleVO) {
        return roleService.updateRole(updateRoleVO);
    }


}


package com.pxxy.controller;


import com.github.pagehelper.PageInfo;
import com.pxxy.entity.pojo.Permission;
import com.pxxy.service.PermissionService;
import com.pxxy.service.RoleService;
import com.pxxy.utils.ResultResponse;
import com.pxxy.entity.vo.AddRoleVO;
import com.pxxy.entity.vo.Page;
import com.pxxy.entity.vo.QueryRoleVO;
import com.pxxy.entity.vo.UpdateRoleVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author hs
 * @since 2023-06-14
 */
@Validated
@CrossOrigin
@RestController
@Api(tags = "角色")
@RequestMapping("/sys/role")
public class RoleController {

    @Resource
    private RoleService roleService;

    @Resource
    private PermissionService permService;

    @GetMapping("/all")
    @ApiOperation("分页查询所有角色")
    public ResultResponse<PageInfo<QueryRoleVO>> getAllRole(@ModelAttribute @Validated Page page) {
        return roleService.getAllRole(page);
    }

    @GetMapping("/vague")
    @ApiOperation("模糊查询角色名")
    public ResultResponse<PageInfo<QueryRoleVO>> getVagueRole(@ModelAttribute @Validated Page page, @RequestParam String rName) {
        return roleService.getVagueRole(page, rName);
    }

    @GetMapping("/perm")
    @ApiOperation("查询所有权限")
    public ResultResponse<List<Permission>> getPerm() {
        return permService.getPerm();
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


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

import static com.pxxy.constant.ResponseMessage.*;
import static com.pxxy.utils.ResultResponse.fail;
import static com.pxxy.utils.ResultResponse.ok;

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
        return ok(roleService.getAllRole(page));
    }

    @GetMapping("/vague")
    @ApiOperation("模糊查询角色名")
    public ResultResponse<PageInfo<QueryRoleVO>> getVagueRole(@ModelAttribute @Validated Page page, @RequestParam String rName) {
        return ok(roleService.getVagueRole(page, rName));
    }

    @GetMapping("/perm")
    @ApiOperation("查询所有权限")
    public ResultResponse<List<Permission>> getPerm() {
        return ok(permService.getPerm());
    }

    @PostMapping
    @ApiOperation("新增角色")
    public ResultResponse<?> addRole(@RequestBody @Validated AddRoleVO addRoleVO) {
        return roleService.addRole(addRoleVO) ? ok() : fail(ADD_FAILED);
    }

    @DeleteMapping
    @ApiOperation("删除角色")
    public ResultResponse<?> deleteRole(@RequestParam Integer roleId) {
        return roleService.deleteRole(roleId) ? ok() : fail(DELETE_FAILED);
    }

    @PutMapping
    @ApiOperation("修改角色")
    public ResultResponse<?> updateRole(@RequestBody @Validated UpdateRoleVO updateRoleVO) {
        return roleService.updateRole(updateRoleVO) ? ok() : fail(UPDATE_FAILED);
    }


}


package com.pxxy.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pxxy.mapper.RoleMapper;
import com.pxxy.pojo.Permission;
import com.pxxy.pojo.Role;
import com.pxxy.pojo.RolePermission;
import com.pxxy.service.PermissionService;
import com.pxxy.service.RolePermissionService;
import com.pxxy.service.RoleService;
import com.pxxy.service.UserRoleService;
import com.pxxy.utils.ResultResponse;
import com.pxxy.vo.AddRoleVO;
import com.pxxy.vo.QueryRoleVO;
import com.pxxy.vo.UpdateRoleVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.pxxy.constant.SystemConstant.DEFAULT_PAGE_SIZE;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hs
 * @since 2023-06-14
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Resource
    private PermissionService permissionService;

    @Resource
    private RolePermissionService rolePermissionService;

    @Resource
    private UserRoleService userRoleService;

    @Override
    public ResultResponse getAllRole(Integer pageNum) {
        // 根据类型分页查询
        Page<Role> page = query().page(new Page<>(pageNum, DEFAULT_PAGE_SIZE));
        List<Role> roleList = page.getRecords();
        List<QueryRoleVO> queryRoleVOS = roleList.stream().map(role -> {
            QueryRoleVO queryRoleVO = new QueryRoleVO();
            BeanUtil.copyProperties(role, queryRoleVO);
            List<RolePermission> rolePermissionList = rolePermissionService.query().eq("r_id", role.getRId()).list();
            List<String> permission = rolePermissionList.stream().map(rolePermission -> {
                Permission p = permissionService.query().eq("p_id", rolePermission.getPId()).one();
                if (p != null){
                    return p.getPName();
                }
                return null;
            }).filter(Objects::nonNull).collect(Collectors.toList());
            queryRoleVO.setPermission(permission);
            return queryRoleVO;
        }).collect(Collectors.toList());
        return ResultResponse.ok(queryRoleVOS, (int) page.getTotal());
    }

    @Override
    public ResultResponse getVagueRole(Integer pageNum, String rName) {
        // 根据类型分页查询
        Page<Role> page = query().like("r_name",rName).page(new Page<>(pageNum, DEFAULT_PAGE_SIZE));
        List<Role> roleList = page.getRecords();
        List<QueryRoleVO> queryRoleVOS = roleList.stream().map(role -> {
            QueryRoleVO queryRoleVO = new QueryRoleVO();
            BeanUtil.copyProperties(role, queryRoleVO);
            List<RolePermission> rolePermissionList = rolePermissionService.query().eq("r_id", role.getRId()).list();
            List<String> permisssion = rolePermissionList.stream().map(rolePermission -> {
                Permission permission = permissionService.query().eq("p_id", rolePermission.getPId()).one();
                if (permission != null){
                    return permission.getPName();
                }
                return null;
            }).collect(Collectors.toList());

            queryRoleVO.setPermission(permisssion);
            return queryRoleVO;
        }).filter(Objects::nonNull).collect(Collectors.toList());
        return ResultResponse.ok(queryRoleVOS, (int) page.getTotal());
    }

    @Override
    @Transactional
    public ResultResponse addRole(AddRoleVO addRoleVO) {
        Role role = new Role();
        role.setRName(addRoleVO.getRName());
        save(role);
        addRoleVO.getPermissionMapper().forEach((key, value) -> {
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRId(role.getRId());
            rolePermission.setPId(key);
            rolePermission.setRpDetail(value);
            rolePermissionService.save(rolePermission);
        });
        return ResultResponse.ok();
    }

    @Override
    public ResultResponse deleteRole(Integer roleId) {
        Role role = query().eq("r_id", roleId).one();
        if (role == null){
            return ResultResponse.fail("非法操作");
        }
        removeById(roleId);
        return ResultResponse.ok();
    }

    @Override
    @Transactional
    public ResultResponse updateRole(UpdateRoleVO updateRoleVO) {
        Role role = query().eq("r_id", updateRoleVO.getRId()).one();
        if (role == null){
            return ResultResponse.fail("非法操作");
        }
        role.setRName(updateRoleVO.getRName());
        updateById(role);
        //先删除后添加
        LambdaQueryWrapper<RolePermission> rolePermissionLambdaQueryWrapper = new LambdaQueryWrapper<>();
        rolePermissionLambdaQueryWrapper.eq(RolePermission::getRId,role.getRId());
        rolePermissionService.remove(rolePermissionLambdaQueryWrapper);
        updateRoleVO.getPermissionMapper().forEach((key, value) -> {
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRId(role.getRId());
            rolePermission.setPId(key);
            rolePermission.setRpDetail(value);
            rolePermissionService.save(rolePermission);
        });
        return ResultResponse.ok();
    }


}

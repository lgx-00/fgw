package com.pxxy.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pxxy.mapper.RoleMapper;
import com.pxxy.pojo.Role;
import com.pxxy.pojo.RolePermission;
import com.pxxy.service.PermissionService;
import com.pxxy.service.RolePermissionService;
import com.pxxy.service.RoleService;
import com.pxxy.utils.ResultResponse;
import com.pxxy.vo.AddRoleVO;
import com.pxxy.vo.QueryRoleVO;
import com.pxxy.vo.UpdateRoleVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

import static com.pxxy.constant.SystemConstant.*;

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

    @Override
    @Transactional
    public ResultResponse getAllRole(Integer pageNum) {
        // 根据类型分页查询
        Page<Role> page = query().page(new Page<>(pageNum, DEFAULT_PAGE_SIZE));
        List<Role> roleList = page.getRecords().stream().filter(role -> role.getRStatus() != DELETED_STATUS).collect(Collectors.toList());
        List<QueryRoleVO> queryRoleVOS = roleList.stream().map(role -> {
            QueryRoleVO queryRoleVO = new QueryRoleVO();
            BeanUtil.copyProperties(role, queryRoleVO);
            List<RolePermission> rolePermissionList = rolePermissionService.query().eq("r_id", role.getRId()).list();
            List<String> permission = rolePermissionList.stream().map(rolePermission -> {
                return permissionService.query().eq("p_id", rolePermission.getPId()).one().getPName();
            }).collect(Collectors.toList());
            queryRoleVO.setPermission(permission);
            return queryRoleVO;
        }).collect(Collectors.toList());
        return ResultResponse.ok(queryRoleVOS);
    }

    @Override
    @Transactional
    public ResultResponse getVagueRole(Integer pageNum, String rName) {
        // 根据类型分页查询
        Page<Role> page = query().like("r_name",rName).page(new Page<>(pageNum, DEFAULT_PAGE_SIZE));
        List<Role> roleList = page.getRecords().stream().filter(role -> role.getRStatus() != DELETED_STATUS).collect(Collectors.toList());
        List<QueryRoleVO> queryRoleVOS = roleList.stream().map(role -> {
            QueryRoleVO queryRoleVO = new QueryRoleVO();
            BeanUtil.copyProperties(role, queryRoleVO);
            List<RolePermission> rolePermissionList = rolePermissionService.query().eq("r_id", role.getRId()).list();
            List<String> permisssion = rolePermissionList.stream().map(rolePermission ->
                    permissionService.query().eq("p_id", rolePermission.getPId()).one().getPName()).collect(Collectors.toList());
            queryRoleVO.setPermission(permisssion);
            return queryRoleVO;
        }).collect(Collectors.toList());
        return ResultResponse.ok(queryRoleVOS);
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
    @Transactional
    public ResultResponse deleteRole(Integer roleId) {
        Role role = query().eq("r_id", roleId).ne("r_status", DELETED_STATUS).one();
        if (role == null){
            return ResultResponse.fail("非法操作");
        }
        role.setRStatus(DELETED_STATUS);
        updateById(role);
        LambdaQueryWrapper<RolePermission> rolePermissionLambdaQueryWrapper = new LambdaQueryWrapper<>();
        rolePermissionLambdaQueryWrapper.eq(RolePermission::getRId,roleId);
        rolePermissionService.remove(rolePermissionLambdaQueryWrapper);
        return ResultResponse.ok();
    }

    @Override
    @Transactional
    public ResultResponse updateRole(UpdateRoleVO updateRoleVO) {
        Role role = query().eq("r_id", updateRoleVO.getRId()).ne("r_status", DELETED_STATUS).one();
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

package com.pxxy.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.pxxy.advice.annotations.Cached;
import com.pxxy.mapper.RoleMapper;
import com.pxxy.pojo.Permission;
import com.pxxy.pojo.Role;
import com.pxxy.pojo.RolePermission;
import com.pxxy.service.PermissionService;
import com.pxxy.service.RolePermissionService;
import com.pxxy.service.RoleService;
import com.pxxy.utils.PageUtil;
import com.pxxy.utils.ResultResponse;
import com.pxxy.vo.AddRoleVO;
import com.pxxy.vo.Page;
import com.pxxy.vo.QueryRoleVO;
import com.pxxy.vo.UpdateRoleVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.pxxy.constant.ResponseMessage.ADD_FAILED;
import static com.pxxy.constant.ResponseMessage.ILLEGAL_OPERATE;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hs
 * @since 2023-06-14
 */
@Service
@Cached(parent = {PermissionServiceImpl.class})
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Resource
    private PermissionService permissionService;

    @Resource
    private RolePermissionService rolePermissionService;

    private final Function<Role, QueryRoleVO> mapRoleToVO = role -> {
        QueryRoleVO queryRoleVO = new QueryRoleVO();
        BeanUtil.copyProperties(role, queryRoleVO);
        List<RolePermission> rolePermissionList = rolePermissionService.query().eq("r_id", role.getRId()).list();
        List<String> permisssion = rolePermissionList.stream().map(rolePermission -> {
            Permission permission = permissionService.query().eq("p_id", rolePermission.getPId()).one();
            if (permission != null) {
                return permission.getPName();
            }
            return null;
        }).collect(Collectors.toList());
        queryRoleVO.setPermission(permisssion);
        return queryRoleVO;
    };

    @Override
    public ResultResponse<PageInfo<QueryRoleVO>> getAllRole(Page page) {
        //  根据类型分页查询
        PageInfo<QueryRoleVO> pageInfo = PageUtil.selectPage(page, () -> query().list(), mapRoleToVO);
        return ResultResponse.ok(pageInfo);
    }

    @Override
    public ResultResponse<PageInfo<QueryRoleVO>> getVagueRole(Page page, String rName) {
        // 根据类型分页查询
        PageInfo<QueryRoleVO> pageInfo = PageUtil.selectPage(page,
                () -> query().like("r_name", rName).list(), mapRoleToVO);
        return ResultResponse.ok(pageInfo);
    }

    @Override
    @Transactional
    public ResultResponse<?> addRole(AddRoleVO addRoleVO) {
        Role role = new Role();
        role.setRName(addRoleVO.getRName());
        save(role);
        return formatPermission(role, addRoleVO.getPermissionMapper());
    }

    private ResultResponse<?> formatPermission(Role role, Map<Integer, Integer> permissionMapper) {
        try {
            permissionMapper.forEach((key, value) -> {
                RolePermission rolePermission = new RolePermission();
                rolePermission.setRId(role.getRId());
                rolePermission.setPId(key);
                rolePermission.setRpDetail(value);
                if (!rolePermissionService.save(rolePermission)) {
                    throw new RuntimeException("failed");
                }
            });
        } catch (Exception e) {
            if ("failed".equals(e.getMessage())) {
                return ResultResponse.fail(ADD_FAILED);
            }
            throw e;
        }
        return ResultResponse.ok();
    }

    @Override
    public ResultResponse<?> deleteRole(Integer roleId) {
        Role role = query().eq("r_id", roleId).one();
        if (role == null){
            return ResultResponse.fail(ILLEGAL_OPERATE);
        }
        removeById(roleId);
        return ResultResponse.ok();
    }

    @Override
    @Transactional
    public ResultResponse<?> updateRole(UpdateRoleVO updateRoleVO) {
        Role role = query().eq("r_id", updateRoleVO.getRId()).one();
        if (role == null){
            return ResultResponse.fail(ILLEGAL_OPERATE);
        }
        role.setRName(updateRoleVO.getRName());
        updateById(role);
        //先删除后添加
        LambdaQueryWrapper<RolePermission> rolePermissionLambdaQueryWrapper = new LambdaQueryWrapper<>();
        rolePermissionLambdaQueryWrapper.eq(RolePermission::getRId,role.getRId());
        rolePermissionService.remove(rolePermissionLambdaQueryWrapper);
        return formatPermission(role, updateRoleVO.getPermissionMapper());
    }


}

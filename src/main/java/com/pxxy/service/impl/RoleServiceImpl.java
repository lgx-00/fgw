package com.pxxy.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.pxxy.mapper.RoleMapper;
import com.pxxy.entity.pojo.Permission;
import com.pxxy.entity.pojo.Role;
import com.pxxy.entity.pojo.RolePermission;
import com.pxxy.service.PermissionService;
import com.pxxy.service.RolePermissionService;
import com.pxxy.service.RoleService;
import com.pxxy.utils.PageUtil;
import com.pxxy.utils.ResultResponse;
import com.pxxy.entity.vo.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.pxxy.constant.ResponseMessage.*;

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

    private final Function<Role, QueryRoleVO> mapRoleToVO = role -> {
        QueryRoleVO queryRoleVO = new QueryRoleVO();
        BeanUtil.copyProperties(role, queryRoleVO);
        return queryRoleVO;
    };

    @Override
    public ResultResponse<PageInfo<QueryRoleVO>> getAllRole(Page page) {
        // 根据类型分页查询
        PageInfo<QueryRoleVO> pageInfo = PageUtil.selectPage(page, () -> query().orderByDesc("r_id").list(), mapRoleToVO);

        return fillBean(pageInfo);
    }

    @Override
    public ResultResponse<PageInfo<QueryRoleVO>> getVagueRole(Page page, String rName) {
        // 根据类型分页查询
        PageInfo<QueryRoleVO> pageInfo = PageUtil.selectPage(page,
                () -> query().like("r_name", rName).orderByDesc("r_id").list(), mapRoleToVO);

        return fillBean(pageInfo);
    }

    private ResultResponse<PageInfo<QueryRoleVO>> fillBean(PageInfo<QueryRoleVO> pageInfo) {
        List<QueryRoleVO> queryRoleVOList = pageInfo.getList();
        Map<Integer, List<RolePermission>> roleIdMapRps = rolePermissionService.list()
                .stream().collect(Collectors.groupingBy(RolePermission::getRId));
        Map<Integer, Permission> pIdMapPerm = permissionService.getPerm().getData()
                .stream().collect(Collectors.toMap(Permission::getPId, p -> p));

        queryRoleVOList.forEach(vo -> vo.setPermission(
                Optional.ofNullable(roleIdMapRps.get(vo.getRId()))
                        .orElse(Collections.emptyList()).stream()
                        .map(rp -> new PermissionVO(rp.getRpDetail(), pIdMapPerm.get(rp.getPId())))
                        .collect(Collectors.toList())
        ));

        return ResultResponse.ok(pageInfo);
    }

    @Override
    @Transactional
    public ResultResponse<?> addRole(AddRoleVO addRoleVO) {
        if (addRoleVO.getPermissionMapper().isEmpty()) {
            return ResultResponse.fail(PERMISSION_CANNOT_BE_EMPTY);
        }
        Role role = new Role();
        role.setRName(addRoleVO.getRName());
        save(role);
        return savePerm(role, addRoleVO.getPermissionMapper());
    }

    private ResultResponse<?> savePerm(Role role, Map<Integer, Integer> permissionMapper) {
        if (Objects.nonNull(permissionMapper)) {
            List<RolePermission> rps = permissionMapper.entrySet()
                    .stream()
                    .filter(e -> e.getValue() > 7)
                    .map(e -> new RolePermission(null, e.getKey(), role.getRId(), e.getValue()))
                    .collect(Collectors.toList());
            rolePermissionService.saveBatch(rps);
        }
        return ResultResponse.ok();
    }

    @Override
    public ResultResponse<?> deleteRole(Integer roleId) {
        if (roleId == 1) {
            return ResultResponse.fail(CANNOT_DELETE_ADMINISTRATOR_ROLE);
        }
        Role role = query().eq("r_id", roleId).one();
        if (Objects.isNull(role)) {
            return ResultResponse.fail(ILLEGAL_OPERATE);
        }
        removeById(roleId);
        return ResultResponse.ok();
    }

    @Override
    @Transactional
    public ResultResponse<?> updateRole(UpdateRoleVO updateRoleVO) {
        if (updateRoleVO.getRId() == 1) {
            updateRoleVO.setPermissionMapper(null);
        } else {
            //先删除后添加
            LambdaQueryWrapper<RolePermission> rolePermissionLambdaQueryWrapper = new LambdaQueryWrapper<>();
            rolePermissionLambdaQueryWrapper.eq(RolePermission::getRId, updateRoleVO.getRId());
            rolePermissionService.remove(rolePermissionLambdaQueryWrapper);
        }
        if (updateRoleVO.getRId() != 1
                && (Objects.isNull(updateRoleVO.getPermissionMapper())
                || updateRoleVO.getPermissionMapper().isEmpty())) {
            return ResultResponse.fail(PERMISSION_CANNOT_BE_EMPTY);
        }
        Role role = query().eq("r_id", updateRoleVO.getRId()).one();
        if (Objects.isNull(role)) {
            return ResultResponse.fail(ILLEGAL_OPERATE);
        }
        role.setRName(updateRoleVO.getRName());
        updateById(role);
        return savePerm(role, updateRoleVO.getPermissionMapper());
    }

}
